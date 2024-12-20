package com.example.socks_warehouse.controller;

import org.springframework.stereotype.Component;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.common.Operator;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.exception.InvalidDataFormatException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class DataValidator {

    void checkAttributes(String color, Short cottonPart, Integer quantity){
        try {
            if(color != null)
                Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Invalid color");
        }
        if ((cottonPart != null) && (cottonPart < 0 || cottonPart > 100))
            throw new InvalidDataFormatException("Cotton part must be between 0 and 100");
        if (quantity != null && quantity <= 0)
            throw new InvalidDataFormatException("Quantity must be greater than 0");
    }

    void checkFilters(String color, String operator, Integer cottonPart){
        try {
            if(color != null)
                Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Invalid color");
        }
        if (cottonPart != null && operator == null)
            throw new InvalidDataFormatException("Operator is required when cottonPart is provided.");
        else if (cottonPart == null && operator != null)
            throw new InvalidDataFormatException("CottonPart is required when operator is provided.");
        else if(cottonPart != null && operator != null){
            try {
                Operator.valueOf(operator.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidDataFormatException("Invalid operator");
            }
            if (cottonPart < 0 || cottonPart > 100)
                throw new InvalidDataFormatException("Cotton part must be between 0 and 100");
        }
    }

    void checkId(Long id){
        if (id <= 0) throw new InvalidDataFormatException("Id must be greater than 0");
    }

    void checkIfAllAttributesNull(SockDTO dto){
        if(dto.getColor() == null && dto.getCottonPart() == null && dto.getQuantity() == null)
            throw new InvalidDataFormatException("At least one attribute must be presented to update");
    }
}
