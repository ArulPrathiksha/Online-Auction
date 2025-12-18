package com.auction.auction_system.dto;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AuctionRequest {
    @NotNull
    private String productName;
    private String description;
    @NotNull
    private BigDecimal reservePrice;
    @NotNull
    private Long timeSlotId;

    // getters/setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}

