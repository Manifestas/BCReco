package com.example.manifest.bcreco.main_screen;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.manifest.bcreco.MainViewModel;
import com.example.manifest.bcreco.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProductInfoFragment extends Fragment {

    private TextView modelTextView;
    private TextView colorTextView;
    private TextView modelDescTextView;
    private TextView seasonTextView;
    private TextView priceTextView;
    private TextView maxPriceTextView;
    private GridView sizesGridView;
    private MainViewModel viewModel;

    public static Fragment newInstance() {
        return new ProductInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        modelTextView = v.findViewById(R.id.tv_model);
        colorTextView = v.findViewById(R.id.tv_color);
        modelDescTextView = v.findViewById(R.id.tv_model_desc);
        seasonTextView = v.findViewById(R.id.tv_season);
        priceTextView = v.findViewById(R.id.tv_price);
        maxPriceTextView = v.findViewById(R.id.tv_max_price);
        sizesGridView = v.findViewById(R.id.gv_sizes);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getProduct().observe(this, product -> {
            modelTextView.setText(product.getModel());
            colorTextView.setText(product.getColor());
            modelDescTextView.setText(product.getModelDesc());
            seasonTextView.setText(product.getPrice());
            priceTextView.setText(product.getPrice());
            maxPriceTextView.setText(product.getInfoFromSite().getMaxPrice());
            // make text crossed
            maxPriceTextView.setPaintFlags(maxPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        });
    }
}
