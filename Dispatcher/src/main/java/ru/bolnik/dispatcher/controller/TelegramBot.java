package ru.bolnik.dispatcher.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final TelegramMessageDispatcher telegramMessageDispatcher;

    public TelegramBot(@Value("${telegram.bot.token}")String botToken, TelegramMessageDispatcher telegramMessageDispatcher) {
        super(botToken);
        this.telegramMessageDispatcher = telegramMessageDispatcher;
    }

    @PostConstruct
    public void init() {
        telegramMessageDispatcher.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramMessageDispatcher.processUpdate(update);
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

}