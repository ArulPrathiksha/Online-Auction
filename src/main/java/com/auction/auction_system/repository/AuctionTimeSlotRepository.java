package com.auction.auction_system.repository;

import com.auction.auction_system.entity.AuctionTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface AuctionTimeSlotRepository extends JpaRepository<AuctionTimeSlot, Long> {
    // Check if a time slot is already booked for a specific date
    boolean existsByAuctionDateAndTimeSlotId(LocalDate auctionDate, Long timeSlotId);
}
