package ru.bolnik.dispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// модель болта
@Getter
@ToString
@AllArgsConstructor
public class Bolt implements Product {

    private final String gost;
    private final int size;
    private final int length;
    // вес всего количество болтов
    private final int weight;
    private final Integer quantity;

}