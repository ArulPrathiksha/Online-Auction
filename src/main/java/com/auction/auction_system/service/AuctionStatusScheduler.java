package com.auction.auction_system.service;

import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionStatus;
import com.auction.auction_system.repository.AuctionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionStatusScheduler {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    public AuctionStatusScheduler(AuctionRepository auctionRepository, AuctionService auctionService) {
        this.auctionRepository = auctionRepository;
        this.auctionService = auctionService;
    }

    // every 5 seconds (for dev/demo). Adjust interval as needed.
    @Scheduled(fixedDelay = 5000)
    public void checkStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Check for auctions that should become LIVE
        List<Auction> upcoming = auctionRepository.findByStatus(AuctionStatus.UPCOMING);
        for (Auction auction : upcoming) {
            if (!auction.getStartTime().isAfter(now)) {
                auctionService.markLive(auction);  // Transition to LIVE
            }
        }

        // Check for auctions that should be ENDED
        List<Auction> live = auctionRepository.findByStatus(AuctionStatus.LIVE);
        for (Auction auction : live) {
            if (!auction.getEndTime().isAfter(now)) {
                auctionService.markEnded(auction);  // Transition to ENDED
            }
        }
    }
}
