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
import com.example.companies.SharedViewModel;
import com.example.companies.ui.chat.PendingChatCreation;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private SharedViewModel sharedViewModel;
    private FirestoreHelper firestoreHelper;
    public ChatRepository(){

    }

    public ChatRepository(FirestoreHelper firestoreHelper, SharedViewModel sharedViewModel) {
        this.firestoreHelper = firestoreHelper;
        this.sharedViewModel = sharedViewModel;
    }

    // Получение чатов по тикету и обновление профиля пользователя
    public void updateChatsForTicket(String ticketId) {
        firestoreHelper.getChatsFromTicket(ticketId, new FirestoreHelper.OnChatsFetchedListener() {
            @Override
            public void onChatsFetched(List<String> fetchedChatIds) {
                // Обновляем чаты Firmen в SharedViewModel
                FirmenModel profile = sharedViewModel.getUserProfile().getValue();
                if (profile != null) {
                    //Обновляем все чаты компании
                    //profile.setChats(fetchedChatIds);
                    //sharedViewModel.setUserProfile(profile);
                    //Получаем список чатов компании
                    List<String> companyChatIds = profile.getChats();
                    //получаем одинаковые чаты
                    //ticketChatIds - чат тикета
                    //companyChatIds чат фирм
                    String commonChatId = findCommonChat(sharedViewModel.getSelectedTiket().getValue().getChats(), companyChatIds);

                    if (commonChatId != null) {
                        // Общий чат найден, открыть его
                        openChat(commonChatId);
                    } else {
                        // Общий чат не найден, создать новый чат
                        prepareToCreateChatOnFirstMessage(ticketId, profile);
                        Log.e("ChatRepository", "Общий чат не найден!");
                        //createNewChat(ticketId, profile);
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
    public void sendMessage(String messageContent) {
        PendingChatCreation pendingChatCreation = sharedViewModel.getPendingChatCreation().getValue();
        if (pendingChatCreation != null) {
            firestoreHelper.createChat(pendingChatCreation.getTicketId(),pendingChatCreation.getProfile().getUserId(), pendingChatCreation.getProfile().getUserId(), new FirestoreHelper.OnChatCreatedListener() {
                @Override
                public void onChatCreated(String newChatId) {
                    // Чат создан, теперь можно отправить сообщение
                    //firestoreHelper.sendMessageToChat(newChatId, messageContent);

                    // Обновляем списки чатов в профиле компании
                    List<String> companyChatIds = pendingChatCreation.getProfile().getChats();
                    if (companyChatIds != null) {
                        companyChatIds.add(newChatId);
                    } else {
                        companyChatIds = new ArrayList<>();
                        companyChatIds.add(newChatId);
                    }
                    pendingChatCreation.getProfile().setChats(companyChatIds);
                    sharedViewModel.setUserProfile(pendingChatCreation.getProfile());

                    // Обновляем чаты тикета
                    //updateTicketChats(pendingChatCreation.getTicketId(), newChatId);
                    openChat(newChatId);

                    // Удалить pendingChatCreation после создания чата и отправки сообщения
                    sharedViewModel.clearPendingChatCreation();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ChatRepository", "Error creating chat", e);
                }
            });
        }
    }
    private void prepareToCreateChatOnFirstMessage(String ticketId, FirmenModel profile) {
        sharedViewModel.setPendingChatCreation(new PendingChatCreation(ticketId, profile));
    }

    // Открытие чата
    private void openChat(String chatId) {
        // Логика открытия чата, например, передача chatId в адаптер или другой компонент
        Log.d("ChatRepository", "Чат найден Opening chat: " + chatId);
        // Пример: обновить LiveData в SharedViewModel, чтобы наблюдающий
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
