package com.auction.auction_system.service;

import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlotService {
    private final TimeSlotRepository repo;

    public SlotService(TimeSlotRepository repo) {
        this.repo = repo;
    }

    public List<TimeSlot> getAvailableSlots() {
        return repo.findByBookedFalseAndStartTimeAfter(LocalDateTime.now());
    }

    @Transactional
    public TimeSlot createSlot(TimeSlot slot) {
        // Could add overlap check for slots here
        return repo.save(slot);
    }

    public TimeSlot findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
