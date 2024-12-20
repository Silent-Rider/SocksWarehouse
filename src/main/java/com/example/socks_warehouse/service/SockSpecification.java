package com.example.socks_warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.socks_warehouse.common.Operator;
import com.example.socks_warehouse.exception.InvalidDataFormatException;
import com.example.socks_warehouse.model.Sock;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SockSpecification implements Specification<Sock>{

    private final Filter filter;
    private Root<Sock> root;
    private CriteriaBuilder builder;

    @Override
    public Predicate toPredicate(Root<Sock> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.builder = builder;
        List<Predicate> predicates = new ArrayList<>();
        String color = filter.getColor();
        Operator operator = filter.getOperator();
        Integer cottonPart = filter.getCottonPart();
        if(color != null) 
            predicates.add(colorCriteria(color));
        if(operator != null && cottonPart != null)
            predicates.add(cottonPartCriteria(operator, cottonPart));
        return builder.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate colorCriteria(String color){
        return builder.equal(root.get("color"), color);
    }

    private Predicate cottonPartCriteria(Operator operator, Integer cottonPart){
        return switch(operator){
            case MORETHAN -> builder.greaterThan(root.get("cotton_part"), cottonPart);
            case LESSTHAN -> builder.lessThan(root.get("cotton_part"), cottonPart);
            case EQUAL -> builder.equal(root.get("cotton_part"), cottonPart);
            default -> throw new InvalidDataFormatException("Invalid operator");
        };
    }



}
