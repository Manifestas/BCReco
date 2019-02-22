package com.example.manifest.bcreco.main_screen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.manifest.bcreco.MainViewModel;
import com.example.manifest.bcreco.R;
import com.example.manifest.bcreco.databinding.FragmentInfoBinding;
import com.example.manifest.bcreco.models.Product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductInfoFragment extends Fragment {

    private GridView sizesGridView;
    private MainViewModel viewModel;
    private SizesMapAdapter sizeAdapter;
    private RecyclerView sizeQuantityRecyclerView;
    private SizesQuantityAdapter sizesQuantityAdapter;

    private int previousSelectedSizePosition = -1;
    private int previousSelectedSizeBackground = -1;
    private FragmentInfoBinding binding;

    public static Fragment newInstance() {
        return new ProductInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();
        initSizesGridView(v);
        initRecyclerView(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            binding.setViewmodel(viewModel);
            viewModel.getProduct().observe(this, product -> {
                if (product != null) {
                    sizeAdapter.clear();
                    sizeAdapter.addAll(product.getAvailableSizes());
                    sizesQuantityAdapter.clear();
                }
            });
        }
    }

    private void initSizesGridView(View v) {
        sizeAdapter = new SizesMapAdapter(getActivity());
        sizesGridView = v.findViewById(R.id.gv_sizes);
        sizesGridView.setAdapter(sizeAdapter);
        sizesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = viewModel.getProduct().getValue();
                if (product != null) {
                    TextView clickedView = (TextView) view;
                    String clickedSize = clickedView.getText().toString();
                    if (previousSelectedSizePosition != -1) {
                        TextView previousTextView = (TextView) sizesGridView.getChildAt(previousSelectedSizePosition);
                        previousTextView.setBackgroundResource(previousSelectedSizeBackground);
                        previousTextView.setTextColor(Color.BLACK);
                    }
                    clickedView.setBackgroundResource(R.drawable.size_button_bg_black);
                    clickedView.setTextColor(Color.WHITE);
                    sizesQuantityAdapter.setSizesData(product.getSizeQuantityForAllStores(clickedSize));
                    previousSelectedSizePosition = position;
                    // in adapter current background color saved as a Tag
                    previousSelectedSizeBackground = (int) clickedView.getTag();
                }
            }
        });
    }

    private void initRecyclerView(View v) {
        sizeQuantityRecyclerView = v.findViewById(R.id.rv_quantity_in_store);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        sizeQuantityRecyclerView.setLayoutManager(layoutManager);
        sizeQuantityRecyclerView.setHasFixedSize(true);
        sizesQuantityAdapter = new SizesQuantityAdapter();
        sizeQuantityRecyclerView.setAdapter(sizesQuantityAdapter);
    }
}
