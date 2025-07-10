package ru.bolnik.dispatcher.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bolnik.dispatcher.controller.TelegramBot;
import ru.bolnik.dispatcher.model.Bolt;
import ru.bolnik.dispatcher.model.Nut;
import ru.bolnik.dispatcher.model.Product;
import ru.bolnik.dispatcher.model.Washer;
import ru.bolnik.dispatcher.model.tempState.BoltDataStore;
import ru.bolnik.dispatcher.model.tempState.ProductDataStore;
import ru.bolnik.dispatcher.service.data.ActionTypeEnum;
import ru.bolnik.dispatcher.service.data.DialogState;
import ru.bolnik.dispatcher.service.data.DialogStateEnum;
import ru.bolnik.dispatcher.service.data.ProductTypeEnum;

@Service
public class UserDialogService {

    private final TelegramBot telegramBot;
    private final KafkaUpdateService kafkaUpdateService;

    //Ленивая загрузка TelegramBot для снятия цикличности.
    public UserDialogService(@Lazy TelegramBot telegramBot, KafkaUpdateService kafkaUpdateService) {
        this.telegramBot = telegramBot;
        this.kafkaUpdateService = kafkaUpdateService;
    }

    public void processUpdate(Update update) {
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        DialogStateEnum currentState = DialogState.getState(chatId);

        switch (currentState) {
            case START -> startDialog(chatId);
            case WAIT_FOR_ACTION_TYPE -> chooseActionType(chatId, messageText); // Выбор действия
            case WAIT_FOR_PRODUCT_TYPE -> chooseProductType(chatId, messageText); // Выбор изделия
            case WAIT_GOST -> waitForGost(chatId, messageText);
            case WAIT_SIZE -> waitForSize(chatId, messageText);
            case WAIT_LENGTH -> waitForLength(chatId, messageText);
            case WAIT_WEIGHT -> waitForWeight(chatId, messageText);
            case WAIT_QUANTITY -> waitForQuantity(chatId, messageText);
            case FINISH -> handleProductCreation(chatId);
            default -> sendMessage(chatId, "Неизвестное состояние. Попробуйте снова.");
        }
    }

    // 1. Начало диалога: выбор действия
    private void startDialog(Long chatId) {
        sendMessage(chatId, """
                Здравствуйте! Что вы хотите сделать?
                1. Указать общий вес и рассчитать количество
                2. Указать количество и узнать общий вес
                """);
        DialogState.setState(chatId, DialogStateEnum.WAIT_FOR_ACTION_TYPE);
    }

    // 2. Выбор действия: WEIGHT_TO_QUANTITY или QUANTITY_TO_WEIGHT
    private void chooseActionType(Long chatId, String input) {
        switch (input.trim()) {
            case "1" -> {
                DialogState.setActionType(chatId, ActionTypeEnum.WEIGHT_TO_QUANTITY);
                DialogState.setState(chatId, DialogStateEnum.WAIT_FOR_PRODUCT_TYPE);
                chooseProductTypeMenu(chatId);
            }
            case "2" -> {
                DialogState.setActionType(chatId, ActionTypeEnum.QUANTITY_TO_WEIGHT);
                DialogState.setState(chatId, DialogStateEnum.WAIT_FOR_PRODUCT_TYPE);
                chooseProductTypeMenu(chatId);
            }
            default -> sendMessage(chatId, "Выберите 1 или 2.");
        }
    }

    // Меню выбора типа изделия
    private void chooseProductTypeMenu(Long chatId) {
        sendMessage(chatId, """
                Что вы хотите создать?
                1. Болт
                2. Гайка
                3. Шайба
                """);
    }

    // 3. Выбор типа изделия (болт / гайка / шайба)
    private void chooseProductType(Long chatId, String input) {
        ProductTypeEnum productType = ProductTypeEnum.fromLabel(input);
        if (productType == null) {
            sendMessage(chatId, "Неверный выбор. Пожалуйста, выберите 'Болт', 'Гайка' или 'Шайба'.");
            return;
        }

        DialogState.setProductType(chatId, productType);
        DialogState.setState(chatId, DialogStateEnum.WAIT_GOST);
        sendMessage(chatId, "Вы выбрали %s , введите ГОСТ:".formatted(productType.getLabel()));
    }
    // 4. Ввод ГОСТа
    private void waitForGost(Long chatId, String gost) {
        ProductDataStore.setGost(chatId, gost);
        DialogState.setState(chatId, DialogStateEnum.WAIT_SIZE);
        sendMessage(chatId, "Введите размер (мм):");
    }

