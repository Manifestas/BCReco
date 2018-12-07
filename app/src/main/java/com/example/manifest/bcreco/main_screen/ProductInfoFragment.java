package com.example.manifest.bcreco.main_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manifest.bcreco.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductInfoFragment extends Fragment {

    private static final String ARG_BARCODE = "arg_barcode";

    private TextView modelTextView;
    private TextView colorTextView;
    private TextView modelDescTextView;
    private TextView seasonTextView;
    private TextView priceTextView;

    public static Fragment newInstance(String barcode) {
        Bundle args = new Bundle();
        args.putString(ARG_BARCODE, barcode);
        ProductInfoFragment fragment = new ProductInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String barcode = getArguments().getString(ARG_BARCODE);
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

        return v;
    }
}
