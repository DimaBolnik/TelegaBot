package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;

// временное хранение данных до завершения диалога, хранение состояния сразу нескольких пользователей
public class ProductDataStore {

    private static final Map<Long, String> gosts = new HashMap<>();
    private static final Map<Long, String> sizes = new HashMap<>();
    private static final Map<Long, Double> weights = new HashMap<>();

    public static void setGost(Long chatId, String gost) {
        gosts.put(chatId, gost);
    }

    public static void setSize(Long chatId, String size) {
        sizes.put(chatId, size);
    }

    public static void setWeight(Long chatId, double weight) {
        weights.put(chatId, weight);
    }

    public static String getGost(Long chatId) {
        return gosts.get(chatId);
    }

    public static String getSize(Long chatId) {
        return sizes.get(chatId);
    }

    public static double getWeight(Long chatId) {
        return weights.getOrDefault(chatId, 0.0);
    }

    public static void clear(Long chatId) {
        gosts.remove(chatId);
        sizes.remove(chatId);
        weights.remove(chatId);
    }
}