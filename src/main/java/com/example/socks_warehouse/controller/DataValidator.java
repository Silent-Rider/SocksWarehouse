package com.example.socks_warehouse.controller;

import org.springframework.stereotype.Component;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.exception.InvalidDataFormatException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class DataValidator {

    void checkAttributes(String color, short cottonPart, int quantity){
        try {
            Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Invalid color");
        }
        if (quantity <= 0)
            throw new InvalidDataFormatException("Quantity must be greater than 0");
        if (cottonPart < 0 || cottonPart > 100)
            throw new InvalidDataFormatException("Cotton part must be between 0 and 100");
    }

    void checkFilters(String color, String operator, Integer cottonPart){
        try {
            Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Invalid color");
        }
        if (cottonPart != null && operator == null)
            throw new InvalidDataFormatException("Operator is required when cottonPart is provided.");
        else if (cottonPart == null && operator != null)
            throw new InvalidDataFormatException("CottonPart is required when operator is provided.");
        else if(cottonPart != null && operator != null){
            if(!(operator.equals("moreThan")) && !(operator.equals("lessThan")) && !(operator.equals("equal")))
                throw new InvalidDataFormatException("Invalid operator");
            if (cottonPart < 0 || cottonPart > 100)
                throw new InvalidDataFormatException("Cotton part must be between 0 and 100");
        }
            

    }
}
