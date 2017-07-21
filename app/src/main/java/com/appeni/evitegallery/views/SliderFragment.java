package com.appeni.evitegallery.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.appeni.evitegallery.R;
import com.appeni.evitegallery.networkHelper.AppController;


/**
 * Created by Davit on 7/20/17.
 */

public class SliderFragment extends android.support.v4.app.Fragment {
    private ImageView imageView;

    private static String TAG= "SliderFragmentTag";
    public static String PICTURE_URL = "PICTURE_URL";
    public static String POSITION = "POSITION";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slider, container, false);

//        TextView tv = (TextView) v.findViewById(R.id.title);
//        tv.setText(getArguments().getInt(POSITION) + " name");

        imageView = (ImageView) v.findViewById(R.id.image);
        imageLoader(getArguments().getString(PICTURE_URL));

        return v;
    }


    private void imageLoader(String url) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();


        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }


    public static SliderFragment newInstance(int position, String url) {

        SliderFragment f = new SliderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SliderFragment.PICTURE_URL, url);
        bundle.putInt(SliderFragment.POSITION, position);

        f.setArguments(bundle);

        return f;
    }

}
