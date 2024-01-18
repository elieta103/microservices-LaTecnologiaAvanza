package com.elhg.orden.exceptions;

public class ItemInventoryNotFoundException extends RuntimeException{

    public ItemInventoryNotFoundException() {
        super("Item no encontrado en stock !!!");
    }

    public ItemInventoryNotFoundException(String message) {
        super(message);
    }
}
