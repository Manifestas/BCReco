package com.example.manifest.bcreco;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manifest.bcreco.data.DbContract;
import com.example.manifest.bcreco.data.DbContract.ColorEntry;
import com.example.manifest.bcreco.data.DbContract.ExchangeEntry;
import com.example.manifest.bcreco.data.DbContract.ModelEntry;
import com.example.manifest.bcreco.data.DbContract.PluEntry;
import com.example.manifest.bcreco.data.DbContract.SeasonEntry;
import com.example.manifest.bcreco.data.DbContract.SizeEntry;
import com.example.manifest.bcreco.data.Goods;
import com.example.manifest.bcreco.settings.SettingsActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //this code will be returned in onActivityResult() when the activity exits.
    //By it we determine from which Activity came the result
    private static int GET_BARCODE_REQUEST = 1;

    private TextView modelTextView;
    private TextView colorTextView;
    private TextView modelDescTextView;
    private TextView seasonTextView;
    private TextView sizeTextView;
    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelTextView = (TextView) findViewById(R.id.tv_model);
        colorTextView = (TextView) findViewById(R.id.tv_color);
        modelDescTextView = (TextView) findViewById(R.id.tv_model_desc);
        seasonTextView = (TextView) findViewById(R.id.tv_season);
        sizeTextView = (TextView) findViewById(R.id.tv_size);
        priceTextView = (TextView) findViewById(R.id.tv_price);

        Button barcodeBtn = (Button) findViewById(R.id.barcode_btn);
        barcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivityForResult(intent, GET_BARCODE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        // Check which request we're responding to
        if (requestCode == GET_BARCODE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get barcode from intent
                String barcodeString = data.getStringExtra(CameraActivity.EXTRA_BCVALUE);
                new GoodsDBAsyncTask().execute(barcodeString);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // Return true so that menu is displayed in the toolbar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When Settings menu item is pressed, open SettingActivity
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GoodsDBAsyncTask extends AsyncTask<String, Void, Goods> {

        @Override
        protected Goods doInBackground(String... barcodes) {
            // Don't perform the request if there are no barcodes, or the first barcode is null
            if (barcodes.length < 1 || barcodes[0] == null) {
                return null;
            }
            Goods goods = null;
            try {
                // initialize JDBC driver
                // The newInstance() call is a work around for some broken Java implementations
                //this creates some static objects that we need.
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection connection = null;
                Statement statement = null;
                ResultSet rs = null;
                try {
                    // establish a database connection
                    connection = DriverManager.getConnection(DbContract.DB_CONN_URL);
                    if (connection != null) {
                        // A Statement is an interface that represents a SQL statement.
                        // You execute Statement objects, and they generate ResultSet objects,
                        // which is a table of data representing a database result set.
                        statement = connection.createStatement();
                        /*You access the data in a ResultSet object through a cursor.
                         Note that this cursor is not a database cursor.
                         This cursor is a pointer that points to one row of data in the ResultSet.
                         Initially, the cursor is positioned before the first row
                         */
                        rs = statement.executeQuery(DbContract.goodsQuery(barcodes[0]));
                        if (rs != null) {
                            rs.next();

                            String model = rs.getString(ModelEntry.COLUMN_MODEL);
                            String color = rs.getString(ColorEntry.COLUMN_COLOR);
                            String modelDesc = rs.getString(ModelEntry.COLUMN_MODEL_DESC);
                            String season = rs.getString(SeasonEntry.COLUMN_SEASON);
                            float currencyPrice = rs.getFloat(PluEntry.COLUMN_CURRENT_PRICE);
                            float exchangeRate = rs.getFloat(ExchangeEntry.COLUMN_EXCHANGE_RATE);
                            String size = rs.getString(SizeEntry.COLUMN_SIZE_NAME);

                            // get int rounded price in rubles
                            int rubPrice = Math.round(currencyPrice * exchangeRate);

                            goods = new Goods(model, color, modelDesc, season, rubPrice, size);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // Immediately release the resources it is using.
                        if (rs != null) rs.close();
                        if (statement != null) statement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return goods;
        }

        @Override
        protected void onPostExecute(Goods goods) {
            if (goods != null) {
                modelTextView.setText(goods.getModel());
                colorTextView.setText(goods.getColor());
                modelDescTextView.setText(goods.getModelDesc());
                seasonTextView.setText(goods.getSeason());
                sizeTextView.setText(String.valueOf(goods.getSize()));
                priceTextView.setText(String.valueOf(goods.getPrice()));
            }
        }
    }
}
