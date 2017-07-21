package com.appeni.evitegallery.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appeni.evitegallery.R;
import com.appeni.evitegallery.adapters.GalleryAdapter;
import com.appeni.evitegallery.model.GalleryPhoto;
import com.appeni.evitegallery.networkHelper.AppController;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Davit on 7/20/17.
 */

public class MainActivity extends AppCompatActivity {

    private Gson gson;
    private RecyclerView mainRV;
    private LinearLayoutManager llm;
    private ProgressDialog pDialog;
    private String tag_json_obj = "json_obj_req";
    private String url = "http://application.am/evite/eviteapi.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        mainRV = findViewById(R.id.main_rv);
        getPhotosServer();
    }

    private void getPhotosServer() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        GalleryPhoto[] galleryPhotos = gson.fromJson(response.toString(), GalleryPhoto[].class);
                        drawRecycler(galleryPhotos);
                        pDialog.hide();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Web", "Error: " + error.getMessage());
                        pDialog.hide();
                    }
                });

        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    void drawRecycler(final GalleryPhoto[] galleryPhotos) {
        mainRV.setVisibility(View.VISIBLE);
        ArrayList<GalleryPhoto> packageList = new ArrayList<GalleryPhoto>(Arrays.asList(galleryPhotos));

        mainRV.setHasFixedSize(true);
        llm = new GridLayoutManager(this, 2);
        mainRV.setLayoutManager(llm);
        GalleryAdapter currentTaskAdapter = new GalleryAdapter(this, packageList);

        mainRV.setAdapter(currentTaskAdapter);
    }
}
