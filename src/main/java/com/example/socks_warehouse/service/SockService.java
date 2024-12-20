package com.example.socks_warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Sock sock = sockRepository.findByColorAndCottonPart(dto.getColor(), dto.getCottonPart())
                .orElse(new Sock(dto.getColor(), dto.getCottonPart(), 0));
        sock.setQuantity(sock.getQuantity() + dto.getQuantity());
        sockRepository.save(sock);
    }

    public void registerOutcome(SockDTO dto) {
        Sock sock = sockRepository.findByColorAndCottonPart(dto.getColor(), dto.getCottonPart())
            .orElseThrow(() -> new SocksNotFoundException ("No socks found matching the specified params"));
        if (sock.getQuantity() < dto.getQuantity())
            throw new InsufficientSocksException("Not enough socks in stock");
        sock.setQuantity(sock.getQuantity() - dto.getQuantity());
        sockRepository.save(sock);
    }

    public int getCount(Filter filter){
        return 0;
    }
}
