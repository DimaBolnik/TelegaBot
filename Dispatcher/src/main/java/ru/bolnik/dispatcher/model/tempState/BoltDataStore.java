package ru.bolnik.dispatcher.model.tempState;

import java.util.HashMap;
import java.util.Map;


// временное хранение данных болта до завершения диалога, хранение состояния сразу нескольких пользователей
public class BoltDataStore extends ProductDataStore {

    private static final Map<Long, Integer> lengths = new HashMap<>();

    public static void setLength(Long chatId, int length) {
        lengths.put(chatId, length);
    }

    public static int getLength(Long chatId) {
        return lengths.getOrDefault(chatId, 0);
    }

    public static void clear(Long chatId) {
        lengths.remove(chatId);
        ProductDataStore.clear(chatId);
    }
}
