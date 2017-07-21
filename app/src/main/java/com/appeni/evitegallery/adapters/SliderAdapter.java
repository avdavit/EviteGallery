package com.appeni.evitegallery.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appeni.evitegallery.views.SliderFragment;
import com.appeni.evitegallery.model.GalleryPhoto;

/**
 * Created by Davit on 7/20/17.
 */

public class SliderAdapter extends FragmentPagerAdapter {

    GalleryPhoto[] galleryPhotos;
    int photoPosition = -1;
    Context context;
    SliderFragment currentFragment = null;

    public SliderAdapter(FragmentManager fm, GalleryPhoto[] galleryPhotos, int photoPosition, Context context) {
        super(fm);
        this.galleryPhotos = galleryPhotos;
        this.context = context;
        this.photoPosition = photoPosition;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int pos) {

        pos %= galleryPhotos.length;

        SliderFragment currentFragment = SliderFragment.newInstance(pos,galleryPhotos[pos].getPhoto());
        return currentFragment;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}