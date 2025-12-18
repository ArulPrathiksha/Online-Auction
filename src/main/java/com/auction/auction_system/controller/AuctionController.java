package com.auction.auction_system.controller;

import com.auction.auction_system.dto.AuctionRequest;
import com.auction.auction_system.dto.AuctionResponse;
import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping("/upcoming")
    public List<AuctionResponse> upcoming() {
        return auctionService.getUpcoming().stream()
                .map(this::toResp)
                .collect(Collectors.toList());
    }

    @GetMapping("/live")
    public List<AuctionResponse> live() {
        return auctionService.getLive().stream().map(this::toResp).collect(Collectors.toList());
    }

    @GetMapping("/ended")
    public List<AuctionResponse> ended() {
        return auctionService.getEnded().stream().map(this::toResp).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AuctionRequest req) {
        Auction a = auctionService.createAuction(req);
        return ResponseEntity.ok(toResp(a));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            Auction a = auctionService.getById(id);
            return ResponseEntity.ok(toResp(a));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private AuctionResponse toResp(Auction a) {
        return new AuctionResponse(a.getId(), a.getProductName(), a.getDescription(), a.getReservePrice(),
                a.getStartTime(), a.getEndTime(), a.getStatus(), a.getCurrentHighestBid());
    }
}