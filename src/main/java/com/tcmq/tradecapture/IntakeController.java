package com.tcmq.tradecapture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/trade")
public class IntakeController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SafeStoreRepo repo;
    private final MqSender mqSender;
    private final SafeStoreValidator validator;

    public IntakeController(SafeStoreRepo repo, MqSender mqSender, SafeStoreValidator validator) {
        this.repo = repo;
        this.mqSender = mqSender;
        this.validator = validator;
    }

    @PostMapping("/capture")
    public String captureTrade(@RequestBody TradeMessage msg) {
        try {
            validator.validate(msg);

            String payloadJson = mapper.writeValueAsString(msg);

            SafeStoreTrade trade = new SafeStoreTrade();
            trade.tradeId = msg.tradeId;
            trade.payloadJson = payloadJson;
            trade.status = "NEW";
            trade.createdAt = LocalDateTime.now();
            trade.updatedAt = LocalDateTime.now();
            repo.save(trade);

            System.out.println("Stored trade in SafeStore: " + msg.tradeId);

            msg.source = "INTAKE";
            msg.timestamp = LocalDateTime.now().toString();
            mqSender.send(msg);

            return "Trade captured successfully with ID: " + msg.tradeId;

        } catch (IllegalArgumentException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return "Rejected: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Capture failed: " + e.getMessage());
            return "Internal Error: " + e.getMessage();
        }
    }
}
