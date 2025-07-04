package ru.bolnik.dispatcher.service.data;

/* состояние диалога с пользователем для болтов и гаек,
 пока так, потом организую как-нибудь по другому.*/
public enum DialogStateEnum {
    START,
    WAIT_FOR_PRODUCT_TYPE,
    WAIT_GOST,
    WAIT_SIZE,
    WAIT_LENGTH,
    WAIT_WEIGHT
}
