package com.fivos.thesuperherosquadmaker.ui.superHeroDetails;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.fivos.thesuperherosquadmaker.R;
import com.fivos.thesuperherosquadmaker.data.Character;
import com.fivos.thesuperherosquadmaker.data.ComicsResponse;
import com.fivos.thesuperherosquadmaker.databinding.SuperHeroDetailsFragmentBinding;
import com.fivos.thesuperherosquadmaker.util.UnitConverter;

import java.util.List;

public class SuperHeroDetailsFragment extends Fragment {

    private SuperHeroDetailsViewModel mViewModel;
    private int mId;
    private SuperHeroDetailsFragmentBinding mBinding;
    private int mRedColor;

    public SuperHeroDetailsFragment() {
        // Empty Constructor
    }

    public static SuperHeroDetailsFragment newInstance() {
        return new SuperHeroDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("hero_id")) {
            mId = getArguments().getInt("hero_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = SuperHeroDetailsFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new SuperHeroDetailsViewModelFactory(mId))
                .get(SuperHeroDetailsViewModel.class);
        mRedColor = ContextCompat.getColor(getActivity(), R.color.colorAccent);
        setupToolbar();
        subscribeToViewModel();
        mViewModel.start();
    }

    private void subscribeToViewModel() {
        mViewModel.getSuperHero().observe(getViewLifecycleOwner(), superHero -> setupSuperHeroDetails(superHero));

        mViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    showProgressDialog();
                } else {
                    hideProgressDialog();
                }
            }
        });

        mViewModel.getComics().observe(getViewLifecycleOwner(), new Observer<List<ComicsResponse.Data.Comics>>() {
            @Override
            public void onChanged(List<ComicsResponse.Data.Comics> comicsList) {
                if (comicsList != null) {
                    if (comicsList.size() > 0) {
                        ComicsResponse.Data.Comics comic1 = comicsList.get(0);
                        Glide.with(getActivity()).load(comic1.getThumbnail().getUrl()).into(mBinding.comic1IV);
                    }
                    if (comicsList.size() >= 1) {
                        ComicsResponse.Data.Comics comic2 = comicsList.get(1);
                        Glide.with(getActivity()).load(comic2.getThumbnail().getUrl()).into(mBinding.comic2IV);
                    }
                }

            }
        });
    }

    private void setupSuperHeroDetails(Character superHero) {
        if (superHero != null) {
            String url = superHero.getThumbnail().getPath() + "." + superHero.getThumbnail().getExtension();
            Glide.with(getActivity()).load(url).into(mBinding.avatarIV);
            mBinding.nameTV.setText(superHero.getName());
            mBinding.biographyTV.setText(superHero.getDescription());

            /*Character.Comics comics = superHero.getComics();
            List<Character.Comics.Item> comicsList = comics.getItems();*/

            styleButton(false);
        }
    }

    private void styleButton(boolean heroBelongsToSquad) {
        if (heroBelongsToSquad) {
            mBinding.button.setStrokeWidth((int) UnitConverter.convertDpToPixel(1, getActivity()));
            mBinding.button.setStrokeColor(ColorStateList.valueOf(mRedColor));
            mBinding.button.setText(R.string.button_fire);
        } else {
            mBinding.button.setBackgroundTintList(ColorStateList.valueOf(mRedColor));
            mBinding.button.setText(R.string.button_recruit);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    private void showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "progress_dialog");
    }

    private void hideProgressDialog() {
        DialogFragment dialog = (DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("progress_dialog");
        dialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}