package com.example.companies.repository;

import android.util.Log;

import com.example.common.model.FirmenModel;
import com.example.common.utils.FirestoreHelper;
import com.example.companies.SharedViewModel;
import com.example.companies.adapter.Auftraggeber;
import com.example.companies.ui.chat.ChatroomModel;
import com.example.companies.ui.chat.NachrichtenModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/*
    TODO:
        1. Получаем все чаты компании
 */
public class NachrichtenRepository {
    private static final String TAG = "NachrichtenRepository";
    private SharedViewModel sharedViewModel;
    private FirestoreHelper firestoreHelper;

    public NachrichtenRepository(FirestoreHelper firestoreHelper, SharedViewModel sharedViewModel){
        this.firestoreHelper = firestoreHelper;
        this.sharedViewModel = sharedViewModel;
    }

    public void getAllChats(String companyId){
        firestoreHelper.getChatsFromCompany(companyId, new FirestoreHelper.OnChatsFetchedListener() {
            @Override
            public void onChatsFetched(List<String> companyChats) {
                FirmenModel currentProfile = sharedViewModel.getUserProfile().getValue();
                if (currentProfile != null) {
                    List<String> currentChats = currentProfile.getChats();
                    if (currentChats == null) {
                        currentChats = new ArrayList<>();
                    }
                    // Если чаты компании изменились, обновите профиль
                    if (!currentChats.equals(companyChats)) {
                        currentProfile.setChats(companyChats);
                        sharedViewModel.setUserProfile(currentProfile);
                    }
                    // Запросите данные для каждого чата
                    getChatroomModels(companyChats);
                }
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error fetching chats from company", e);
            }
        });

    }

