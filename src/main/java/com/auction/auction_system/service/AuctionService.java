package com.auction.auction_system.service;

import com.auction.auction_system.dto.AuctionRequest;
import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionStatus;
import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.repository.AuctionRepository;
import com.auction.auction_system.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final TimeSlotRepository timeSlotRepository;

    public AuctionService(AuctionRepository auctionRepository, TimeSlotRepository timeSlotRepository) {
        this.auctionRepository = auctionRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public Auction createAuction(AuctionRequest req) {
        TimeSlot slot = timeSlotRepository.findById(req.getTimeSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (slot.isBooked()) throw new IllegalStateException("Slot already booked");

        // Check overlapping auctions again
        boolean overlap = auctionRepository.existsOverlapping(slot.getStartTime(), slot.getEndTime());
        if (overlap) throw new IllegalStateException("Another auction overlaps this slot");

        Auction a = new Auction();
        a.setProductName(req.getProductName());
        a.setDescription(req.getDescription());
        a.setReservePrice(req.getReservePrice());
        a.setStartTime(slot.getStartTime());
        a.setEndTime(slot.getEndTime());
        a.setStatus(AuctionStatus.UPCOMING);
        a.setCurrentHighestBid(req.getReservePrice());

        Auction saved = auctionRepository.save(a);

        slot.setBooked(true);
        slot.setAuctionId(saved.getId());
        timeSlotRepository.save(slot);

        return saved;
    }

    public List<Auction> getUpcoming() {
        return auctionRepository.findByStatus(AuctionStatus.UPCOMING);
    }

    public List<Auction> getLive() {
        return auctionRepository.findByStatus(AuctionStatus.LIVE);
    }

    public List<Auction> getEnded() {
        return auctionRepository.findByStatus(AuctionStatus.ENDED);
    }

    public Auction getById(Long id) {
        return auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Auction not found"));
    }

    @Transactional
    public void markLive(Auction a) {
        if (a.getStatus() == AuctionStatus.UPCOMING) {
            a.setStatus(AuctionStatus.LIVE);
            auctionRepository.save(a);
        }
    }

    @Transactional
    public void markEnded(Auction a) {
        if (a.getStatus() == AuctionStatus.LIVE) {
            a.setStatus(AuctionStatus.ENDED);
            auctionRepository.save(a);
        }
    }
}
