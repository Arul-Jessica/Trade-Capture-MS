package com.tcmq.tradecapture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class FinalDecisionListener {

    private final SafeStoreRepo repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public FinalDecisionListener(SafeStoreRepo repo) {
        this.repo = repo;
    }

    @JmsListener(destination = "final_results")
    public void handle(String json) throws Exception {
        System.out.println("RAW MESSAGE FROM MQ: " + json);

        TradeMessage msg = mapper.readValue(json, TradeMessage.class);
        System.out.println("Parsed decision=" + msg.decision + ", tradeId=" + msg.tradeId);

        SafeStoreTrade trade = repo.findByTradeId(msg.tradeId);
        if (trade == null) {
            System.err.println("Trade not found in SafeStore for " + msg.tradeId);
            return;
        }

        trade.status = msg.decision;
        trade.updatedAt = LocalDateTime.now();
        repo.save(trade);

        System.out.println("Updated status in DB to " + trade.status);
    }

}
