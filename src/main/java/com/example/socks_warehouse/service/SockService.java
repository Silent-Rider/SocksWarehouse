package com.example.socks_warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.socks_warehouse.common.Color;
import com.example.socks_warehouse.dto.SockDTO;
import com.example.socks_warehouse.exception.InsufficientSocksException;
import com.example.socks_warehouse.exception.SocksNotFoundException;
import com.example.socks_warehouse.model.Sock;
import com.example.socks_warehouse.repository.SockRepository;

@Service
public class SockService {

    @Autowired
    private SockRepository sockRepository;

    public void registerIncome(SockDTO dto) {
        Color color = Color.valueOf(dto.getColor().toUpperCase());
        Sock sock = sockRepository.findByColorAndCottonPart(color, dto.getCottonPart())
                .orElse(new Sock(color, dto.getCottonPart(), 0));
        sock.setQuantity(sock.getQuantity() + dto.getQuantity());
        sockRepository.save(sock);
    }

    public void registerOutcome(SockDTO dto) {
        Color color = Color.valueOf(dto.getColor().toUpperCase());
        Sock sock = sockRepository.findByColorAndCottonPart(color, dto.getCottonPart())
            .orElseThrow(() -> new SocksNotFoundException ("No socks found matching the specified params"));
        if (sock.getQuantity() < dto.getQuantity())
            throw new InsufficientSocksException("Not enough socks in stock");
        sock.setQuantity(sock.getQuantity() - dto.getQuantity());
        sockRepository.save(sock);
    }

    public long getCount(Filter filter){
        SockSpecification spec = new SockSpecification(filter);
        return sockRepository.count(spec);
    }

    public void update(Long id, SockDTO dto){
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
}
