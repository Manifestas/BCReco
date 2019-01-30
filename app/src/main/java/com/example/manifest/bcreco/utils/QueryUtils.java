package com.example.manifest.bcreco.utils;

import android.util.Log;

import com.example.manifest.bcreco.models.InfoFromSite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    private static final String REQUEST_URL = "https://www.parad-shoes.ru/api/products?";
    private static final String MODEL_TAG = "tags%5B%5D=";
    private static final String USER_AGENT = "Mozilla/5.0";

    private static String article;

    public static InfoFromSite fetchInfoFromSite(String model) {
        article = model;
        URL url = createUrl(model);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem with request: " + e);
        }
        return extractProductInfoFromJson(jsonResponse);
    }

    private static InfoFromSite extractProductInfoFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }
        String maxPrice = "";
        List<String> imageUrls = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray products = root.getJSONArray("products");
            //if array is empty - return null;
            if (products.isNull(0)) {
                Log.w(TAG, article + " not found");
                return null;
            }
            for (int i = 0; i < products.length(); ++i) {
                JSONObject model = products.getJSONObject(i);
                JSONObject properties = model.getJSONObject("PROPERTIES");
                JSONObject articul = properties.getJSONObject("ARTICUL");
                String propertiesModel = articul.getString("value");
                // remove escape symbol
                propertiesModel = propertiesModel.replace("\\", "");
                if (article.equals(propertiesModel)) {
                    // if key "IS_SALE_PRICE" false - return null
                    if (model.getBoolean("IS_SALE_PRICE")) {
                        maxPrice = model.getString("PRICE");
                    }
                    JSONArray extendedImages = model.getJSONArray("ADDITIONAL_IMAGES_EXTENDED");
                    for (int j = 0; j < extendedImages.length(); j++) {
                        JSONObject imageObject = extendedImages.getJSONObject(j);
                        String imageUrl = imageObject.getString("medium2");
                        imageUrls.add(imageUrl);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problems with parsing JSON");
        }
        return new InfoFromSite(maxPrice, imageUrls);
    }

    private static URL createUrl(String query) {
        URL url = null;
        URI uri = URI.create(REQUEST_URL + MODEL_TAG + query);
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create URL " + e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if url is null - return early
        if (url == null) {
            return jsonResponse;
        }
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.i(TAG, "Response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't fetch JSON data from server. " + e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }
}