    public void getChatroomModels(List<String> chatIds) {
        // Создайте пустой список для хранения NachrichtenModel
        List<NachrichtenModel> nachrichtenModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String chatId : chatIds) {
            db.collection("chats").document(chatId).get()
                    .addOnSuccessListener(chatDocumentSnapshot -> {
                        if (chatDocumentSnapshot.exists()) {
                            ChatroomModel chatroomModel = chatDocumentSnapshot.toObject(ChatroomModel.class);
                            if (chatroomModel == null) {
                                Log.e(TAG, "Failed to parse ChatroomModel for chatId: " + chatId);
                                return;
                            }

                            // Получаем информацию о пользователе
                            db.collection("user").document(chatroomModel.getUserId()).get()
                                    .addOnSuccessListener(userDocumentSnapshot -> {
                                        if (userDocumentSnapshot.exists()) {
                                            Auftraggeber auftraggeber = userDocumentSnapshot.toObject(Auftraggeber.class);
                                            if (auftraggeber == null) {
                                                Log.e(TAG, "Failed to parse Auftraggeber for userId: " + chatroomModel.getUserId());
                                                return;
                                            }

                                            // Создаем модель сообщений с данными пользователя и чата
                                            NachrichtenModel nachrichtenModel = new NachrichtenModel();
                                            nachrichtenModel.setChatroomId(chatroomModel.getChatroomId());
                                            nachrichtenModel.setUserId(chatroomModel.getUserId());
                                            nachrichtenModel.setTicketId(chatroomModel.getTicketId());
                                            nachrichtenModel.setUserName(auftraggeber.getUserName());
                                            nachrichtenModel.setUserImage(auftraggeber.getUserImage());

                                            // Получаем последнее сообщение из чата
                                            db.collection("chats").document(chatId).collection("messages")
                                                    .orderBy("timestamp", Query.Direction.DESCENDING)
                                                    .limit(1)
                                                    .get()
                                                    .addOnSuccessListener(messagesQuerySnapshot -> {
                                                        if (!messagesQuerySnapshot.isEmpty()) {
                                                            DocumentSnapshot lastMessageSnapshot = messagesQuerySnapshot.getDocuments().get(0);
                                                            String lastMessage = lastMessageSnapshot.getString("message");
                                                            Timestamp lastMessageTimestamp = lastMessageSnapshot.getTimestamp("timestamp");

                                                            nachrichtenModel.setLastMessage(lastMessage);
                                                            nachrichtenModel.setMessageTimestamp(lastMessageTimestamp);
                                                            nachrichtenModel.setTicketStatus(0);

                                                            // Добавляем модель сообщений в список
                                                            nachrichtenModels.add(nachrichtenModel);

                                                            // Обновляем ViewModel данными
                                                            sharedViewModel.setNachrichtenModel(nachrichtenModels);

                                                            Log.e(TAG, "Chat Rooms id " + chatroomModel.getChatroomId());
                                                            Log.e(TAG, "Last Message: " + lastMessage);
                                                            Log.e(TAG, "Last Message Timestamp: " + lastMessageTimestamp.toDate().toString());
                                                        } else {
                                                            Log.e(TAG, "No messages found in chatId: " + chatId);
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching last message for chatId: " + chatId, e));
                                        } else {
                                            Log.e(TAG, "User not found for userId: " + chatroomModel.getUserId());
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching user data for chatId: " + chatId, e));
                        } else {
                            Log.e(TAG, "Chat not found for chatId: " + chatId);
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching chat data for chatId: " + chatId, e));
        }
    }

    public void getChatroomModel(String chatId) {
        // Создайте пустой список для хранения NachrichtenModel
        List<NachrichtenModel> nachrichtenModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Сначала получаем информацию о чате
        db.collection("chats").document(chatId).get()
                .addOnSuccessListener(chatDocumentSnapshot -> {
                    if (chatDocumentSnapshot.exists()) {
                        ChatroomModel chatroomModel = chatDocumentSnapshot.toObject(ChatroomModel.class);
                        if (chatroomModel == null) {
                            Log.e(TAG, "Failed to parse ChatroomModel");
                            return;
                        }
                        sharedViewModel.setChatroomModel(chatroomModel);
                        // Теперь получаем информацию о пользователе, используя userId из chatroomModel
                        db.collection("user").document(chatroomModel.getUserId()).get()
                                .addOnSuccessListener(userDocumentSnapshot -> {
                                    if (userDocumentSnapshot.exists()) {
                                        Auftraggeber auftraggeber = userDocumentSnapshot.toObject(Auftraggeber.class);
                                        if (auftraggeber == null) {
                                            Log.e(TAG, "Failed to parse Auftraggeber");
                                            return;
                                        }
                                        // Создаем модель сообщений с данными пользователя и чата
                                        NachrichtenModel nachrichtenModel = new NachrichtenModel();
                                        nachrichtenModel.setChatroomId(chatroomModel.getChatroomId());
                                        nachrichtenModel.setUserId(chatroomModel.getUserId());
                                        nachrichtenModel.setUserName(auftraggeber.getUserName());
                                        nachrichtenModel.setUserImage(auftraggeber.getUserImage());
                                        // Получаем последнее сообщение из чата
                                        db.collection("chats").document(chatId).collection("messages")
                                                .orderBy("timestamp", Query.Direction.DESCENDING)
                                                .limit(1)
                                                .get()
                                                .addOnSuccessListener(messagesQuerySnapshot -> {
                                                    if (!messagesQuerySnapshot.isEmpty()) {
                                                        DocumentSnapshot lastMessageSnapshot = messagesQuerySnapshot.getDocuments().get(0);
                                                        String lastMessage = lastMessageSnapshot.getString("messageText");
                                                        Timestamp lastMessageTimestamp = lastMessageSnapshot.getTimestamp("timestamp");

                                                        nachrichtenModel.setLastMessage(lastMessage);
                                                        nachrichtenModel.setMessageTimestamp(lastMessageTimestamp);
                                                        // Добавляем модель сообщений в список
                                                        nachrichtenModels.add(nachrichtenModel);
                                                        // Обновляем ViewModel данными
                                                        sharedViewModel.setNachrichtenModel(nachrichtenModels);

                                                        Log.e(TAG, "Chat Rooms id " + sharedViewModel.getChatroomModel().getValue().getChatroomId());
                                                        Log.e(TAG, "Last Message: " + lastMessage);
                                                        Log.e(TAG, "Last Message Timestamp: " + lastMessageTimestamp.toDate().toString());
                                                    } else {
                                                        Log.e(TAG, "No messages found in chatId: " + chatId);
                                                    }
                                                })
                                                .addOnFailureListener(e -> Log.e(TAG, "Error fetching last message", e));
                                    } else {
                                        Log.e(TAG, "User not found for userId: " + chatroomModel.getUserId());
                                    }
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error fetching user data", e));
                    } else {
                        Log.e(TAG, "Chat not found for chatId: " + chatId);
                    }
                });
            }
}
