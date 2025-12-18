package com.auction.auction_system.service;

import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.Bid;
import com.auction.auction_system.entity.Participant;
import com.auction.auction_system.repository.AuctionRepository;
import com.auction.auction_system.repository.BidRepository;
import com.auction.auction_system.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BidService {

    private final BidRepository bidRepo;
    private final AuctionRepository auctionRepo;
    private final ParticipantRepository participantRepo;

    public BidService(BidRepository bidRepo, AuctionRepository auctionRepo, ParticipantRepository participantRepo) {
        this.bidRepo = bidRepo;
        this.auctionRepo = auctionRepo;
        this.participantRepo = participantRepo;
    }

    @Transactional
    public Bid placeBid(Long auctionId, Long userId, BigDecimal amount) {
        Auction a = auctionRepo.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        if (a.getStatus() != com.auction.auction_system.entity.AuctionStatus.LIVE) {
            throw new IllegalStateException("Auction not live");
        }

        Optional<Participant> p = participantRepo.findByAuctionIdAndUserId(auctionId, userId);
        if (p.isEmpty()) throw new IllegalStateException("User not registered for auction");

        BigDecimal current = a.getCurrentHighestBid();
        if (current == null) current = a.getReservePrice();

        if (amount.compareTo(current) <= 0)
            throw new IllegalArgumentException("Bid must be higher than current highest bid");

        Bid bid = new Bid();
        bid.setAuctionId(auctionId);
        bid.setUserId(userId);
        bid.setAmount(amount);
        bid.setPlacedAt(LocalDateTime.now());
        Bid saved = bidRepo.save(bid);

        // update auction highest bid (optimistic locking will help)
        a.setCurrentHighestBid(amount);

        // anti-sniping: extend auction by 30 sec if bid in last 10s
        long secondsLeft = java.time.Duration.between(LocalDateTime.now(), a.getEndTime()).getSeconds();
        if (secondsLeft >= 0 && secondsLeft <= 10) {
            a.setEndTime(a.getEndTime().plusSeconds(30));
        }

        auctionRepo.save(a);

        // TODO: publish websocket notification (later)
        return saved;
    }
}
