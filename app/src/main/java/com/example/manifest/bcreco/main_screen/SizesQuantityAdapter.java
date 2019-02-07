package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.manifest.bcreco.databinding.SizeListItemBinding;

import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SizesQuantityAdapter extends RecyclerView.Adapter<SizesQuantityAdapter.SizeViewHolder> {

    private Map<String, Integer> sizesData = new TreeMap<>();

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        SizeListItemBinding binding = SizeListItemBinding.inflate(inflater, parent, false);
        return new SizeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        int count = 0;
        for (Map.Entry<String, Integer> entry : sizesData.entrySet()) {
            if (count == position) {
                holder.bind(entry);
            }
            count++;
        }
    }

    @Override
    public int getItemCount() {
        return sizesData.size();
    }

    public void setSizesData(Map<String, Integer> newSizeData) {
        sizesData.clear();
        sizesData.putAll(newSizeData);
        notifyDataSetChanged();
    }


    class SizeViewHolder extends RecyclerView.ViewHolder {

        private SizeListItemBinding binding;

        SizeViewHolder(SizeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Map.Entry<String, Integer> sizeQuantity) {
            binding.setEntry(sizeQuantity);
            binding.executePendingBindings();
        }
    }

}
