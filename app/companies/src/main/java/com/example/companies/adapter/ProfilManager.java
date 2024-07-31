package com.example.companies.adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.common.model.FirmenModel;


public class ProfilManager {
    private static FirmenModel profil;


    public static FirmenModel getProfil(){
        return profil;
    }



}
