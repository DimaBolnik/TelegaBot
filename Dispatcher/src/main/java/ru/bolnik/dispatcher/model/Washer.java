package ru.bolnik.dispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


// модель шайбы
@Getter
@ToString
@AllArgsConstructor
public class Washer implements Product {

    private final String gost;
    private final int size;
    // вес всего колличества шайб
    private final int weight;
}