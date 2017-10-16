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
import com.example.manifest.bcreco.data.Goods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends Activity {

    private static int GET_BARCODE_REQUEST = 1;
    private TextView barcodeText;
    private TextView modelText;
    private TextView colorText;
    private TextView sizeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeText = (TextView) findViewById(R.id.barcode_text);
        modelText = (TextView) findViewById(R.id.model_text);
        colorText = (TextView) findViewById(R.id.color_text);
        sizeText = (TextView) findViewById(R.id.size_text);

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
                barcodeText.setText(barcodeString);
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
                        // get ID_MODEL, ID_ColorVend, ID_Sizes from T_PLU table
                        String query = PluEntry.queryModelColorSizeFromIDPluTable(strings[0]);
                        resultSet = statement.executeQuery(query);
                        if (resultSet != null) {
                            resultSet.next();
                            int idModel = resultSet.getInt(DbContract.PluEntry.COLUMN_ID_MODEL);
                            int color = resultSet.getInt(DbContract.PluEntry.COLUMN_COLOR);
                            int size = resultSet.getInt(DbContract.PluEntry.COLUMN_ID_SIZE);
                            //TODO: change with real value.
                            goods = new Goods(strings[0], String.valueOf(idModel),
                                    String.valueOf(color), String.valueOf(size));
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
                modelText.setText(goods.getModel());
                colorText.setText(goods.getColor());
                sizeText.setText(goods.getSize());
            }
        }
    }
}
