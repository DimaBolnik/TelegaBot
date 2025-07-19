package ru.bolnik.dispatcher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bolnik.dispatcher.controller.TelegramBot;
import ru.bolnik.dispatcher.dto.ProductResponseDto;

@Component
public class KafkaResponseConsumer {

    private final TelegramBot telegramBot;
    private final ObjectMapper objectMapper;

    public KafkaResponseConsumer(TelegramBot telegramBot, ObjectMapper objectMapper) {
        this.telegramBot = telegramBot;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.telegram-responses}", groupId = "dispatcher-group")
    public void listen(String message) {
        try {
            ProductResponseDto response = objectMapper.readValue(message, ProductResponseDto.class);
            String text;

            if (response.getQuantity() != null) {
                text = "По заданному весу получилось: " + response.getQuantity() + " шт.";
            } else if (response.getTotalWeight() != null) {
                text = "Общий вес: " + response.getTotalWeight() + " г.";
            } else {
                text = "Не удалось рассчитать результат.";
            }

            SendMessage sendMessage = new SendMessage(response.getChatId().toString(), text);
            telegramBot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
