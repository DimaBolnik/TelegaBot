package ru.bolnik.messagedbhandler.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nuts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gost", nullable = false, length = 50)
    private String gost;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "weight_gram", nullable = false)
    private Integer weight; // вес одной гайки в граммах
}