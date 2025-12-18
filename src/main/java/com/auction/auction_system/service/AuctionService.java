package com.auction.auction_system.service;

import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionStatus;
import com.auction.auction_system.entity.AuctionTimeSlot;
import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.repository.AuctionRepository;
import com.auction.auction_system.repository.AuctionTimeSlotRepository;
import com.auction.auction_system.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AuctionTimeSlotRepository auctionTimeSlotRepository;

    public AuctionService(AuctionRepository auctionRepository, 
                          TimeSlotRepository timeSlotRepository,
                          AuctionTimeSlotRepository auctionTimeSlotRepository) {
        this.auctionRepository = auctionRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.auctionTimeSlotRepository = auctionTimeSlotRepository;
    }

    /**
     * Create a new auction and assign a time slot to it.
     */
    @Transactional
    public Auction createAuction(String productName, String description, BigDecimal reservePrice, 
                                 Long timeSlotId, LocalDate auctionDate) {

        // Fetch the time slot for the given ID
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found"));

        if (timeSlot.isBooked()) {
            throw new IllegalStateException("This time slot is already booked.");
        }

        // Create the new auction
        Auction auction = new Auction();
        auction.setProductName(productName);
        auction.setDescription(description);
        auction.setReservePrice(reservePrice);
        auction.setStartTime(timeSlot.getStartTime().atDate(auctionDate));  // Combine with auction date
        auction.setEndTime(timeSlot.getEndTime().atDate(auctionDate));  // Combine with auction date
        auction.setStatus(AuctionStatus.UPCOMING);

        // Save the auction
        auction = auctionRepository.save(auction);

        // Link the auction with the time slot for the given date
        AuctionTimeSlot auctionTimeSlot = new AuctionTimeSlot();
        auctionTimeSlot.setAuction(auction);
        auctionTimeSlot.setTimeSlot(timeSlot);
        auctionTimeSlot.setAuctionDate(auctionDate);
        
        // Save the auction-time slot link
        auctionTimeSlotRepository.save(auctionTimeSlot);

        // Mark the time slot as booked
        timeSlot.setBooked(true);
        timeSlotRepository.save(timeSlot);

        return auction;
    }

    /**
     * Mark an auction as LIVE if its start time has passed.
     */
    @Transactional
    public void markLive(Auction auction) {
        if (auction.getStatus() == AuctionStatus.UPCOMING && auction.getStartTime().isBefore(LocalDateTime.now())) {
            auction.setStatus(AuctionStatus.LIVE);
            auctionRepository.save(auction);
        }
    }

    /**
     * Mark an auction as ENDED if its end time has passed.
     */
    @Transactional
    public void markEnded(Auction auction) {
        if (auction.getStatus() == AuctionStatus.LIVE && auction.getEndTime().isBefore(LocalDateTime.now())) {
            auction.setStatus(AuctionStatus.ENDED);
            auctionRepository.save(auction);
        }
    }

    /**
     * Get all upcoming auctions.
     */
    public List<Auction> getUpcoming() {
        return auctionRepository.findByStatus(AuctionStatus.UPCOMING);
    }

    /**
     * Get all live auctions.
     */
    public List<Auction> getLive() {
        return auctionRepository.findByStatus(AuctionStatus.LIVE);
    }

    /**
     * Get all ended auctions.
     */
    public List<Auction> getEnded() {
        return auctionRepository.findByStatus(AuctionStatus.ENDED);
    }

    /**
     * Get an auction by its ID.
     */
    public Auction getById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));
    }
}
