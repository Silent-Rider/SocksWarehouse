package com.example.socks_warehouse.service;

import com.example.socks_warehouse.common.Color;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
    
    private Color color;
    private String operator;
    private Integer cottonPart;
}
