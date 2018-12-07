package com.example.manifest.bcreco;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.example.manifest.bcreco.data.DbConnectionParams;
import com.example.manifest.bcreco.data.DbHelper;
import com.example.manifest.bcreco.models.Product;
import com.example.manifest.bcreco.settings.PrefHelper;

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

    @SuppressLint("StaticFieldLeak")
    private void loadProductFromDB(String barcode) {
        new AsyncTask<String, Void, Product>() {
            @Override
            protected Product doInBackground(String... barcodes) {
                // Don't perform the request if there are no barcodes, or the first barcode is null
                if (barcodes.length < 1 || barcodes[0] == null) {
                    return null;
                }
                return DbHelper.returnProductFromDb(connectionParams, barcodes[0]);
            }

            @Override
            protected void onPostExecute(Product productFromDb) {
                product.setValue(productFromDb);
            }
        };
    }
}
