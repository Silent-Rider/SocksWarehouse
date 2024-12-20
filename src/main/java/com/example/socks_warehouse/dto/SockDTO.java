package com.example.socks_warehouse.dto;

import com.example.socks_warehouse.common.Color;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SockDTO {
    
    private Long id;
    private Color color;
    private short cottonPart;
    private int quantity;
}
