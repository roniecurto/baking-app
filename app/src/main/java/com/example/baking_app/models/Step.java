package com.example.baking_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.baking_app.R;

public class Step implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL){
        this.id = id;
        this.shortDescription = shortDescription;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.description = description;
    }

    private Step(Parcel in){
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int i) {
            return new Step[i];
        }
    };

    //Getters
    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getShortDescription() {
        return shortDescription;
    }
    public String getThumbnailURL() {
        return thumbnailURL;
    }
    public String getVideoURL() {
        return videoURL;
    }
    public int getVideoIconPreview(){
        if((videoURL != "" && videoURL != null) || (thumbnailURL != null && thumbnailURL != "")) return R.drawable.video_play_active;

        return R.drawable.video_play_inactive;
    }

    //Setters
    public void setId(int id){ this.id = id; }
    public void setShortDescription(String shortDescription){ this.shortDescription = shortDescription; }
    public void setDescription(String description){ this.description = description; }
    public void setVideoURL(String videoURL){ this.videoURL = videoURL; }
    public void setThumbnailURL(String thumbnailURL){ this.thumbnailURL = thumbnailURL; }
}
