package com.example.manifest.bcreco;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.example.manifest.bcreco.data.DbConnectionParams;
import com.example.manifest.bcreco.data.DbHelper;
import com.example.manifest.bcreco.models.Product;
import com.example.manifest.bcreco.settings.PrefHelper;
import com.example.manifest.bcreco.utils.QueryUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Product> product = new MutableLiveData<>();
    private DbConnectionParams connectionParams;

    public MainViewModel(@NonNull Application application) {
        super(application);
        connectionParams = PrefHelper.getDbConnectionParams(application);
    }

    public MutableLiveData<Product> getProduct() {
        return product;
    }

    public void init(String barcode) {
        loadProductFromDB(barcode);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadProductFromDB(String barcode) {
        new AsyncTask<String, Product, Product>() {
            @Override
            protected Product doInBackground(String... barcodes) {
                // Don't perform the request if there are no barcodes, or the first barcode is null
                if (barcodes.length < 1 || barcodes[0] == null) {
                    return null;
                }
                Product product = DbHelper.returnProductFromDb(connectionParams, barcodes[0]);

                if (product != null) {
                    publishProgress(product);

                    String productModel = product.getModel();
                    product.setInfoFromSite(QueryUtils.fetchInfoFromSite(productModel));
                }
                return product;
            }

            @Override
            protected void onProgressUpdate(Product... values) {
                super.onProgressUpdate(values);
                product.setValue(values[0]);
            }

            @Override
            protected void onPostExecute(Product productWithInfo) {
                product.setValue(productWithInfo);
            }
        }.execute(barcode);
    }
}
