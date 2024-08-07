package com.example.companies.repository;


/*TODO:
*   1. Обновляем информацию о чатах/Тикетах
*   2. Получаем данные из chat у компании
*   3. Данные из чатов у Заказчкиа
*   3. Если есть общие, открываем чат
*   4. Если общих нет, создаём новый чат
*
* */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.common.model.FirmenModel;
import com.example.common.utils.FirestoreHelper;
import com.example.companies.R;
import com.example.companies.SharedViewModel;
import com.example.companies.ui.chat.ChatFragment;
import com.example.companies.ui.chat.ChatMessageModel;
import com.example.companies.ui.chat.ChatroomModel;
import com.example.companies.ui.chat.PendingChatCreation;
import com.example.companies.ui.chat.SystemChatMessageModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatRepository{
    private static final String TAG = "ChatRepository";
    private SharedViewModel sharedViewModel;
    private FirestoreHelper firestoreHelper;
    private ChatFragment chatFragment;
    private Context context;

    public ChatRepository(){

    }


    public ChatRepository(Context context, FirestoreHelper firestoreHelper, SharedViewModel sharedViewModel) {
        this.context = context;
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

                        if (Boolean.TRUE.equals(sharedViewModel.getSelectedTiket().getValue().isKostenVoranschlag())) {
                            // Получите массив строк из ресурсов
                            String[] messages = context.getResources().getStringArray(com.example.common.R.array.kv_anfrage_messages);
                            final Handler handler = new Handler(Looper.getMainLooper());
                            int delay = 0; // Начальная задержка
                            // Отобразите каждое сообщение
                            for (String message : messages) {
                                handler.postDelayed(() -> {
                                    // Отобразите сообщение
                                    if (message.equals(messages[messages.length-1])) {
                                        Log.d("ChatRepository"," message " +  message);
                                        Map<String, Map<String, Object>> preferredTimes = sharedViewModel.getSelectedTiket().getValue().getPreferredTimes();
                                        if (preferredTimes != null) {
                                            List<String> buttons = new ArrayList<>();
                                            for (Map.Entry<String, Map<String, Object>> entry : preferredTimes.entrySet()) {
                                                Map<String, Object> timeSlot = entry.getValue();
                                                if (timeSlot != null) {
                                                    String dateStr = (String) timeSlot.get("date");
                                                    String startTime = (String) timeSlot.get("startTime");
                                                    String endTime = (String) timeSlot.get("endTime");
                                                    // Преобразование строки даты в объект Date
                                                    Date date  = parseDate(dateStr);
                                                    if (date != null) {
                                                        // Проверка, не прошла ли дата
                                                        if (date.after(new Date())) {
                                                            String formattedDate = formatDate(date);
                                                            String timeSlotText = formattedDate + " " + startTime + " - " + endTime;
                                                            buttons.add(timeSlotText);
                                                        }
                                                    }

                                                }
                                            }
                                            displaySystemMessage("Select Time", buttons);
                                            Log.d("ChatRepository"," TimeSlots " +  buttons);

                                        }
                                        // Создание кнопок на основе списка
                                        /*List<String> buttons = Arrays.asList("10:00 - 20:00", "10:00 - 20:00", "10:00 - 20:00");*/

                                    }
                                    else {
                                        displaySystemMessage(message, null);
                                    }

                                }, delay);

                                delay += 1000;
                            }
                        }

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
    private Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.GERMAN); // Подставьте формат вашей даты
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing error: ", e);
            return null;
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM. EEE", Locale.GERMAN); // Подставьте нужный формат
        return sdf.format(date);
    }

    private void displaySystemMessage(String messageContent, List<String> buttons) {
        if (buttons != null && !buttons.isEmpty()) {
            SystemChatMessageModel welcomeMessage = new SystemChatMessageModel(
                    messageContent,
                    sharedViewModel.getUserProfile().getValue().getUserId(),
                    new Timestamp(new Date()),
                    buttons);
            // Обновите LiveData или другой механизм для отображения сообщения
            sharedViewModel.addSystemMessageToChat(welcomeMessage);
            Log.d("ChatRepository", "welcome message inkl. Buttons");
        }else{
            SystemChatMessageModel welcomeMessage = new SystemChatMessageModel(
                    messageContent,
                    sharedViewModel.getUserProfile().getValue().getUserId(),
                    new Timestamp(new Date()),
                    null);
            // Обновите LiveData или другой механизм для отображения сообщения
            sharedViewModel.addSystemMessageToChat(welcomeMessage);
            Log.d("ChatRepository", "Displayed welcome message to new chat");
        }

    }

    // Метод, который вызывается при отправке первого сообщения
    public void sendFirstMessage(String messageContent, boolean isSystem) {
        PendingChatCreation pendingChatCreation = sharedViewModel.getPendingChatCreation().getValue();
        // Создаем новый чат и отправляем сообщение
        firestoreHelper.createChat(pendingChatCreation.getTicketId(),
                sharedViewModel.getSelectedTiket().getValue().getUserId(),
                pendingChatCreation.getProfile().getUserId(),//Company ID
                isSystem,
                new FirestoreHelper.OnChatCreatedListener() {
                    @Override
                    public void onChatCreated(String newChatId) {
                        createNewChatRoom(newChatId);
                        // Отправляем сообщение в новый чат
                        sendMessageToNewChat(newChatId, messageContent, isSystem);
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
                new Timestamp(new Date()),
                false
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

    private void sendMessageToNewChat(String chatId, String messageContent,boolean isSystem) {

        ChatMessageModel chatMessage = new ChatMessageModel(
                messageContent,
                sharedViewModel.getUserProfile().getValue().getUserId(),
                new Timestamp(new Date()),
                isSystem
        );
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
