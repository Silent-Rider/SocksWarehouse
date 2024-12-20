package com.example.socks_warehouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.socks_warehouse.common.Operator;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.service.Filter;
import com.example.socks_warehouse.service.SockService;
import com.example.socks_warehouse.validation.DataValidator;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




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
    public ResponseEntity<String> addSocks(@RequestParam String color, 
    @RequestParam short cottonPart, @RequestParam int quantity) {
        dataValidator.checkAttributes(color, cottonPart, quantity);
        SockDTO dto = SockDTO.builder()
                .color(color)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.addSocks(dto);
        return ResponseEntity.ok("Socks have been successfully added");
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> removeSocks(@RequestParam String color, 
    @RequestParam short cottonPart, @RequestParam int quantity) {
        dataValidator.checkAttributes(color, cottonPart, quantity);
        SockDTO dto = SockDTO.builder()
                .color(color)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.removeSocks(dto);
        return ResponseEntity.ok("Socks have been successfully removed");
    }

    @GetMapping
    public ResponseEntity<Long> countSocks(@RequestParam(required = false) String color, 
    @RequestParam(required = false) String operator, @RequestParam(required = false) Integer cottonPart) {
        dataValidator.checkFilters(color, operator, cottonPart);
        Operator verifiedOperator = Operator.valueOf(operator.toUpperCase());
        Filter filter = Filter.builder()
                .color(color.toUpperCase())
                .operator(verifiedOperator)
                .cottonPart(cottonPart)
                .build();
        long count = sockService.getSocksCount(filter);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSockDetails(@PathVariable Long id, @RequestBody SockDTO dto) {
        dataValidator.checkIfAllAttributesNull(dto);
        dataValidator.checkAttributes(dto.getColor(), dto.getCottonPart(), dto.getQuantity());
        dataValidator.checkId(id);
        sockService.updateSock(id, dto);
        return ResponseEntity.ok("Socks have been successfully updated");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> addSocksBatch(@RequestParam("file") MultipartFile file) {
        sockService.addSocksBatchFromExcel(file);
        return ResponseEntity.ok("Socks batch uploaded successfully.");
    }
    
    

    
}
