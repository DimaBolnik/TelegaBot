package ru.bolnik.messagedbhandler.entity;

import jakarta.persistence.*;
import lombok.*;

// класс для шайбы
@Entity
@Table(name = "washers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Washer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gost", nullable = false, length = 50)
    private String gost;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "weight_gram", nullable = false)
    private Double weight; // вес одной шайбы в граммах
}

