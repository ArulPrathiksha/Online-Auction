package com.auction.auction_system.repository;

import com.auction.auction_system.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // Fetch available time slots that are not booked and are after the current time
    List<TimeSlot> findByBookedFalseAndStartTimeAfter(LocalTime now);
    
    // Fetch all available time slots that are not booked
    List<TimeSlot> findByBookedFalse();
}
