package com.elhg.producto.service;

import com.elhg.producto.dto.ProductoRequest;
import com.elhg.producto.dto.ProductoResponse;
import com.elhg.producto.model.Producto;
import com.elhg.producto.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public void createProducto(ProductoRequest productoRequest){
        Producto producto = Producto.builder()
                .nombre(productoRequest.getNombre())
                .descripcion(productoRequest.getDescripcion())
                .precio(productoRequest.getPrecio())
                .build();
        productoRepository.save(producto);
        log.info("Producto{} ha sido guardado con exito",producto.getId());
    }

    public List<ProductoResponse> getAllProductos(){
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(this::mapToProductoResponse).collect(Collectors.toList());
    }

    private ProductoResponse mapToProductoResponse(Producto producto){
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .build();
    }
}
