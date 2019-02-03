package com.example.manifest.bcreco;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.example.manifest.bcreco.data.DbConnectionParams;
import com.example.manifest.bcreco.data.DbHelper;
import com.example.manifest.bcreco.models.InfoFromSite;
import com.example.manifest.bcreco.models.Product;
import com.example.manifest.bcreco.settings.PrefHelper;
import com.example.manifest.bcreco.utils.QueryUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Product> product = new MutableLiveData<>();
    private MutableLiveData<InfoFromSite> infoFromSite = new MutableLiveData<>();
    private DbConnectionParams connectionParams;
    private String storeId;

    public MainViewModel(@NonNull Application application) {
        super(application);
        connectionParams = PrefHelper.getDbConnectionParams(application);
        storeId = PrefHelper.getCurrentStoreId(application);
    }

    public MutableLiveData<InfoFromSite> getInfoFromSite() {
        return infoFromSite;
    }

    public MutableLiveData<Product> getProduct() {
        return product;
    }

    public void init(String barcode) {
        loadProductFromDB(barcode);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadProductFromDB(String barcode) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... barcodes) {
                // Don't perform the request if there are no barcodes, or the first barcode is null
                if (barcodes.length < 1 || barcodes[0] == null) {
                    return null;
                }
                Product productFromDb = DbHelper.returnProductFromDb(connectionParams, barcodes[0], storeId);

                if (productFromDb != null) {
                    product.postValue(productFromDb);

                    String productModel = productFromDb.getModel();
                    infoFromSite.postValue(QueryUtils.fetchInfoFromSite(productModel));
                }
                return null;
            }
        }.execute(barcode);
    }
}
