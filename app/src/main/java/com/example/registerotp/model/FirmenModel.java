package com.example.registerotp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirmenModel implements Parcelable {
    private String phone;
    private String companyName;
    private String eMail;
    private Timestamp createdTimestamp;
    private String userId;
    private int experience;
    private String webLink;
    private Set<String> professions = new HashSet<>();

    private Set<String> keywordsProfession = new HashSet<>();
    private int serviceRadius;
    private String contactInfo;
    private String aboutMe;
    private String legalRepresentation;
    private String imageUrl;
    private boolean contacktEnable;
    private boolean regComplet;

    public FirmenModel(
            String phone,
            String companyName,
            String eMail,
            Timestamp createdTimestamp,
            String userId,
            int experience,
            String webLink,
            Set<String> professions,
            Set<String> keywordsProfession,
            String contactInfo,
            String aboutMe,
            String legalRepresentation,
            int serviceRadius,
            String imageUrl,
            boolean contacktEnable,
            boolean regComplet
    ) {
        this.phone = phone;
        this.companyName = companyName;
        this.eMail = eMail;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.experience = experience;
        this.webLink = webLink;
        this.professions = professions;
        this.keywordsProfession = keywordsProfession;
        this.contactInfo = contactInfo;
        this.aboutMe = aboutMe;
        this.legalRepresentation = legalRepresentation;
        this.serviceRadius = serviceRadius;
        this.imageUrl = imageUrl;
        this.contacktEnable = contacktEnable;
        this.regComplet = regComplet;
    }

    public FirmenModel() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected FirmenModel(Parcel in) {
        phone = in.readString();
        companyName = in.readString();
        eMail = in.readString();
        createdTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
        experience = in.readInt();
        webLink = in.readString();
        professions = Collections.singleton(in.readString());
        keywordsProfession = Collections.singleton(in.readString());
        serviceRadius = in.readInt();
        contactInfo = in.readString();
        aboutMe = in.readString();
        legalRepresentation = in.readString();
        imageUrl = in.readString();
        contacktEnable = in.readBoolean();
        regComplet = in.readBoolean();

    }

    public static final Creator<FirmenModel> CREATOR = new Creator<FirmenModel>() {
        @Override
        public FirmenModel createFromParcel(Parcel in) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return new FirmenModel(in);
            }
            return null;
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
    //Professions
    public Set<String> getProfessions() {
        return professions;
    }
    public void setProfession(Set<String> professions) {
        this.professions = professions;
    }
    //Professions-Keywords
    public Set<String> getKeywordsProfession() {
        return keywordsProfession;
    }
    public void setKeywordsProfession(Set<String> keywordsProfession) {
        this.keywordsProfession = keywordsProfession;
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

    public boolean getcontacktEnable(){return contacktEnable;}
    public void setcontacktEnable(boolean contacktEnable){this.contacktEnable = contacktEnable;}

    public boolean getRegComplet() {
        return regComplet;
    }

    public void setRegComplet(boolean regComplet) {
        this.regComplet = regComplet;
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
        dest.writeList((List) professions);
        dest.writeInt(serviceRadius);
        dest.writeString(contactInfo);
        dest.writeString(aboutMe);
        dest.writeString(legalRepresentation);
        dest.writeString(imageUrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(contacktEnable);
            dest.writeBoolean(regComplet);
        }
    }
}
