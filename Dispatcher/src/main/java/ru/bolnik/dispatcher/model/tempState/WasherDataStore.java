package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;

/* временное хранение данных Шайбы до завершения диалога,
 хранение состояния диалога сразу нескольких пользователей*/
public class WasherDataStore {

    private static final Map<Long, String> gosts = new HashMap<>();
    private static final Map<Long, String> sizes = new HashMap<>();
    private static final Map<Long, Double> weights = new HashMap<>();

    public static void setTempGost(Long chatId, String gost) {
        gosts.put(chatId, gost);
    }

    public static void setTempSize(Long chatId, String size) {
        sizes.put(chatId, size);
    }

    public static void setTempWeight(Long chatId, double weight) {
        weights.put(chatId, weight);
    }

    public static String getTempGost(Long chatId) {
        return gosts.get(chatId);
    }

    public static String getTempSize(Long chatId) {
        return sizes.get(chatId);
    }

    public static Double getTempWeight(Long chatId) {
        return weights.get(chatId);
    }

    public static void clear(Long chatId) {
        gosts.remove(chatId);
        sizes.remove(chatId);
        weights.remove(chatId);
    }
}