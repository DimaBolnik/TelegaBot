package ru.bolnik.dispatcher.service.data;

import java.util.HashMap;
import java.util.Map;

// хранение состояний пользователей по chatId из телеги
public class DialogState {

    private static final Map<Long, DialogStateEnum> states = new HashMap<>();

    public static void setState(Long chatId, DialogStateEnum state) {
        states.put(chatId, state);
    }

    public static DialogStateEnum getState(Long chatId) {
        return states.getOrDefault(chatId, DialogStateEnum.START);
    }

    public static void clearState(Long chatId) {
        states.remove(chatId);
    }
}
