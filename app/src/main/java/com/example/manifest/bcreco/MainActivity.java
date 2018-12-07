package com.example.manifest.bcreco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manifest.bcreco.settings.SettingsActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //this code will be returned in onActivityResult() when the activity exits.
    //By it we determine from which Activity came the result
    private static final int GET_BARCODE_REQUEST_CODE = 1;
    private static final int GET_PERMISSION_REQUEST_CODE = 2;

    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_CAMERA = 10;

    private View rootLayout;
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

        rootLayout = findViewById(R.id.root_layout);
        modelTextView = findViewById(R.id.tv_model);
        colorTextView = findViewById(R.id.tv_color);
        modelDescTextView = findViewById(R.id.tv_model_desc);
        seasonTextView = findViewById(R.id.tv_season);
        sizeTextView = findViewById(R.id.tv_size);
        priceTextView = findViewById(R.id.tv_price);

        Button barcodeBtn = findViewById(R.id.barcode_btn);
        barcodeBtn.setOnClickListener(view -> startReadingBarcode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_BARCODE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                // Get barcode from intent
                String barcodeString = data.getStringExtra(CameraActivity.EXTRA_BCVALUE);
                // TODO: new GoodsDBAsyncTask().execute(barcodeString);
            }
        } else if (requestCode == GET_PERMISSION_REQUEST_CODE) {
            Log.i(TAG, "Coming back from permission settings");
            startReadingBarcode();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestCameraPermission() {
        Log.i(TAG, "Permission hasn't been granted. Request permission");
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            Log.i(TAG, "Displaying camera permission rationale");
            Snackbar.make(rootLayout, R.string.camera_permission_rationale, Snackbar.LENGTH_LONG)
                    .setAction(android.R.string.ok,
                            view -> ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA)
                    ).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Camera permission has now been granted.");
                startReadingBarcode();
            } else {
                Log.i(TAG, "Camera permission was not granted.");
                // User denied permission and clicked "don't ask again"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showNoCameraPermissionSnackbar();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startReadingBarcode() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCameraActivity();
        } else {
            requestCameraPermission();
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivityForResult(intent, GET_BARCODE_REQUEST_CODE);
    }

    public void showNoCameraPermissionSnackbar() {
        Log.i(TAG, "Displaying snackbar with app settings for permission.");
        Snackbar.make(rootLayout, R.string.camera_permission_is_not_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_settings, v -> {
                    openApplicationSettings();

                    Toast.makeText(getApplicationContext(),
                            R.string.camera_permission_settings_explanation,
                            Toast.LENGTH_SHORT)
                            .show();
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        Log.i(TAG, "Open application settings for result.");
        startActivityForResult(appSettingsIntent, GET_PERMISSION_REQUEST_CODE);
    }
}
