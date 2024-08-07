package com.example.common.utils;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

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

    public void addTicket(String adresse,
                          Timestamp date,
                          String description,
                          GeoPoint geopoint,
                          String profession,
                          String short_description,
                          int status,
                          String title,
                          int urgency,
                          String userId,
                          int views) {
        // Подготовка данных для нового документа
        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("adresse", adresse);
        ticketData.put("date", date);
        ticketData.put("description", description);
        ticketData.put("geopoint", geopoint);
        ticketData.put("profession", profession);
        ticketData.put("short_description", short_description);
        ticketData.put("status", status);
        ticketData.put("title", title);
        ticketData.put("urgency", urgency);
        ticketData.put("userId", userId);
        ticketData.put("views", views);

        // Добавление документа в коллекцию 'tickets' с авто-сгенерированным ID
        db.collection("tickets")
                .add(ticketData)
                .addOnSuccessListener(documentReference -> {
                    // Успешное добавление документа
                    String documentId = documentReference.getId();
                    System.out.println("DocumentSnapshot added with ID: " + documentId);
                })
                .addOnFailureListener(e -> {
                    // Ошибка при добавлении документа
                    System.err.println("Error adding document: " + e.getMessage());
                });
    }



    public void sendMessageToChat(String chatId, Object message, OnMessageSentListener listener) {
        // Получаем ссылку на коллекцию сообщений в чате
        CollectionReference chatMessagesRef = db.collection("chats")
                .document(chatId)
                .collection("messages");

        // Добавляем сообщение в коллекцию
        chatMessagesRef.add(message)
                .addOnSuccessListener(documentReference -> {
                    // Сообщение успешно добавлено
                    if (listener != null) {
                        listener.onMessageSent();
                    }
                })
                .addOnFailureListener(e -> {
                    // Ошибка при добавлении сообщения
                    if (listener != null) {
                        listener.onError(e);
                    }
                });
    }

    // Метод для получения чатов из тикета
    public void getChatsFromTicket(String ticketId, OnChatsFetchedListener listener) {
        db.collection("tickets").document(ticketId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            List<String> chatIds = (List<String>) document.get("chats");
                            listener.onChatsFetched(chatIds);
                        } else {
                            listener.onError(new Exception("No chats found for ticket"));
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }
    // Метод для получения чатов у компании
    public void getChatsFromCompany(String companyId, OnChatsFetchedListener listener) {
        db.collection("companies").document(companyId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            List<String> chatIds = (List<String>) document.get("chats");
                            listener.onChatsFetched(chatIds);
                        } else {
                            listener.onError(new Exception("No chats found for company"));
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }
    // Метод для обновления чатов тикета
    public void updateTicketChats(String ticketId, List<String> updatedChats, OnUpdateCompleteListener listener) {
        db.collection("tickets").document(ticketId).update("chats", updatedChats)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onUpdateComplete();
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public void createChat(String ticketId, String userId, String companyId,boolean isSystem, OnChatCreatedListener listener) {
        // Генерируем новый идентификатор для чата
        DocumentReference newChatRef = db.collection("chats").document();
        String chatId = newChatRef.getId();

        // Создаем данные для нового чата
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("chatroomId", chatId);
        chatData.put("ticketId", ticketId);
        chatData.put("userId", userId);
        chatData.put("companyId", companyId);
        chatData.put("timestamp", FieldValue.serverTimestamp());
        chatData.put("isSystemMessage", isSystem);
        // Создаем новый чат в Firestore
        newChatRef.set(chatData)
                .addOnSuccessListener(aVoid -> {
                    // Чат успешно создан
                    listener.onChatCreated(chatId);
                    // Теперь добавляем этот чат в список чатов компании
                    addChatToCompany(companyId, chatId);
                    // Так же добавляем этот чат в список чатов Юзера
                    //TODO: !!!!
                    //Так же добавляем этот чат в список чатов Tiket!
                    addChatToTicket(chatId,ticketId);
                })
                .addOnFailureListener(e -> {
                    // Ошибка при создании чата
                    listener.onError(e);
                });
    }
    private void addChatToCompany(String companyId, String chatId) {
        DocumentReference companyRef = db.collection("companies").document(companyId);
        companyRef.update("chats", FieldValue.arrayUnion(chatId))
                .addOnSuccessListener(aVoid -> {
                    // Чат успешно добавлен в компанию
                    Log.d("FirestoreHelper", "Chat added to company successfully");
                })
                .addOnFailureListener(e -> {
                    // Обработка ошибки
                    Log.e("FirestoreHelper", "Error adding chat to company", e);
                });
    }
    private void addChatToTicket(String chatId, String ticketId) {
        DocumentReference companyRef = db.collection("tickets").document(ticketId);
        companyRef.update("chats", FieldValue.arrayUnion(chatId))
                .addOnSuccessListener(aVoid -> {
                    // Чат успешно добавлен в компанию
                    Log.d("FirestoreHelper", "Chat added to Ticket successfully");
                })
                .addOnFailureListener(e -> {
                    // Обработка ошибки
                    Log.e("FirestoreHelper", "Error adding chat to Ticket", e);
                });
    }

    // Интерфейс для получения ChatroomModel
    public interface OnChatroomModelCallback {
        void onSuccess(Object chatroomModel);
        void onFailure(Exception e);
    }


    public interface OnChatCreatedListener {
        void onChatCreated(String newChatId);
        void onError(Exception e);
    }
    // Интерфейс для обработки результата обновления чатов
    public interface OnUpdateCompleteListener {
        void onUpdateComplete();
        void onError(Exception e);
    }

    // Интерфейс для обработки результата получения чатов
    public interface OnChatsFetchedListener {
        void onChatsFetched(List<String> chatIds);
        void onError(Exception e);
    }
    public interface OnMessageSentListener {
        void onMessageSent();
        void onError(Exception e);
    }

    public void addMultipleProfessions(String collectionName, Map<String, List<String>> professionsMap) {
        for (Map.Entry<String, List<String>> entry : professionsMap.entrySet()) {
            addProfessionDocument(collectionName, entry.getKey(), entry.getValue());
        }
    }

}
