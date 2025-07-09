package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;

// временное хранение данных до завершения диалога, хранение состояния сразу нескольких пользователей
public class ProductDataStore {

    private static final Map<Long, String> gosts = new HashMap<>();
    private static final Map<Long, Integer> sizes = new HashMap<>();
    private static final Map<Long, Integer> weights = new HashMap<>();

    public static void setGost(Long chatId, String gost) {
        gosts.put(chatId, gost);
    }

    public static void setSize(Long chatId, Integer size) {
        sizes.put(chatId, size);
    }

    public static void setWeight(Long chatId, int weight) {
        weights.put(chatId, weight);
    }

    public static String getGost(Long chatId) {
        return gosts.get(chatId);
    }

    public static int getSize(Long chatId) {
        return sizes.get(chatId);
    }

    public static int getWeight(Long chatId) {
        return weights.getOrDefault(chatId, 0);
    }

    public static void clear(Long chatId) {
        gosts.remove(chatId);
        sizes.remove(chatId);
        weights.remove(chatId);
    }
}