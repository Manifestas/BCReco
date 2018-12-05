package com.example.manifest.bcreco.data;

import android.util.Log;

import com.example.manifest.bcreco.data.DbContract.ModelEntry;
import com.example.manifest.bcreco.data.DbContract.ColorEntry;
import com.example.manifest.bcreco.data.DbContract.SizeEntry;
import com.example.manifest.bcreco.data.DbContract.LogPluCostEntry;
import com.example.manifest.bcreco.data.DbContract.PluEntry;
import com.example.manifest.bcreco.data.DbContract.ObjectEntry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbHelper {

    private static final String TAG = DbHelper.class.getSimpleName();

    private static Connection connection;
    private static PreparedStatement statement;

    public static void dbConnect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbUrl = String.format(DbContract.DB_CONN_URL, Prefs.getIp(), Prefs.getPort(), Prefs.getLogin(), Prefs.getPassword());
            connection = DriverManager.getConnection(dbUrl);

            Log.i(TAG,"Got connection: " + connection);
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
            Log.e(TAG, "Exception while disposing DB: " + e);
        }
    }

    public static void dbReconnect() {
        dispose();
        dbConnect();
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

    private static Product getProductFromResultSet(ResultSet resultSet) throws SQLException {
        String modelName = resultSet.getString(ModelEntry.COLUMN_MODEL);
        String color = resultSet.getString(ColorEntry.COLUMN_COLOR);
        String size = resultSet.getString(SizeEntry.COLUMN_SIZE_NAME);

        Product currentProduct = new Product(modelName, color, size);

        log.finest("Getting a Product from ResultSet: " + currentProduct);

        return currentProduct;
    }

    /**
     * Returns the product received from the database with this pluID,
     * if there are no any sizes of this article on the residuals.
     * We can't use barcode search, because there is no barcode in database for absolute new product
     * while invoice won't be closed. And we can't specify the object immediately in the query,
     * because if the product hasn't been in your object yet the quantity won't be displayed (even zero)
     * and the query returns null.
     *
     * @param pluId of product.
     * @return A Product with this pluID, if quantity of any of its sizes is zero.
     */
    public static Product returnProductIfNew(String pluId) {
        Product product = null;
        try {
            if (connection == null) {
                log.finest("Connection == null");

                dbConnect();
            }
            try (ResultSet rs = getResultSet(pluId)) {
                if (rs == null) {
                    log.finest("ResultSet == null");
                    MidiPlayer.playAlarmSound();

                    return null;
                }
                String userObjectID = Prefs.getObject();
                // check each size and each object
                while (rs.next()) {
                    boolean objectIsTheSame = rs.getString(ObjectEntry.COLUMN_OBJECT).equals(userObjectID);
                    //if this is the same object as ours and the quantity of goods with this size is greater than zero
                    if (objectIsTheSame && rs.getInt(LogPluCostEntry.COLUMN_QUANTITY) > 0) {
                        log.info(rs.getString(ModelEntry.COLUMN_MODEL) + " > 0");
                        //immediately go out with null
                        return null;
                    }
                    //if we haven't found our product and  ID_PLU matches argument...
                    if (product == null && rs.getString(DbContract.PluEntry.COLUMN_ID).equals(pluId)) {
                        // ... remember it in product
                        product = getProductFromResultSet(rs);
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
        log.finest("Returning a new product: " + product);
        return product;
    }
}
