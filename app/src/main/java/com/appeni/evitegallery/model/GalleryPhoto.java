
package com.appeni.evitegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryPhoto {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
