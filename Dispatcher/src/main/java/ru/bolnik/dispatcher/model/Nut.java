package ru.bolnik.dispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// модель гайки
@Getter
@ToString
@AllArgsConstructor
public class Nut implements Product {

    private final String gost;
    private final int size;
    // вес всего колличества гаек
    private final int weight;
    private final Integer quantity;
}