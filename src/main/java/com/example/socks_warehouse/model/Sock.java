package com.example.socks_warehouse.model;

import com.example.socks_warehouse.common.Color;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "socks")
public class Sock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(6)")
    private Color color;

    @Column(name = "cotton_part", nullable = false, 
    columnDefinition = "SMALLINT CHECK (cotton_part BETWEEN 0 AND 100)")
    private short cottonPart;

    @Column(nullable = false, columnDefinition = "INT CHECK (quantity > 0)")
    private int quantity;

    public Sock(Color color, short cottonPart, int quantity){
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }
}
