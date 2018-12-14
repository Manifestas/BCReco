package com.example.manifest.bcreco.main_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manifest.bcreco.MainViewModel;
import com.example.manifest.bcreco.R;
import com.example.manifest.bcreco.models.InfoFromSite;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

    private static final String ARG_PHOTO_NUM = "photo_num";

    private ImageView ivPhoto;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photoNum number photo for showing
     * @return A new instance of fragment PhotoFragment.
     */
    public static PhotoFragment newInstance(int photoNum) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_NUM, photoNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        ivPhoto = v.findViewById(R.id.iv_photo);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int photoNumber = getArguments().getInt(ARG_PHOTO_NUM);
        if (isAdded()) {
            MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            viewModel.getProduct().observe(this, product -> {
                InfoFromSite infoFromSite = product.getInfoFromSite();
                if (infoFromSite != null) {
                    String imageUrl = infoFromSite.getImageUrls().get(photoNumber);
                    Picasso.get()
                            .load(imageUrl)
                            .into(ivPhoto);
                }
            });
        }
    }
}
