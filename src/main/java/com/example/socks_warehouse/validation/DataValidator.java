package com.example.socks_warehouse.validation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.exception.InvalidDataFormatException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class DataValidator {

    public void checkAttributes(String color, Short cottonPart, Integer quantity){
        if(color != null) checkColor(color);
        if (cottonPart != null) checkCottonPart((int)cottonPart); 
        if (quantity != null && quantity <= 0)
            throw new InvalidDataFormatException("Quantity must be greater than 0");
    }

    public void checkFilters(String color, Integer cottonPart, Integer minCottonPart, Integer maxCottonPart){
        if(color != null) checkColor(color);
        if ((cottonPart != null) && (minCottonPart != null || maxCottonPart != null))
            throw new InvalidDataFormatException("Cannot use both exact and range-based filtering simultaneously");
        if(cottonPart != null){
            checkCottonPart(cottonPart);
            return;
        }
        if(minCottonPart != null) checkCottonPart(minCottonPart);
        if(maxCottonPart != null) checkCottonPart(maxCottonPart);
        if(minCottonPart != null && maxCottonPart != null && maxCottonPart <= minCottonPart)
            throw new InvalidDataFormatException("Min cotton part cannot be bigger than or equal to max cotton part");
    }

    public void checkId(Long id){
        if (id <= 0) throw new InvalidDataFormatException("Id must be greater than 0");
    }

    public void checkIfAllAttributesNull(SockDTO dto){
        if(dto.getColor() == null && dto.getCottonPart() == null && dto.getQuantity() == null)
            throw new InvalidDataFormatException("At least one attribute must be presented to update");
    }

    public void checkCells(Cell colorCell, Cell cottonPartCell, Cell quantityCell){
        if (colorCell == null || cottonPartCell == null || quantityCell == null)
            throw new NullPointerException("One of the cells is null");
        if (colorCell.getCellType() != CellType.STRING)
            throw new IllegalStateException("Color cell type is not STRING");
        if (cottonPartCell.getCellType() != CellType.NUMERIC)
            throw new IllegalStateException("Cotton part cell type is not NUMERIC");
        if (quantityCell.getCellType() != CellType.NUMERIC)
            throw new IllegalStateException("Quantity cell type is not NUMERIC");
    }

    private void checkCottonPart(Integer cottonPart){
        if (cottonPart < 0 || cottonPart > 100)
            throw new InvalidDataFormatException("Cotton part must be between 0 and 100");
    }

    private void checkColor(String color){
        try {
            Color.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataFormatException("Invalid color");
        }
    }
}
