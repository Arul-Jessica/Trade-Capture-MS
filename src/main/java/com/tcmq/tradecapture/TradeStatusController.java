package com.tcmq.tradecapture;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/trade")
public class TradeStatusController {

    private final SafeStoreRepo repo;

    public TradeStatusController(SafeStoreRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/status/{tradeId}")
    public TradeStatusResponse getStatus(@PathVariable String tradeId) {
        SafeStoreTrade trade = repo.findByTradeId(tradeId);
        if (trade == null) {
            throw new TradeNotFoundException("Trade not found: " + tradeId);
        }

        return new TradeStatusResponse(
                trade.tradeId,
                trade.status != null ? trade.status : "PENDING",
                trade.updatedAt != null ? trade.updatedAt.toString() : null
        );
    }

    @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
    public static class TradeNotFoundException extends RuntimeException {
        public TradeNotFoundException(String message) {
            super(message);
        }
    }

    public record TradeStatusResponse(String tradeId, String status, String lastUpdated) {}
}
