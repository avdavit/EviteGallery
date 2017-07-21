package com.appeni.evitegallery.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.appeni.evitegallery.R;
import com.appeni.evitegallery.model.GalleryPhoto;
import com.appeni.evitegallery.networkHelper.AppController;
import com.appeni.evitegallery.views.FullScreenActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Davit on 7/20/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<GalleryPhoto> galleryPhotos;
    private Context context;
    private LayoutInflater inflater;
    private Gson gson;
    private String TAG = GalleryAdapter.class.getSimpleName();


    public GalleryAdapter(Context context, ArrayList<GalleryPhoto> galleryPhotos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.galleryPhotos = galleryPhotos;
        gson = new Gson();
    }


    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_gallery_image, parent, false);

        GalleryViewHolder viewHolder = new GalleryViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        imageLoader(galleryPhotos.get(position).getPhoto(), holder.mainPhoto);
        holder.photoTitle.setText(galleryPhotos.get(position).getName());

        holder.listItem.setTag(position);
        holder.listItem.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            View sharedView = (ImageView) v.findViewById(R.id.mainPhoto);
            String transitionName = context.getString(R.string.transiction_photo);

            Intent taskIntent = new Intent(context, FullScreenActivity.class);
            taskIntent.putExtra(FullScreenActivity.PICTURE_LIST, gson.toJson(galleryPhotos));
            taskIntent.putExtra(FullScreenActivity.PICTURE_POSITION, position);

            ActivityOptions transitionActivityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, sharedView, transitionName);
                context.startActivity(taskIntent, transitionActivityOptions.toBundle());
            } else {
                context.startActivity(taskIntent);
            }
        }
    };


    @Override
    public int getItemCount() {
        return galleryPhotos.size();
    }


    private void imageLoader(String url, final ImageView imageView) {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        TextView photoTitle;
        ImageView mainPhoto;
        RelativeLayout listItem;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            mainPhoto = (ImageView) itemView.findViewById(R.id.mainPhoto);
            photoTitle = (TextView) itemView.findViewById(R.id.photoTitle);
            listItem = (RelativeLayout) itemView.findViewById(R.id.listItem);

        }
    }


}