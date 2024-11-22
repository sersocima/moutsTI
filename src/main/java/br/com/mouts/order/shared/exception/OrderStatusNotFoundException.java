package br.com.mouts.order.shared.exception;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(String value) {
        super("Status '" + value + "' não encontrado");
    }
}
