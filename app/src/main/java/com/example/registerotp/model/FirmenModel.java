package com.example.registerotp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.List;

public class FirmenModel implements Parcelable {
    private String phone;
    private String companyName;
    private Timestamp createdTimestamp;
    private String userId;
    private int experience;
    private String webLink;
    private String serviceCategory;
    private int serviceRadius;
    private String contactInfo;
    private String aboutMe;
    private String legalRepresentation;

    private String imageUrl;

    public FirmenModel(
            String phone,
            String companyName,
            Timestamp createdTimestamp,
            String userId,
            int experience,
            String webLink,
            String serviceCategory,
            String contactInfo,
            String aboutMe,
            String legalRepresentation,
            int serviceRadius,
            String imageUrl
    ) {
        this.phone = phone;
        this.companyName = companyName;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.experience = experience;
        this.webLink = webLink;
        this.serviceCategory = serviceCategory;
        this.contactInfo = contactInfo;
        this.aboutMe = aboutMe;
        this.legalRepresentation = legalRepresentation;
        this.serviceRadius = serviceRadius;
        this.imageUrl = imageUrl;
    }

    public FirmenModel() {
    }

    protected FirmenModel(Parcel in) {
        phone = in.readString();
        companyName = in.readString();
        createdTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
        experience = in.readInt();
        webLink = in.readString();
        serviceCategory = in.readString();
        serviceRadius = in.readInt();
        contactInfo = in.readString();
        aboutMe = in.readString();
        legalRepresentation = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<FirmenModel> CREATOR = new Creator<FirmenModel>() {
        @Override
        public FirmenModel createFromParcel(Parcel in) {
            return new FirmenModel(in);
        }

        @Override
        public FirmenModel[] newArray(int size) {
            return new FirmenModel[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getLegalRepresentation() {
        return legalRepresentation;
    }

    public void setLegalRepresentation(String legalRepresentation) {
        this.legalRepresentation = legalRepresentation;
    }

    public int getServiceRadius() {
        return serviceRadius;
    }

    public void setServiceRadius(int serviceRadius) {
        this.serviceRadius = serviceRadius;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(companyName);
        dest.writeParcelable(createdTimestamp, flags);
        dest.writeString(userId);
        dest.writeInt(experience);
        dest.writeString(webLink);
        dest.writeString(serviceCategory);
        dest.writeInt(serviceRadius);
        dest.writeString(contactInfo);
        dest.writeString(aboutMe);
        dest.writeString(legalRepresentation);
        dest.writeString(imageUrl);
    }
}
