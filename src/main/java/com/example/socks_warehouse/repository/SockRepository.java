package com.example.socks_warehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.model.Sock;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long>, JpaSpecificationExecutor<Sock> {
    
    Optional<Sock> findByColorAndCottonPart(Color color, int cottonPart);
}
