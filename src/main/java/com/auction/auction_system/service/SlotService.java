package com.auction.auction_system.service;

import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionTimeSlot;
import com.auction.auction_system.repository.TimeSlotRepository;
import com.auction.auction_system.repository.AuctionRepository;
import com.auction.auction_system.repository.AuctionTimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionTimeSlotRepository auctionTimeSlotRepository;

    public SlotService(TimeSlotRepository timeSlotRepository, 
                       AuctionRepository auctionRepository, 
                       AuctionTimeSlotRepository auctionTimeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.auctionRepository = auctionRepository;
        this.auctionTimeSlotRepository = auctionTimeSlotRepository;
    }

    // Preload fixed time slots for every day (from 00:00 to 23:30 in 30-minute intervals)
    @PostConstruct
    @Transactional
    public void preloadTimeSlots() {
        // Check if fixed time slots are already populated
        if (timeSlotRepository.count() == 0) {
            LocalTime startTime = LocalTime.of(0, 0);  // Start at 00:00
            LocalTime endTime;

            // Generate 48 time slots (00:00 to 23:30 in 30-minute intervals)
            for (int i = 0; i < 48; i++) {
                endTime = startTime.plusMinutes(30);  // Each time slot lasts for 30 minutes

                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(startTime);
                timeSlot.setEndTime(endTime);
                timeSlot.setBooked(false);

                // Save the time slot in the repository
                timeSlotRepository.save(timeSlot);

                // Increment the start time by 30 minutes for the next slot
                startTime = endTime;
            }
        }
    }

    // Fetch available time slots for a specific date
    public List<TimeSlot> getAvailableSlots(LocalDate auctionDate) {
        // Fetch available time slots that are not booked for the given date
        return timeSlotRepository.findByBookedFalseAndStartTimeAfter(LocalTime.now());
    }

    // Book an auction with a specific time slot and date
    @Transactional
    public void bookAuctionSlot(Long auctionId, Long timeSlotId, LocalDate auctionDate) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found"));

        // Check if the time slot is already booked for the given date
        boolean isBooked = auctionTimeSlotRepository.existsByAuctionDateAndTimeSlotId(auctionDate, timeSlotId);
        if (isBooked) {
            throw new IllegalStateException("This time slot is already booked for this date");
        }

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        // Link the auction with the selected time slot
        AuctionTimeSlot auctionTimeSlot = new AuctionTimeSlot();
        auctionTimeSlot.setAuction(auction);
        auctionTimeSlot.setTimeSlot(timeSlot);
        auctionTimeSlot.setAuctionDate(auctionDate);

        // Save the auction time slot link
        auctionTimeSlotRepository.save(auctionTimeSlot);

        // Mark the time slot as booked
        timeSlot.setBooked(true);
        timeSlotRepository.save(timeSlot);
    }
}
