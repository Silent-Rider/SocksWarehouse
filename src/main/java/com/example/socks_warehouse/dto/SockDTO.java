package com.example.socks_warehouse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SockDTO {
    
    @JsonIgnore
    private Long id;
    private String color;
    private Short cottonPart;
    private Integer quantity;
}
