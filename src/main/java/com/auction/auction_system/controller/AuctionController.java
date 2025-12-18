package com.auction.auction_system.controller;

import com.auction.auction_system.dto.AuctionRequest;
import com.auction.auction_system.dto.AuctionResponse;
import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionStatus;
import com.auction.auction_system.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    // Endpoint to create a new auction
    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> createAuction(@Valid @RequestBody AuctionRequest auctionRequest) {
        Auction auction = auctionService.createAuction(
                auctionRequest.getProductName(),
                auctionRequest.getDescription(),
                auctionRequest.getReservePrice(),
                auctionRequest.getTimeSlotId(),
                auctionRequest.getAuctionDate()
        );
        
        AuctionResponse auctionResponse = new AuctionResponse(
                auction.getId(),
                auction.getProductName(),
                auction.getDescription(),
                auction.getReservePrice(),
                auction.getStartTime(),
                auction.getEndTime(),
                auction.getStatus(),
                auction.getCurrentHighestBid()
        );

        return ResponseEntity.ok(auctionResponse);
    }

    // Endpoint to fetch all upcoming auctions
    @GetMapping("/upcoming")
    public List<AuctionResponse> getUpcomingAuctions() {
        List<Auction> upcomingAuctions = auctionService.getUpcoming();
        return upcomingAuctions.stream()
                .map(auction -> new AuctionResponse(
                        auction.getId(),
                        auction.getProductName(),
                        auction.getDescription(),
                        auction.getReservePrice(),
                        auction.getStartTime(),
                        auction.getEndTime(),
                        auction.getStatus(),
                        auction.getCurrentHighestBid()
                ))
                .collect(Collectors.toList());
    }

    // Endpoint to fetch all live auctions
    @GetMapping("/live")
    public List<AuctionResponse> getLiveAuctions() {
        List<Auction> liveAuctions = auctionService.getLive();
        return liveAuctions.stream()
                .map(auction -> new AuctionResponse(
                        auction.getId(),
                        auction.getProductName(),
                        auction.getDescription(),
                        auction.getReservePrice(),
                        auction.getStartTime(),
                        auction.getEndTime(),
                        auction.getStatus(),
                        auction.getCurrentHighestBid()
                ))
                .collect(Collectors.toList());
    }

    // Endpoint to fetch all ended auctions
    @GetMapping("/ended")
    public List<AuctionResponse> getEndedAuctions() {
        List<Auction> endedAuctions = auctionService.getEnded();
        return endedAuctions.stream()
                .map(auction -> new AuctionResponse(
                        auction.getId(),
                        auction.getProductName(),
                        auction.getDescription(),
                        auction.getReservePrice(),
                        auction.getStartTime(),
                        auction.getEndTime(),
                        auction.getStatus(),
                        auction.getCurrentHighestBid()
                ))
                .collect(Collectors.toList());
    }

    // Endpoint to get auction details by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable Long id) {
        try {
            Auction auction = auctionService.getById(id);
            AuctionResponse auctionResponse = new AuctionResponse(
                    auction.getId(),
                    auction.getProductName(),
                    auction.getDescription(),
                    auction.getReservePrice(),
                    auction.getStartTime(),
                    auction.getEndTime(),
                    auction.getStatus(),
                    auction.getCurrentHighestBid()
            );
            return ResponseEntity.ok(auctionResponse);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to mark auction as live
    @PostMapping("/{id}/mark-live")
    public ResponseEntity<String> markAuctionLive(@PathVariable Long id) {
        try {
            Auction auction = auctionService.getById(id);
            auctionService.markLive(auction);
            return ResponseEntity.ok("Auction marked as live successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Auction not found.");
        }
    }

    // Endpoint to mark auction as ended
    @PostMapping("/{id}/mark-ended")
    public ResponseEntity<String> markAuctionEnded(@PathVariable Long id) {
        try {
            Auction auction = auctionService.getById(id);
            auctionService.markEnded(auction);
            return ResponseEntity.ok("Auction marked as ended successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Auction not found.");
        }
    }
}
