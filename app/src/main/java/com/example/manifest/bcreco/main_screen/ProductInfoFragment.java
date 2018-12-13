package com.example.manifest.bcreco.main_screen;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.manifest.bcreco.MainViewModel;
import com.example.manifest.bcreco.R;
import com.example.manifest.bcreco.models.Product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductInfoFragment extends Fragment {

    private TextView modelTextView;
    private TextView colorTextView;
    private TextView modelDescTextView;
    private TextView seasonTextView;
    private TextView priceTextView;
    private TextView maxPriceTextView;
    private GridView sizesGridView;
    private MainViewModel viewModel;
    private SizesMapAdapter sizeAdapter;
    private RecyclerView sizeQuantityRecyclerView;
    private SizesQuantityAdapter sizesQuantityAdapter;

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
        sizeAdapter = new SizesMapAdapter(getActivity());
        sizesGridView.setAdapter(sizeAdapter);
        sizesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = viewModel.getProduct().getValue();
                if (product != null) {
                    TextView clickedView = (TextView) view;
                    String clickedSize = clickedView.getText().toString();
                    sizesQuantityAdapter.setSizesData(product.getSizeQuantityForAllStores(clickedSize));
                }
            }
        });

        sizeQuantityRecyclerView = v.findViewById(R.id.rv_quantity_in_store);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        sizeQuantityRecyclerView.setLayoutManager(layoutManager);
        sizeQuantityRecyclerView.setHasFixedSize(true);
        sizesQuantityAdapter = new SizesQuantityAdapter();
        sizeQuantityRecyclerView.setAdapter(sizesQuantityAdapter);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            viewModel.getProduct().observe(this, product -> {
                modelTextView.setText(product.getModel());
                colorTextView.setText(product.getColor());
                modelDescTextView.setText(product.getModelDesc());
                seasonTextView.setText(product.getSeason());
                String price = product.getPrice() + "\u20BD"; // add ruble sign
                priceTextView.setText(price);
                if (product.getInfoFromSite() != null) {
                    String maxPrice = product.getInfoFromSite().getMaxPrice() + "\u20BD"; //add ruble sign
                    maxPriceTextView.setText(maxPrice);
                    // make text crossed
                    maxPriceTextView.setPaintFlags(maxPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

                sizeAdapter.clear();
                sizeAdapter.addAll(product.getAvailableSizes());
            });
        }
    }
}
