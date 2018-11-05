package com.codeaamirkalimi.mealplanner.datamodel;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class GoogleAccountData implements Parcelable {

    String id;
    String displayName;
    String email;
    String familyName;
    String givenName;
    Uri photoUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    protected GoogleAccountData(Parcel in) {
        this.id = in.readString();
        this.displayName = in.readString();
        this.email = in.readString();
        this.familyName = in.readString();
        this.givenName = in.readString();
        this.photoUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.displayName);
        dest.writeString(this.email);
        dest.writeString(this.familyName);
        dest.writeString(this.givenName);
        dest.writeParcelable(this.photoUrl, flags);
    }

    public GoogleAccountData(String id, String displayName, String email, String familyName,
                             String givenName, Uri photoUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.familyName = familyName;
        this.givenName = givenName;
        this.photoUrl = photoUrl;
    }

    public static final Creator<GoogleAccountData> CREATOR = new Creator<GoogleAccountData>() {
        @Override
        public GoogleAccountData createFromParcel(Parcel source) {
            return new GoogleAccountData(source);
        }

        @Override
        public GoogleAccountData[] newArray(int size) {
            return new GoogleAccountData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
