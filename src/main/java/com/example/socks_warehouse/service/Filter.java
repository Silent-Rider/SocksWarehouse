package com.example.socks_warehouse.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter {
    
    private String color;
    private Integer cottonPart; // for exact match
    private Integer minCottonPart;
    private Integer maxCottonPart;
}
