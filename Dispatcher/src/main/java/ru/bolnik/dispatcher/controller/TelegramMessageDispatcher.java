package ru.bolnik.dispatcher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bolnik.dispatcher.service.UserDialogService;

//  Обрабатывает входящие обновления (Update) и может быть расширен для обработки разных типов команд или состояний.
@Component
public class TelegramMessageDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    //  Храним ссылку на TelegramBot и использует его для отправки ответов
    private TelegramBot telegramBot;

    private final UserDialogService userDialogService;

    public TelegramMessageDispatcher(UserDialogService userDialogService) {
        this.userDialogService = userDialogService;
    }


    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            userDialogService.processUpdate(update);
        }

    }

}
