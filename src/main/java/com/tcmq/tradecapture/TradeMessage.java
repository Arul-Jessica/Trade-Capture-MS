package com.tcmq.tradecapture;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeMessage {

    @Schema(description = "Unique trade identifier", example = "T-1001", required = true)
    public String tradeId;

    @Schema(description = "Trade price", example = "250.75", required = true)
    public double price;

    @Schema(description = "Trade quantity", example = "100", required = true)
    public int quantity;

    @Schema(description = "Internal use only", accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String source;

    @Schema(description = "Auto-set by system", accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String timestamp;

    @Schema(description = "Decision made by rule/fraud engines", hidden = true)
    public String decision;

}
