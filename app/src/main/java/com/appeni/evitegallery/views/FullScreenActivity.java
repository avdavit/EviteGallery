package com.appeni.evitegallery.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appeni.evitegallery.R;
import com.appeni.evitegallery.adapters.SliderAdapter;
import com.appeni.evitegallery.model.GalleryPhoto;
import com.google.gson.Gson;

/**
 * Created by Davit on 7/20/17.
 */

public class FullScreenActivity extends AppCompatActivity {

    public static String PICTURE_LIST = "PICTURE_LIST";
    public static String PICTURE_POSITION = "PICTURE_POSITION";

    private TextView nameTextView, numberTextView;
    private ImageView closeImageView;

    private Gson gson;
    private GalleryPhoto[] galleryPhotos;
    private int photoPosition;
    private int photosCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        initView();

        galleryPhotos = gson.fromJson(getIntent().getStringExtra(PICTURE_LIST), GalleryPhoto[].class);
        photosCount = galleryPhotos.length;
        photoPosition = getIntent().getIntExtra(PICTURE_POSITION, 0);

        numberTextView.setText("(" + (photoPosition + 1) + "/" + photosCount + ")");
        nameTextView.setText(galleryPhotos[photoPosition].getName());

        ViewPager pager = findViewById(R.id.photoSliderPager);
        pager.setAdapter(new SliderAdapter(getSupportFragmentManager(), galleryPhotos, photoPosition, this));
        pager.setCurrentItem(photoPosition);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                numberTextView.setText("(" + (position % photosCount + 1) + "/" + photosCount + ")");
                nameTextView.setText(galleryPhotos[position % photosCount].getName());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        gson = new Gson();
        nameTextView = findViewById(R.id.nameTextView);
        numberTextView = findViewById(R.id.numberTextView);
        closeImageView = findViewById(R.id.closeImageView);
    }


}