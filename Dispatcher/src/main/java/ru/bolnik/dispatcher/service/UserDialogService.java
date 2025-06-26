package ru.bolnik.dispatcher.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bolnik.dispatcher.controller.TelegramBot;
import ru.bolnik.dispatcher.model.Bolt;
import ru.bolnik.dispatcher.model.Nut;
import ru.bolnik.dispatcher.model.tempState.BoltDataStore;
import ru.bolnik.dispatcher.model.tempState.NutDataStore;
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
            case WAIT_FOR_PRODUCT_TYPE -> waitForProductType(chatId, messageText);
            case WAIT_BOLT_GOST -> waitForBoltGost(chatId, messageText);
            case WAIT_BOLT_SIZE -> waitForBoltSize(chatId, messageText);
            case WAIT_BOLT_LENGTH -> waitForBoltLength(chatId, messageText);
            case WAIT_BOLT_WEIGHT -> waitForBoltWeight(chatId, messageText);
            case WAIT_NUT_GOST -> waitForNutGost(chatId, messageText);
            case WAIT_NUT_SIZE -> waitForNutSize(chatId, messageText);
            case WAIT_NUT_WEIGHT -> waitForNutWeight(chatId, messageText);
        }
    }

    private void startDialog(Long chatId) {
        sendMessage(chatId, "Здравствуйте! Что вы хотите создать?\n1. Болт\n2. Гайка");
        DialogState.setState(chatId, DialogStateEnum.WAIT_FOR_PRODUCT_TYPE);
    }

    private void waitForProductType(Long chatId, String input) {
        ProductTypeEnum productType = ProductTypeEnum.fromLabel(input);

        if (productType == null) {
            sendMessage(chatId, "Неверный выбор. Пожалуйста, выберите 'Болт' или 'Гайка'.");
            return;
        }

        switch (productType) {
            case BOLT -> {
                DialogState.setState(chatId, DialogStateEnum.WAIT_BOLT_GOST);
                sendMessage(chatId, "Выбран болт. Введите ГОСТ:");
            }
            case NUT -> {
                DialogState.setState(chatId, DialogStateEnum.WAIT_NUT_GOST);
                sendMessage(chatId, "Выбрана гайка. Введите ГОСТ:");
            }
        }
    }

    private void waitForBoltGost(Long chatId, String gost) {
        BoltDataStore.setTempGost(chatId, gost);
        DialogState.setState(chatId, DialogStateEnum.WAIT_BOLT_SIZE);
        sendMessage(chatId, "Введите размер болта (миллиметр):");
    }

    private void waitForBoltSize(Long chatId, String size) {
        BoltDataStore.setTempSize(chatId, size);
        DialogState.setState(chatId, DialogStateEnum.WAIT_BOLT_LENGTH);
        sendMessage(chatId, "Введите длину болта (миллиметр):");
    }

    private void waitForBoltLength(Long chatId, String lengthStr) {
        try {
            int length = Integer.parseInt(lengthStr);
            DialogState.setState(chatId, DialogStateEnum.WAIT_BOLT_WEIGHT);
            BoltDataStore.setTempLength(chatId, length);
            sendMessage(chatId, "Введите общий вес болтов (грамм):");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Длина должна быть числом. Попробуйте снова.");
        }
    }

    private void waitForBoltWeight(Long chatId, String weightStr) {
        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                sendMessage(chatId, "Вес должен быть положительным числом.");
                return;
            }

            int length = BoltDataStore.getTempLength(chatId);
            String gost = BoltDataStore.getTempGost(chatId);
            String size = BoltDataStore.getTempSize(chatId);

            Bolt bolt = new Bolt(gost, size, length, weight);

            // Отправляем объект в Kafka
            kafkaUpdateService.sendProductToKafka(bolt, chatId);

            DialogState.clearState(chatId);
            BoltDataStore.clear(chatId);
            sendMessage(chatId, "Болт успешно создан и отправлен в систему!");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Вес должен быть числом. Попробуйте снова.");
        }
    }

    private void waitForNutGost(Long chatId, String gost) {
        NutDataStore.setTempGost(chatId, gost);
        DialogState.setState(chatId, DialogStateEnum.WAIT_NUT_SIZE);
        sendMessage(chatId, "Введите размер гайки  (миллиметр):");
    }

    private void waitForNutSize(Long chatId, String size) {
        NutDataStore.setTempSize(chatId, size);
        DialogState.setState(chatId, DialogStateEnum.WAIT_NUT_WEIGHT);
        sendMessage(chatId, "Введите общий вес гаек (грамм):");
    }

    private void waitForNutWeight(Long chatId, String weightStr) {
        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                sendMessage(chatId, "Вес должен быть положительным числом.");
                return;
            }

            String gost = NutDataStore.getTempGost(chatId);
            String size = NutDataStore.getTempSize(chatId);

            Nut nut = new Nut(gost, size, weight);
            kafkaUpdateService.sendProductToKafka(nut, chatId);

            NutDataStore.clear(chatId);
            DialogState.clearState(chatId);
            sendMessage(chatId, "Гайка успешно создана и отправлена в систему!");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Вес должен быть числом. Попробуйте снова.");
        }
    }

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
