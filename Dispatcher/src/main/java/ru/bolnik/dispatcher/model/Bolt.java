package ru.bolnik.dispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// модель болта
@AllArgsConstructor
@Getter
@ToString
public class Bolt {

    private final String gost;
    private final String size;
    private final int length;
    // вес всего количество болтов
    private final double weight;


}