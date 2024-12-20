package com.example.socks_warehouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.service.Filter;
import com.example.socks_warehouse.service.SockService;



@RestController
@RequestMapping("/api/socks")
public class SockController {

    private final SockService sockService;
    private final DataValidator dataValidator;

    public SockController(SockService sockService, DataValidator dataValidator) {
        this.sockService = sockService;
        this.dataValidator = dataValidator;
    }

    @PostMapping("/income")
    public ResponseEntity<String> registerIncome(@RequestParam String color, 
    @RequestParam short cottonPart, @RequestParam int quantity) {
        dataValidator.checkAttributes(color, cottonPart, quantity);
        Color verifiedColor = Color.valueOf(color.toUpperCase());
        SockDTO dto = SockDTO.builder()
                .color(verifiedColor)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.registerIncome(dto);
        return ResponseEntity.ok("Income registered");
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> registerOutcome(@RequestParam String color, 
    @RequestParam short cottonPart, @RequestParam int quantity) {
        dataValidator.checkAttributes(color, cottonPart, quantity);
        Color verifiedColor = Color.valueOf(color.toUpperCase());
        SockDTO dto = SockDTO.builder()
                .color(verifiedColor)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.registerIncome(dto);
        return ResponseEntity.ok("Outcome registered");
    }

    @GetMapping
    public ResponseEntity<Integer> getMethodName(@RequestParam(required = false) String color, 
    @RequestParam(required = false) String operator, @RequestParam(required = false) Integer cottonPart) {
        dataValidator.checkFilters(color, operator, cottonPart);
        Color verifiedColor = Color.valueOf(color.toUpperCase());
        Filter filter = Filter.builder()
                .color(verifiedColor)
                .operator(operator)
                .cottonPart(cottonPart)
                .build();
        int count = sockService.getCount(filter);
        return ResponseEntity.ok(Integer.valueOf(count));
    }
    

    
}
