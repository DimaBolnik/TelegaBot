package ru.bolnik.dispatcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bolnik.dispatcher.service.KafkaUpdateService;

//  Обрабатывает входящие обновления (Update) и может быть расширен для обработки разных типов команд или состояний.
@Component
public class TelegramMessageDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

//  Храним ссылку на TelegramBot и использует его для отправки ответов
    private TelegramBot telegramBot;
    private final KafkaUpdateService kafkaUpdateService;

    public TelegramMessageDispatcher(KafkaUpdateService kafkaUpdateService) {
        this.kafkaUpdateService = kafkaUpdateService;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (isHasText(update)) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            logger.info("Получено сообщение: {} от пользователя с chatId: {}", messageText, chatId);

            // Отправляем в Kafka
            kafkaUpdateService.sendUpdateToKafka(update);

            responseBot(chatId);
        }
    }

    private void responseBot(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("сообщение получено");
        // Отправляем ответ пользователю
        telegramBot.sendResponse(sendMessage);
    }

    private static boolean isHasText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
