package com.example.manifest.bcreco;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manifest.bcreco.data.DbContract;
import com.example.manifest.bcreco.data.DbContract.PluEntry;
import com.example.manifest.bcreco.data.DbContract.ModelEntry;
import com.example.manifest.bcreco.data.DbContract.ColorEntry;
import com.example.manifest.bcreco.data.DbContract.SeasonEntry;
import com.example.manifest.bcreco.data.Goods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends Activity {

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

    private class GoodsDBAsyncTask extends AsyncTask<String, Void, Goods> {

        @Override
        protected Goods doInBackground(String... strings) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            Goods goods = null;
            try {
                // The newInstance() call is a work around for some broken Java implementations
                //this creates some static objects that we need.
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection connection = null;
                Statement statement = null;
                ResultSet resultSet = null;
                try {
                    connection = DriverManager.getConnection(DbContract.DB_CONN_URL);
                    if (connection != null) {
                        statement = connection.createStatement();
                        resultSet = statement.executeQuery(DbContract.goodsQuery(strings[0]));
                        if (resultSet != null) {
                            resultSet.next();

                            String model = resultSet.getString(ModelEntry.COLUMN_MODEL);
                            String color = resultSet.getString(ColorEntry.COLUMN_COLOR);
                            String modelDesc = resultSet.getString(ModelEntry.COLUMN_MODEL_DESC);
                            String season = resultSet.getString(SeasonEntry.COLUMN_SEASON);

                            //TODO: complete after check types


                            int sizeId = resultSet.getInt(PluEntry.COLUMN_ID_SIZE);
                            goods = new Goods(strings[0], modelName, colorName, modelDesc);
                            + PluEntry.TABLE_NAME + DOT + PluEntry.COLUMN_CURRENT_PRICE + COMMA
                                    + SizeEntry.TABLE_NAME + DOT + SizeEntry.COLUMN_SIZE_NAME + COMMA
                                    + ExchangeEntry.TABLE_NAME + DOT + ExchangeEntry.COLUMN_EXCHANGE_RATE + COMMA
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (resultSet != null) resultSet.close();
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
                sizeTextView.setText(goods.getSize());
                priceTextView.setText(goods.getPrice());
            }
        }
    }
}
