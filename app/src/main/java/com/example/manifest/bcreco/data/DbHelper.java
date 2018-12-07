package com.example.manifest.bcreco.data;

import android.util.Log;

import com.example.manifest.bcreco.data.DbContract.ColorEntry;
import com.example.manifest.bcreco.data.DbContract.ExchangeEntry;
import com.example.manifest.bcreco.data.DbContract.LogPluCostEntry;
import com.example.manifest.bcreco.data.DbContract.ModelEntry;
import com.example.manifest.bcreco.data.DbContract.ObjectEntry;
import com.example.manifest.bcreco.data.DbContract.PluEntry;
import com.example.manifest.bcreco.data.DbContract.SeasonEntry;
import com.example.manifest.bcreco.data.DbContract.SizeEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    private static Connection connection;
    private static PreparedStatement statement;

    public static void dbConnect(DbConnectionParams connectionParams) {
        try {
            // initialize JDBC driver
            // The newInstance() call is a work around for some broken Java implementations
            // this creates some static objects that we need.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String dbUrl = String.format(DbContract.DB_CONN_URL_FROM_PREF,
                    connectionParams.getIp(),
                    connectionParams.getPort(),
                    connectionParams.getLogin(),
                    connectionParams.getPassword());
            connection = DriverManager.getConnection(dbUrl);

            Log.i(TAG, "Got connection: " + connection);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException while loading JDBC driver: " + e);
        } catch (SQLException e) {
            Log.e(TAG, "Database access error: " + e);
        }
    }

    private static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
            Log.i(TAG, "Closing connection");
        }
    }

    private static void closeStatement() throws SQLException {
        if (statement != null) {
            statement.close();
            statement = null;
            Log.d(TAG, "Closing statement");
        }
    }

    public static void dispose() {
        try {
            closeStatement();
            closeConnection();
        } catch (SQLException e) {
            Log.w(TAG, "Exception while disposing DB: " + e);
        }
    }

    public static void dbReconnect(DbConnectionParams connectionParams) {
        dispose();
        dbConnect(connectionParams);
    }

    private static ResultSet getResultSet(String pluId) throws SQLException {
        if (statement == null) {
            // using PreparedStatement instead Statement reduces execution time.
            statement = connection.prepareStatement(DbContract.queryPluQty);
        }
        // replace first question mark placeholder with second argument String.
        statement.setString(1, pluId);

        return statement.executeQuery();
    }

    private static Product getProductFromResultSet(ResultSet rs) throws SQLException {
        String model = rs.getString(ModelEntry.COLUMN_MODEL);
        String color = rs.getString(ColorEntry.COLUMN_COLOR);
        String modelDesc = rs.getString(ModelEntry.COLUMN_MODEL_DESC);
        String season = rs.getString(SeasonEntry.COLUMN_SEASON);
        float currencyPrice = rs.getFloat(PluEntry.COLUMN_CURRENT_PRICE);
        float exchangeRate = rs.getFloat(ExchangeEntry.COLUMN_EXCHANGE_RATE);

        // get int rounded price in rubles
        int rubPrice = Math.round(currencyPrice * exchangeRate);

        Product product = new Product(model, color, modelDesc, season, rubPrice);

        Log.d(TAG, "Getting a Product from ResultSet: " + product);

        return product;
    }

    /**
     * Returns the product received from specified in DbConnectionParams database with this barcode.
     * We can't use barcode search, because there is no barcode in database for absolute new product
     * while invoice won't be closed. And we can't specify the object immediately in the query,
     * because if the product hasn't been in your object yet the quantity won't be displayed (even zero)
     * and the query returns null.
     *
     * @param params  DataBase connection parameters.
     * @param barcode of product.
     * @return A Product with this barcode.
     */
    public static Product returnProductFromDb(DbConnectionParams params, String barcode) {
        String pluId = Product.getPluFromBarcode(barcode);
        Product product = null;
        try {
            if (connection == null) {
                Log.d(TAG, "Connection == null");

                dbConnect(params);
            }
            try (ResultSet rs = getResultSet(pluId)) {
                if (rs == null) {
                    Log.d(TAG, "ResultSet == null");
                    return null;
                }
                while (rs.next()) {
                    if (product == null) {
                        product = getProductFromResultSet(rs);
                    }
                    int quantity = rs.getInt(LogPluCostEntry.COLUMN_QUANTITY);
                    if (quantity != 0) {
                        String storeName = rs.getString(ObjectEntry.COLUMN_OBJECT);
                        int size = rs.getInt(SizeEntry.COLUMN_SIZE_NAME);
                        product.addStoreStockInfo(storeName, size, quantity);
                    }
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                Log.e(TAG, "SQLException while getting DB query: " + t);
            }
            // close all
            dispose();
        }
        Log.d(TAG, "Returning a new product: " + product);
        return product;
    }
}
