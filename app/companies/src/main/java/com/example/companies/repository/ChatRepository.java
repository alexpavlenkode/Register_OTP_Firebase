package com.example.companies.repository;


/*TODO:
*   1. Обновляем информацию о чатах/Тикетах
*   2. Получаем данные из chat у компании
*   3. Данные из чатов у Заказчкиа
*   3. Если есть общие, открываем чат
*   4. Если общих нет, создаём новый чат
*
* */

import android.util.Log;

import com.example.common.model.FirmenModel;
import com.example.common.utils.FirestoreHelper;
import com.example.companies.R;
import com.example.companies.SharedViewModel;
import com.example.companies.ui.chat.ChatFragment;
import com.example.companies.ui.chat.ChatMessageModel;
import com.example.companies.ui.chat.ChatroomModel;
import com.example.companies.ui.chat.PendingChatCreation;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRepository{
    private SharedViewModel sharedViewModel;
    private FirestoreHelper firestoreHelper;
    private ChatFragment chatFragment;
    public ChatRepository(){
    }


    public ChatRepository(FirestoreHelper firestoreHelper, SharedViewModel sharedViewModel) {
        this.firestoreHelper = firestoreHelper;
        this.sharedViewModel = sharedViewModel;
    }



    // Получение чатов по тикету и обновление профиля пользователя
    public void checkIfExisChat(String ticketId) {
        firestoreHelper.getChatsFromTicket(ticketId, new FirestoreHelper.OnChatsFetchedListener() {
            @Override
            public void onChatsFetched(List<String> fetchedChatIds) {
                // Обновляем чаты Firmen в SharedViewModel
                FirmenModel profile = sharedViewModel.getUserProfile().getValue();
                if (profile != null) {
                    //Обновляем все чаты компании
                    List<String> companyChatIds = profile.getChats();
                    String commonChatId = findCommonChat(sharedViewModel.getSelectedTiket().getValue().getChats(), companyChatIds);
                    if (commonChatId != null) {
                        // Общий чат найден, открыть его
                        sharedViewModel.setChatFound(true); // Общий чат найден
                        openChat(commonChatId);
                        Log.e("ChatRepository", "Общий чат найден!");
                    } else {
                        sharedViewModel.setChatFound(false); // Общий чат не найден
                        prepareToCreateChatOnFirstMessage(ticketId, profile);
                        Log.e("ChatRepository", "Общий чат не найден!");
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                // Обработка ошибки
                Log.e("ChatRepository", "Error fetching chats", e);
            }
        });
    }

    // Метод, который вызывается при отправке первого сообщения
    public void sendFirstMessage(String messageContent) {
        PendingChatCreation pendingChatCreation = sharedViewModel.getPendingChatCreation().getValue();
        // Создаем новый чат и отправляем сообщение
        firestoreHelper.createChat(pendingChatCreation.getTicketId(),
                sharedViewModel.getSelectedTiket().getValue().getUserId(),
                pendingChatCreation.getProfile().getUserId(),//Company ID
                new FirestoreHelper.OnChatCreatedListener() {
                    @Override
                    public void onChatCreated(String newChatId) {
                        // Обновляем PendingChatCreation с новым chatId
                        //pendingChatCreation.setChatId(newChatId);
                        //sharedViewModel.setPendingChatCreation(pendingChatCreation);
                        //List<String> userIds = new ArrayList<>();
                        //userIds.add(sharedViewModel.getSelectedTiket().getValue().getUserId());
                        //userIds.add(pendingChatCreation.getProfile().getUserId());
                        createNewChatRoom(newChatId);
                        // Отправляем сообщение в новый чат
                        sendMessageToNewChat(newChatId, messageContent);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("ChatRepository", "Error creating chat", e);
                    }
                });
    }
    //Загружаем чат этого Тикета и этой Фирмы
    private void openChat(String chatId) {
        Log.d("ChatRepository", "Opening chat: " + chatId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference chatRef = db.collection("chats").document(chatId);

        // Получаем данные чата
        chatRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Получаем данные чата
                    ChatroomModel chatroomModel = document.toObject(ChatroomModel.class);
                    if (chatroomModel != null) {
                        // Обновляем LiveData в SharedViewModel
                        sharedViewModel.setChatroomModel(chatroomModel);
                        // Логика для отображения чата, например, загрузка сообщений
                        //loadMessages(chatId);
                    }
                } else {
                    Log.d("ChatRepository", "No such document");
                }
            } else {
                Log.d("ChatRepository", "Failed to get chat data", task.getException());
            }
        });
    }

    public void createNewChatRoom(String chatroomId){
        ChatroomModel chatroomModel = new ChatroomModel(
                chatroomId,
                sharedViewModel.getSelectedTiket().getValue().getTicketId(),
                sharedViewModel.getSelectedTiket().getValue().getUserId(),
                sharedViewModel.getUserProfile().getValue().getUserId().toString(),
                new Timestamp(new Date()) // initial timestamp

        );
        sharedViewModel.setChatroomModel(chatroomModel);
    }
    private void prepareToCreateChatOnFirstMessage(String ticketId, FirmenModel profile) {
        sharedViewModel.setPendingChatCreation(new PendingChatCreation(ticketId, profile));
    }

    public void sendMessageToExistingChat(String existingChatId, String messageContent) {
        // Создаем объект сообщения
        ChatMessageModel chatMessage = new ChatMessageModel(
                messageContent,
                sharedViewModel.getUserProfile().getValue().getUserId(),
                new Timestamp(new Date())
        );
        firestoreHelper.sendMessageToChat(existingChatId, chatMessage, new FirestoreHelper.OnMessageSentListener() {
            @Override
            public void onMessageSent() {
                Log.d("ChatRepository", "Message sent to existing chat");

            }
            @Override
            public void onError(Exception e) {
                // Логика обработки ошибок
                Log.e("ChatRepository", "Error sending message", e);
            }
        });
    }



    private void sendMessageToNewChat(String chatId, String messageContent) {
        ChatMessageModel chatMessage = new ChatMessageModel(
                messageContent,
                sharedViewModel.getUserProfile().getValue().getUserId(),
                new Timestamp(new Date()));
        firestoreHelper.sendMessageToChat(chatId, chatMessage, new FirestoreHelper.OnMessageSentListener() {
            @Override
            public void onMessageSent() {
                Log.d("ChatRepository", "Message sent to new chat");
                sharedViewModel.clearPendingChatCreation();
            }
            @Override
            public void onError(Exception e) {
                Log.e("ChatRepository", "Error sending message", e);
            }
        });
    }

    // Проверка наличия общего чата
    private String findCommonChat(List<String> ticketChatIds, List<String> companyChatIds) {
        if (ticketChatIds != null && companyChatIds != null) {
            for (String chatId : ticketChatIds) {
                if (companyChatIds.contains(chatId)) {
                    return chatId;
                }
            }
        }
        return null;
    }



}
