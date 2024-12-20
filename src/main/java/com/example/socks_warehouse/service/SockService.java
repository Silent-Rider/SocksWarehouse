package com.example.socks_warehouse.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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
import com.example.socks_warehouse.validation.DataValidator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SockService {

    @Autowired
    private SockRepository sockRepository;
    @Autowired
    private DataValidator dataValidator;
    @PersistenceContext
    private EntityManager entityManager;

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
        log.info("POST /outcome: Found socks matching specified params");
        if (sock.getQuantity() < dto.getQuantity())
            throw new InsufficientSocksException("Not enough socks in stock");
        log.info("POST /outcome: Enough socks quantity to remove");
        sock.setQuantity(sock.getQuantity() - dto.getQuantity());
        sockRepository.save(sock);
    }

    public long getSocksCount(Filter filter){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Sock> root = query.from(Sock.class);
        SockSpecification spec = new SockSpecification(filter, builder, query, root);
        query = spec.toSumQuery();
        return entityManager.createQuery(query).getSingleResult();
    }

    public void updateSock(Long id, SockDTO dto){
        Sock sock = sockRepository.findById(id)
            .orElseThrow(() -> new SocksNotFoundException ("No socks found matching the specified id"));
        log.info("PUT /{}: Found socks matching specified id", id);
        if(dto.getColor() != null){
            Color color = Color.valueOf(dto.getColor().toUpperCase());
            sock.setColor(color);
        }
        if(dto.getCottonPart() != null) sock.setCottonPart(dto.getCottonPart());
        if(dto.getQuantity() != null) sock.setQuantity(dto.getQuantity());
        sockRepository.save(sock);
    }

    public void addSocksBatchFromExcel(MultipartFile file) {
        List<SockDTO> batch = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip heading
                if (row.getPhysicalNumberOfCells() == 0) continue;
                
                Cell colorCell = row.getCell(0);
                Cell cottonPartCell = row.getCell(1);
                Cell quantityCell = row.getCell(2);
                dataValidator.checkCells(colorCell, cottonPartCell, quantityCell);
                log.info("POST /batch: Valid row of cells received from XML file");
                
                String color = colorCell.getStringCellValue();
                short cottonPart = (short) cottonPartCell.getNumericCellValue();
                int quantity = (int) quantityCell.getNumericCellValue();
                dataValidator.checkAttributes(color, cottonPart, quantity);
                log.info("POST /batch: Valid attribute set received from XML file " +
                "for adding socks - color={}, cottonPart={}, quantity={}", color, cottonPart, quantity);

                SockDTO dto = SockDTO.builder()
                .color(color)
                .cottonPart(cottonPart)
                .quantity(quantity)
                .build();
                batch.add(dto);
            }
            addSocksBatch(batch);
        } catch (IOException e){
            throw new FileProcessingException("XML file processing error");
        } catch(IllegalArgumentException e){
            throw new FileProcessingException("No sheet in XML file");
        } catch(NullPointerException | IllegalStateException e){
            throw new FileProcessingException(e.getMessage());
        }
    }

    @Transactional
    private void addSocksBatch(List<SockDTO> batch){
        batch.forEach(dto -> addSocks(dto));
    }
}
