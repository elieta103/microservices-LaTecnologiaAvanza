package com.elhg.inventario.service;


import com.elhg.inventario.dto.InventarioResponse;
import com.elhg.inventario.model.Inventario;
import com.elhg.inventario.repository.InventarioRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Transactional(readOnly = true)
    @SneakyThrows //@SneakyThrows can be used to sneakily throw checked exceptions without actually declaring this in your method's throws clause
    public  List<InventarioResponse>  isInStock(List<String> codigoSku){
        log.info("Espera iniciada ....");
        Thread.sleep(10000);
        log.info("Espera finalizada ...");
        List<InventarioResponse> listInventario = inventarioRepository.findByCodigoSkuIn(codigoSku).stream()
                .map(item -> InventarioResponse.builder()
                        .codigoSku(item.getCodigoSku())
                        .inStock(item.getCantidad()>0)
                        .build())
                .collect(Collectors.toList());
        return  listInventario;
    }
}
