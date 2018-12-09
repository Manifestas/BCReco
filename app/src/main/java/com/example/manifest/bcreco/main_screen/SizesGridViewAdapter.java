package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.manifest.bcreco.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SizesGridViewAdapter extends ArrayAdapter<String> {

    private static final int HEIGHT_BUTTON_IN_DP = 36;
    private Context context;

    public SizesGridViewAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @android.support.annotation.NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        sizeButton.setText(getItem(position));
        return sizeButton;
    }
}
