package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

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
        TextView sizeText;
        if (convertView == null) {
            sizeText = new TextView(context);
            // convert dp to pixels
            int rowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    HEIGHT_BUTTON_IN_DP, context.getResources().getDisplayMetrics());
            sizeText.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, rowHeight));
            sizeText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        } else {
            sizeText = (TextView) convertView;
        }
        // if its size in current store
        if (getItem(position).getValue()) {
            sizeText.setBackgroundResource(R.drawable.size_button_bg_light_grey);
            // for understanding in the future which background was used
            sizeText.setTag(R.drawable.size_button_bg_light_grey);
        } else {
            sizeText.setBackgroundResource(R.drawable.size_button_bg);
            // for understanding in the future which background was used
            sizeText.setTag(R.drawable.size_button_bg);
        }
        sizeText.setText(getItem(position).getKey());
        sizeText.setTextColor(Color.BLACK);
        return sizeText;
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
