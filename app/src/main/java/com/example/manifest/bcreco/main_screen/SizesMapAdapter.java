package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.manifest.bcreco.R;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;

public class SizesMapAdapter extends BaseAdapter {

    private static final int HEIGHT_BUTTON_IN_DP = 36;

    private Context context;
    private Map<String, Boolean> sizesMap = new TreeMap<>();
    private String[] keys = sizesMap.keySet().toArray(new String[0]);

    public SizesMapAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sizesMap.size();
    }

    @Override
    public Boolean getItem(int position) {
        return sizesMap.get(keys[position]);
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
            sizeButton.setBackgroundResource(R.drawable.size_button_bg);
        } else {
            sizeButton = (Button) convertView;
        }
        sizeButton.setText(keys[position]);
        if (getItem(position)) {
            // TODO: if background oval disappear - v.getBackground().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN);
            sizeButton.setBackgroundColor(Color.parseColor("#BDBDBD"));
        }
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
