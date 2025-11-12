package com.tcmq.tradecapture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecoveryDaemon {

    private final SafeStoreRepo repo;
    private final MqSender sender;
    private final ObjectMapper mapper = new ObjectMapper();


    public RecoveryDaemon(SafeStoreRepo repo, MqSender sender) {
        this.repo = repo;
        this.sender = sender;
    }

    @Scheduled(fixedRate = 30000)
    public void retryStuckTrades() throws Exception {

        var stuckTrades = repo.findAll()
                .stream()
                .filter(t -> "NEW".equals(t.status))
                .toList();

        for (SafeStoreTrade t : stuckTrades) {
            System.out.println("RETRYING : " + t.tradeId);

            TradeMessage msg = mapper.readValue(t.payloadJson, TradeMessage.class);

            sender.send(msg);

            t.status = "SENT";
            t.updatedAt = java.time.LocalDateTime.now();
            repo.save(t);
        }
    }
}
