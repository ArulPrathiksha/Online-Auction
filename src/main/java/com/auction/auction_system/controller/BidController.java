package com.auction.auction_system.controller;

import com.auction.auction_system.entity.Bid;
import com.auction.auction_system.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/bids")
public class BidController {
    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<?> place(@RequestBody Map<String, Object> body) {
        Long auctionId = Long.valueOf(body.get("auctionId").toString());
        Long userId = Long.valueOf(body.get("userId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        try {
            Bid b = bidService.placeBid(auctionId, userId, amount);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
