package com.example.registerotp.fragments;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db;
    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }
    public void addProfessionDocument(String collectionName, String professionName, List<String> keywords) {
        Map<String, Object> professionData = new HashMap<>();
        professionData.put("keywords", keywords);

        db.collection(collectionName).document(professionName)
                .set(professionData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }

    public void addMultipleProfessions(String collectionName, Map<String, List<String>> professionsMap) {
        for (Map.Entry<String, List<String>> entry : professionsMap.entrySet()) {
            addProfessionDocument(collectionName, entry.getKey(), entry.getValue());
        }
    }

}
