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
            case WAIT_FOR_PRODUCT_TYPE -> chooseProductType(chatId, messageText);
            case WAIT_GOST -> waitForGost(chatId, messageText);
            case WAIT_SIZE -> waitForSize(chatId, messageText);
            case WAIT_LENGTH -> waitForLength(chatId, messageText);
            case WAIT_WEIGHT -> waitForWeight(chatId, messageText);
            default -> sendMessage(chatId, "Неизвестное состояние. Попробуйте снова.");
        }
    }

    // 1. Начало диалога
    private void startDialog(Long chatId) {
        sendMessage(chatId, """
                Здравствуйте! Что вы хотите создать?
                1. Болт
                2. Гайка
                3. Шайба
                """);
        DialogState.setState(chatId, DialogStateEnum.WAIT_FOR_PRODUCT_TYPE);
    }

    // 2. Выбор типа продукта
    private void chooseProductType(Long chatId, String input) {
        ProductTypeEnum productType = ProductTypeEnum.fromLabel(input);
        if (productType == null) {
            sendMessage(chatId, "Неверный выбор. Пожалуйста, выберите 'Болт', 'Гайка' или 'Шайба'.");
            return;
        }

        DialogState.setState(chatId, DialogStateEnum.WAIT_GOST);
        DialogState.setProductType(chatId, productType);
        sendMessage(chatId, "Введите ГОСТ:");
    }

    // 3. Ввод ГОСТа
    private void waitForGost(Long chatId, String gost) {
        ProductDataStore.setGost(chatId, gost);
        DialogState.setState(chatId, DialogStateEnum.WAIT_SIZE);
        sendMessage(chatId, "Введите размер (мм):");
    }

    // 4. Ввод размера
    private void waitForSize(Long chatId, String size) {
        ProductTypeEnum productType = DialogState.getProductType(chatId);
        ProductDataStore.setSize(chatId, size);

        if (productType == ProductTypeEnum.BOLT) {
            DialogState.setState(chatId, DialogStateEnum.WAIT_LENGTH);
            sendMessage(chatId, "Введите длину (мм):");
        } else {
            DialogState.setState(chatId, DialogStateEnum.WAIT_WEIGHT);
            sendMessage(chatId, "Введите общий вес (гр):");
        }
    }

    // 5. Ввод длины (только для болта)
    private void waitForLength(Long chatId, String lengthStr) {
        try {
            int length = Integer.parseInt(lengthStr);
            BoltDataStore.setLength(chatId, length);
            DialogState.setState(chatId, DialogStateEnum.WAIT_WEIGHT);
            sendMessage(chatId, "Введите общий вес (гр):");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Длина должна быть числом. Попробуйте снова.");
        }
    }

    // 6. Ввод общего веса и отправка в kafka
    private void waitForWeight(Long chatId, String weightStr) {
        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                sendMessage(chatId, "Вес должен быть положительным числом.");
                return;
            }

            ProductTypeEnum productType = DialogState.getProductType(chatId);
            Product product = createProduct(chatId, productType, weight);

            kafkaUpdateService.sendProductToKafka(product, chatId);
            clearTempData(chatId, productType);

            sendMessage(chatId, productType.getLabel() + " успешно создан и отправлен!");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Вес должен быть числом. Попробуйте снова.");
        }
    }

    // Создание объекта Product на основе типа
    private Product createProduct(Long chatId, ProductTypeEnum productType, double weight) {
        String gost = ProductDataStore.getGost(chatId);
        String size = ProductDataStore.getSize(chatId);

        return switch (productType) {
            case BOLT -> new Bolt(gost, size, BoltDataStore.getLength(chatId), weight);
            case NUT -> new Nut(gost, size, weight);
            case WASHER -> new Washer(gost, size, weight);
        };
    }

    // Очистка временных данных
    private void clearTempData(Long chatId, ProductTypeEnum productType) {
        ProductDataStore.clear(chatId);
        if (productType == ProductTypeEnum.BOLT) {
            BoltDataStore.clear(chatId);
        }
        DialogState.clearState(chatId);
    }

    // Отправка сообщения в телеграм
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
