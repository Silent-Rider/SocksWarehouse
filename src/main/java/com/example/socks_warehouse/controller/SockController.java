package com.example.socks_warehouse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.service.Filter;
import com.example.socks_warehouse.service.SockService;
import com.example.socks_warehouse.validation.DataValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("POST /income: Valid data received for adding socks - color={}, cottonPart={}, quantity={}", 
        color, cottonPart, quantity);
        SockDTO dto = SockDTO.builder()
                .color(color)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.addSocks(dto);
        log.info("POST /income: Socks have been successfully added");
        return ResponseEntity.ok("Socks have been successfully added");
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> removeSocks(@RequestParam String color, 
    @RequestParam short cottonPart, @RequestParam int quantity) {
        dataValidator.checkAttributes(color, cottonPart, quantity);
        log.info("POST /outcome: Valid data received for removing socks - color={}, cottonPart={}, quantity={}", 
        color, cottonPart, quantity);
        SockDTO dto = SockDTO.builder()
                .color(color)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
        sockService.removeSocks(dto);
        log.info("POST /outcome: Socks have been successfully removed");
        return ResponseEntity.ok("Socks have been successfully removed");
    }

    @GetMapping
    public ResponseEntity<Long> countSocks(@RequestParam(required = false) String color, 
    @RequestParam(required = false) Integer cottonPart, @RequestParam(required = false) Integer minCottonPart, 
    @RequestParam(required = false) Integer maxCottonPart) {
        dataValidator.checkFilters(color, cottonPart, minCottonPart, maxCottonPart);
        log.info("GET /: Valid filters received for getting socks count - color={}, cottonPart={}, minCottonPart={}, " +
        "maxCottonPart={}", color, cottonPart, minCottonPart, maxCottonPart);
        Filter filter = Filter.builder()
                .color(color.toUpperCase())
                .cottonPart(cottonPart)
                .minCottonPart(minCottonPart)
                .maxCottonPart(maxCottonPart)
                .build();
        long count = sockService.getSocksCount(filter);
        log.info("GET /: Socks count successfully retrieved from database and ready to be sent");
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSockDetails(@PathVariable Long id, @RequestBody SockDTO dto) {
        dataValidator.checkIfAllAttributesNull(dto);
        dataValidator.checkAttributes(dto.getColor(), dto.getCottonPart(), dto.getQuantity());
        dataValidator.checkId(id);
        log.info("PUT /{}: Valid data received for updating socks - color={}, cottonPart={}, quantity={}", 
        id, dto.getColor(), dto.getCottonPart(), dto.getQuantity());
        sockService.updateSock(id, dto);
        log.info("PUT /{}: Socks have been successfully updated", id);
        return ResponseEntity.ok("Socks have been successfully updated");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> addSocksBatch(@RequestParam MultipartFile file) {
        sockService.addSocksBatchFromExcel(file);
        log.info("POST /batch: Socks batch have been successfully added");
        return ResponseEntity.ok("Socks batch have been successfully added");
    }
}
