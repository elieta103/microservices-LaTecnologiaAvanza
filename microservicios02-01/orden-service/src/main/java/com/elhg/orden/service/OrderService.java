package com.elhg.orden.service;


import com.elhg.orden.dto.InventarioResponse;
import com.elhg.orden.dto.OrderLineItemsDto;
import com.elhg.orden.dto.OrderRequest;
import com.elhg.orden.exceptions.ItemInventoryNotFoundException;
import com.elhg.orden.model.Order;
import com.elhg.orden.model.OrderLineItems;
import com.elhg.orden.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
        Order order = new Order();
        order.setNumeroPedido(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);

        // Validar existencia de articulos en inventario
        // Obtener lista de codigos sku
        List<String> codigoSku = orderLineItems.stream()
                        .map(OrderLineItems::getCodigoSku)
                        .collect(Collectors.toList());
        log.info("Lista de codigos Sku : {} ",codigoSku);

        //Llamada con Webclient a inventario-service
        InventarioResponse[] inventarioResponseArray = webClientBuilder.build().get()
                        .uri("http://inventario-service/api/inventario", uriBuilder -> uriBuilder.queryParam("codigoSku", codigoSku).build())
                        .retrieve().bodyToMono(InventarioResponse[].class)
                        .block();

        // Todos los articulos estan en Stock, devuelven true
        boolean allProductsInStock = Arrays.stream(inventarioResponseArray)
                        .allMatch(InventarioResponse::isInStock);

        // Existen todos los articulos, guarda y genera la orden
        if (allProductsInStock){
            orderRepository.save(order);
        }else{
            throw new ItemInventoryNotFoundException("El producto no esta en stock.");
        }


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrecio(orderLineItemsDto.getPrecio());
        orderLineItems.setCantidad(orderLineItemsDto.getCantidad());
        orderLineItems.setCodigoSku(orderLineItemsDto.getCodigoSku());
        return orderLineItems;
    }
}
