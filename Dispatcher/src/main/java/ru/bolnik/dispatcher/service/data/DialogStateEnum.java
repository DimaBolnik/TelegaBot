package ru.bolnik.dispatcher.service.data;

// состояние диалога с пользователем для болтов и гаек
public enum DialogStateEnum {
    START,
    WAIT_FOR_PRODUCT_TYPE,
    WAIT_BOLT_GOST,
    WAIT_BOLT_SIZE,
    WAIT_BOLT_LENGTH,
    WAIT_BOLT_WEIGHT
}
