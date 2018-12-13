package com.example.manifest.bcreco.main_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manifest.bcreco.R;

import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SizesAdapter extends RecyclerView.Adapter<SizesAdapter.SizeViewHolder> {

    private Map<String, Integer> sizesData = new TreeMap<>();

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.size_list_item, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        int count = 0;
        for (Map.Entry<String, Integer> entry : sizesData.entrySet()) {
            if (count == position) {
                holder.storeNameTextView.setText(entry.getKey());
                holder.sizeQuantityTextView.setText(entry.getValue());
            }
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

    public class SizeViewHolder extends RecyclerView.ViewHolder {

        TextView storeNameTextView;
        TextView sizeQuantityTextView;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.tv_list_item_store_name);
            sizeQuantityTextView = itemView.findViewById(R.id.tv_list_item_size_quantity);
        }
    }

}
