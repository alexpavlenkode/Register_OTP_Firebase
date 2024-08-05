package com.example.common.model;

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
    private String userId;
    private int experience;
    private String webLink;
    private List<String> professions = new ArrayList<>();

    private List<String> keywordsProfession = new ArrayList<>();
    private int serviceRadius;
    private String contactInfo;
    private String aboutMe;
    private String legalRepresentation;
    private String imageUrl;
    private List<String> chats;
    private boolean contacktEnable;



    public FirmenModel(
            String phone,
            String companyName,
            String eMail,
            String userId,
            int experience,
            String webLink,
            List<String> professions,
            List<String> keywordsProfession,
            String contactInfo,
            String aboutMe,
            String legalRepresentation,
            int serviceRadius,
            String imageUrl,
            boolean contacktEnable,
            List<String> chats
    ) {
        this.phone = phone;
        this.companyName = companyName;
        this.eMail = eMail;
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
        this.chats = chats;
    }

    public FirmenModel() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected FirmenModel(Parcel in) {
        phone = in.readString();
        companyName = in.readString();
        eMail = in.readString();
        userId = in.readString();
        experience = in.readInt();
        webLink = in.readString();
        professions = Collections.singletonList(in.readString());
        keywordsProfession = Collections.singletonList(in.readString());
        serviceRadius = in.readInt();
        contactInfo = in.readString();
        aboutMe = in.readString();
        legalRepresentation = in.readString();
        imageUrl = in.readString();
        contacktEnable = in.readBoolean();

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
    public void setChats(List<String> chats) {
        this.chats = chats;
    }
    public List<String> getChats() {
        return chats;
    }

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
    public List<String> getProfessions() {
        return professions;
    }
    public void setProfession(List<String> professions) {
        this.professions = professions;
    }
    //Professions-Keywords
    public List<String> getKeywordsProfession() {
        return keywordsProfession;
    }
    public void setKeywordsProfession(List<String> keywordsProfession) {
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(companyName);
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
        }
    }
}
