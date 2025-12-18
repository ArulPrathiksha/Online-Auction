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

        List<Auction> upcoming = auctionRepository.findByStatus(AuctionStatus.UPCOMING);
        for (Auction a : upcoming) {
            if (!a.getStartTime().isAfter(now)) {
                auctionService.markLive(a);
            }
        }

        List<Auction> live = auctionRepository.findByStatus(AuctionStatus.LIVE);
        for (Auction a : live) {
            if (!a.getEndTime().isAfter(now)) {
                auctionService.markEnded(a);
            }
        }
    }
}

