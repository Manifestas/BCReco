package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.manifest.bcreco.R;

import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;

public class SizesMapAdapter extends BaseAdapter {

    private static final int HEIGHT_BUTTON_IN_DP = 36;

    private Context context;
    private Map<String, Boolean> sizesMap = new TreeMap<>();

    public SizesMapAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sizesMap.size();
    }

    @Override
    public Map.Entry<String, Boolean> getItem(int position) {
        int size = getCount();
        int count = 0;
        for (Map.Entry<String, Boolean> entry : sizesMap.entrySet()) {
            if (count == position) {
                return entry;
            }
            count++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button sizeButton;
        if (convertView == null) {
            sizeButton = new Button(context);
            // convert dp to pixels
            int rowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    HEIGHT_BUTTON_IN_DP, context.getResources().getDisplayMetrics());
            sizeButton.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, rowHeight));
        } else {
            sizeButton = (Button) convertView;
        }
        // if its size in current store
        if (getItem(position).getValue()) {
            sizeButton.setBackgroundResource(R.drawable.size_button_bg_light_grey);
        } else {
            sizeButton.setBackgroundResource(R.drawable.size_button_bg);
        }
        sizeButton.setText(getItem(position).getKey());
        return sizeButton;
    }

    public void addAll(@NonNull Map<String, Boolean> map) {
        sizesMap.putAll(map);
        notifyDataSetChanged();
    }

    public void clear() {
        sizesMap.clear();
        notifyDataSetChanged();
    }
}
