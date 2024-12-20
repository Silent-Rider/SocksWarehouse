package com.example.socks_warehouse.service;

import com.example.socks_warehouse.common.Operator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
    
    private String color;
    private Operator operator;
    private Integer cottonPart;
}
