package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;


// временное хранение данных болта до завершения диалога, хранение состояния сразу нескольких пользователей
public class BoltDataStore {

    private static final Map<Long, String> gosts = new HashMap<>();
    private static final Map<Long, String> sizes = new HashMap<>();
    private static final Map<Long, Integer> lengths = new HashMap<>();
    private static final Map<Long, Double> weights = new HashMap<>();

    public static void setTempGost(Long chatId, String gost) {
        gosts.put(chatId, gost);
    }

    public static void setTempSize(Long chatId, String size) {
        sizes.put(chatId, size);
    }

    public static void setTempLength(Long chatId, int length) {
        lengths.put(chatId, length);
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

    public static int getTempLength(Long chatId) {
        return lengths.getOrDefault(chatId, 0);
    }

    public static double getTempWeight(Long chatId) {
        return weights.getOrDefault(chatId, 0.0);
    }

    public static void clear(Long chatId) {
        gosts.remove(chatId);
        sizes.remove(chatId);
        lengths.remove(chatId);
        weights.remove(chatId);
    }
}
