package com.tcmq.tradecapture;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class SafeStoreTrade {

    @Id
    public String tradeId;

    @Column(columnDefinition = "TEXT")
    public String payloadJson;

    public String status;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
