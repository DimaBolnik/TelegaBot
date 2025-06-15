package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;

/* временное хранение данных Гайки до завершения диалога,
 хранение состояния диалога сразу нескольких пользователей*/
public class NutDataStore {
    private static final Map<Long, String> gosts = new HashMap<>();
    private static final Map<Long, String> sizes = new HashMap<>();

    public static void setTempGost(Long chatId, String gost) {
        gosts.put(chatId, gost);
    }

    public static void setTempSize(Long chatId, String size) {
        sizes.put(chatId, size);
    }

    public static String getTempGost(Long chatId) {
        return gosts.get(chatId);
    }

    public static String getTempSize(Long chatId) {
        return sizes.get(chatId);
    }

    public static void clear(Long chatId) {
        gosts.remove(chatId);
        sizes.remove(chatId);
    }
}
