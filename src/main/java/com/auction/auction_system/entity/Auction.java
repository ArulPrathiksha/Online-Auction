package com.auction.auction_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String productName;

    @Column(length = 2000)
    private String description;

    // store in smallest meaningful unit or use BigDecimal for accuracy
    private BigDecimal reservePrice;

    // TODO : Uncomment this later....
    // @Column(nullable = false)
    // private OffsetDateTime startTime;

    // @Column(nullable = false)
    // private OffsetDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    // cached current highest bid amount (not sole source of truth)
    private BigDecimal currentHighestBid;

    @Version
    private Long version;

    // getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    /*
     * TODO : Uncomment it later...
     * public OffsetDateTime getStartTime() {
     * return startTime;
     * }
     * 
     * public void setStartTime(OffsetDateTime startTime) {
     * this.startTime = startTime;
     * }
     * 
     * public OffsetDateTime getEndTime() {
     * return endTime;
     * }
     * 
     * public void setEndTime(OffsetDateTime endTime) {
     * this.endTime = endTime;
     * }
     * 
     */

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public BigDecimal getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(BigDecimal currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}