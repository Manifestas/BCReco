package com.example.manifest.bcreco.main_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manifest.bcreco.MainViewModel;
import com.example.manifest.bcreco.databinding.FragmentPhotoBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

    private static final String ARG_PHOTO_NUM = "photo_num";

    private static int photoIndex;

    private FragmentPhotoBinding binding;

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

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, List<String> imageUrls) {
        Picasso.get()
                .load(imageUrls.get(photoIndex))
                .into(imageView);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            photoIndex = arguments.getInt(ARG_PHOTO_NUM);
        }

        if (isAdded()) {
            MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            binding.setViewmodel(viewModel);

//            viewModel.getProduct().observe(this, product -> {
//                if (product != null && product.getInfoFromSite() != null) {
//                    InfoFromSite infoFromSite = product.getInfoFromSite();
//                    String imageUrl = infoFromSite.getImageUrls().get(photoNumber);
//                    Picasso.get()
//                            .load(imageUrl)
//                            .into(ivPhoto);
//                }
//            });
        }
    }
}
