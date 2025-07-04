package ru.bolnik.dispatcher.service.data;

import java.util.HashMap;
import java.util.Map;

// хранение состояний пользователей по chatId из телеги
public class DialogState {

    private static final Map<Long, DialogStateEnum> states = new HashMap<>();
    private static final Map<Long, ProductTypeEnum> productTypes = new HashMap<>();

    public static void setState(Long chatId, DialogStateEnum state) {
        states.put(chatId, state);
    }

    public static DialogStateEnum getState(Long chatId) {
        return states.getOrDefault(chatId, DialogStateEnum.START);
    }

    public static void setProductType(Long chatId, ProductTypeEnum type) {
        productTypes.put(chatId, type);
    }

    public static ProductTypeEnum getProductType(Long chatId) {
        return productTypes.get(chatId);
    }

    public static void clearState(Long chatId) {
        states.remove(chatId);
        productTypes.remove(chatId);
    }
}
