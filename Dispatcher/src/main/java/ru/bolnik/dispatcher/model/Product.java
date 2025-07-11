package ru.bolnik.dispatcher.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface Product {
    String getGost();
    int getSize();
    int getWeight();
    Integer getQuantity();
}
