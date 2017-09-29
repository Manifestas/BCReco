package com.example.manifest.bcreco;

import android.app.Activity;
import android.os.Bundle;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;


public class MainActivity extends Activity {

    private ImageScanner scanner;

    static {
        System.loadLibrary("iconv");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanner = new ImageScanner();
        // dont know why this numbers
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
    }
}
