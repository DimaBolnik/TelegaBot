package ru.bolnik.dispatcher.service.data;

/* состояние диалога с пользователем для болтов и гаек,
 пока так, потом организую как-нибудь по другому.*/
public enum DialogStateEnum {
    START,
    WAIT_FOR_PRODUCT_TYPE,
    WAIT_BOLT_GOST,
    WAIT_BOLT_SIZE,
    WAIT_BOLT_LENGTH,
    WAIT_BOLT_WEIGHT,
    WAIT_NUT_GOST,
    WAIT_NUT_SIZE,
    WAIT_NUT_WEIGHT,
    WAIT_WASHER_GOST,
    WAIT_WASHER_SIZE,
    WAIT_WASHER_WEIGHT
}
