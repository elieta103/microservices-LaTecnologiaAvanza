package com.elhg.orden.controller;

import com.elhg.orden.dto.OrderRequest;
import com.elhg.orden.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String realizarPedido(@RequestBody OrderRequest orderRequest)throws IllegalAccessException{
        orderService.placeOrder(orderRequest);
        return "Pedido realizado con éxito";
    }

}
