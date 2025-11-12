package com.tcmq.tradecapture;

import org.springframework.stereotype.Component;

@Component
public class SafeStoreValidator {

    public void validate(TradeMessage msg) {
        if (msg == null)
            throw new IllegalArgumentException("Payload is empty or invalid JSON");

        if (msg.tradeId == null || msg.tradeId.isBlank())
            throw new IllegalArgumentException("Trade ID missing");

        if (msg.price <= 0)
            throw new IllegalArgumentException("Invalid price: " + msg.price);

        if (msg.quantity <= 0)
            throw new IllegalArgumentException("Invalid quantity: " + msg.quantity);
    }
}