    // 5. Ввод размера
    private void waitForSize(Long chatId, String sizeStr) {
        if (!isValidPositiveInteger(chatId, sizeStr, "Размер")) {
            return;
        }

        int size = Integer.parseInt(sizeStr);
        ProductTypeEnum productType = DialogState.getProductType(chatId);
        ProductDataStore.setSize(chatId, size);

        if (productType == ProductTypeEnum.BOLT) {
            DialogState.setState(chatId, DialogStateEnum.WAIT_LENGTH);
            sendMessage(chatId, "Введите длину (мм):");
        } else {
            proceedToFinalStep(chatId);
        }
    }

    // 6. Ввод длины (только для болта)
    private void waitForLength(Long chatId, String lengthStr) {
        if (!isValidPositiveInteger(chatId, lengthStr, "Длина")) {
            return;
        }

        int length = Integer.parseInt(lengthStr);
        BoltDataStore.setLength(chatId, length);
        proceedToFinalStep(chatId);
    }

    // Переход к финальному шагу
    private void proceedToFinalStep(Long chatId) {
        ActionTypeEnum actionType = DialogState.getActionType(chatId);
        if (actionType == ActionTypeEnum.WEIGHT_TO_QUANTITY) {
            DialogState.setState(chatId, DialogStateEnum.WAIT_WEIGHT);
            sendMessage(chatId, "Введите общий вес (гр):");
        } else {
            DialogState.setState(chatId, DialogStateEnum.WAIT_QUANTITY);
            sendMessage(chatId, "Введите количество:");
        }
    }

    // 7. Ввод веса
    private void waitForWeight(Long chatId, String weightStr) {
        if (!isValidPositiveInteger(chatId, weightStr, "Вес")) {
            return;
        }

        int weight = Integer.parseInt(weightStr);
        ProductDataStore.setWeight(chatId, weight);
        DialogState.setState(chatId, DialogStateEnum.FINISH);
        handleProductCreation(chatId);
    }

    // 8. Ввод количества
    private void waitForQuantity(Long chatId, String quantityStr) {
        if (!isValidPositiveInteger(chatId, quantityStr, "Количество")) {
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        ProductDataStore.setQuantity(chatId, quantity);
        DialogState.setState(chatId, DialogStateEnum.FINISH);
        handleProductCreation(chatId);
    }

    // 9. Создание и отправка продукта
    private void handleProductCreation(Long chatId) {
        ProductTypeEnum productType = DialogState.getProductType(chatId);
        ActionTypeEnum actionType = DialogState.getActionType(chatId);

        Product product = createProduct(chatId, productType, actionType);
        kafkaUpdateService.sendProductToKafka(product, chatId);
        clearTempData(chatId);
        sendMessage(chatId, productType.getLabel() + " успешно создан и отправлен!");
    }

    // Создание объекта Product на основе типа и действия
    private Product createProduct(Long chatId, ProductTypeEnum productType, ActionTypeEnum actionType) {
        String gost = ProductDataStore.getGost(chatId);
        int size = ProductDataStore.getSize(chatId);

        return switch (productType) {
            case BOLT -> new Bolt(gost, size, BoltDataStore.getLength(chatId),
                    actionType == ActionTypeEnum.WEIGHT_TO_QUANTITY ? ProductDataStore.getWeight(chatId) : 0,
                    actionType == ActionTypeEnum.QUANTITY_TO_WEIGHT ? ProductDataStore.getQuantity(chatId) : 0);
            case NUT -> new Nut(gost, size,
                    actionType == ActionTypeEnum.WEIGHT_TO_QUANTITY ? ProductDataStore.getWeight(chatId) : 0,
                    actionType == ActionTypeEnum.QUANTITY_TO_WEIGHT ? ProductDataStore.getQuantity(chatId) : 0);
            case WASHER -> new Washer(gost, size,
                    actionType == ActionTypeEnum.WEIGHT_TO_QUANTITY ? ProductDataStore.getWeight(chatId) : 0,
                    actionType == ActionTypeEnum.QUANTITY_TO_WEIGHT ? ProductDataStore.getQuantity(chatId) : 0);
        };
    }

    // Проверка, что строка является положительным целым числом
    private boolean isValidPositiveInteger(Long chatId, String valueStr, String fieldName) {
        try {
            int value = Integer.parseInt(valueStr);
            if (value <= 0) {
                sendMessage(chatId, fieldName + " должно быть положительным числом.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            sendMessage(chatId, fieldName + " должно быть целым числом. Попробуйте снова.");
            return false;
        }
    }

    // Очистка временных данных
    private void clearTempData(Long chatId) {
        ProductDataStore.clear(chatId);
        if (DialogState.getProductType(chatId) == ProductTypeEnum.BOLT) {
            BoltDataStore.clear(chatId);
        }
        DialogState.clearState(chatId);
    }

    // Отправка сообщения в Telegram
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            telegramBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
