package com.example.socks_warehouse.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.exception.FileProcessingException;
import com.example.socks_warehouse.exception.InsufficientSocksException;
import com.example.socks_warehouse.exception.SocksNotFoundException;
import com.example.socks_warehouse.model.Sock;
import com.example.socks_warehouse.repository.SockRepository;

@Service
public class SockService {

    @Autowired
    private SockRepository sockRepository;

    public void addSocks(SockDTO dto) {
        Color color = Color.valueOf(dto.getColor().toUpperCase());
        Sock sock = sockRepository.findByColorAndCottonPart(color, dto.getCottonPart())
                .orElse(new Sock(color, dto.getCottonPart(), 0));
        sock.setQuantity(sock.getQuantity() + dto.getQuantity());
        sockRepository.save(sock);
    }

    public void removeSocks(SockDTO dto) {
        Color color = Color.valueOf(dto.getColor().toUpperCase());
        Sock sock = sockRepository.findByColorAndCottonPart(color, dto.getCottonPart())
            .orElseThrow(() -> new SocksNotFoundException ("No socks found matching the specified params"));
        if (sock.getQuantity() < dto.getQuantity())
            throw new InsufficientSocksException("Not enough socks in stock");
        sock.setQuantity(sock.getQuantity() - dto.getQuantity());
        sockRepository.save(sock);
    }

    public long getSocksCount(Filter filter){
        SockSpecification spec = new SockSpecification(filter);
        return sockRepository.count(spec);
    }

    public void updateSock(Long id, SockDTO dto){
        Sock sock = sockRepository.findById(id)
            .orElseThrow(() -> new SocksNotFoundException ("No socks found matching the specified id"));
        if(dto.getColor() != null){
            Color color = Color.valueOf(dto.getColor().toUpperCase());
            sock.setColor(color);
        }
        if(dto.getCottonPart() != null) sock.setCottonPart(dto.getCottonPart());
        if(dto.getQuantity() != null) sock.setQuantity(dto.getQuantity());
        sockRepository.save(sock);
    }

    public void saveSocksFromExcel(MultipartFile file) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // пропускаем заголовок

                String color = row.getCell(0).getStringCellValue();
                int cottonPart = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();
            }
        } catch (IOException e){
            throw new FileProcessingException("XML file processing error", e);
        }
    }
}
