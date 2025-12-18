package com.auction.auction_system.controller;

import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auctions/slots")
public class SlotController {
    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public List<TimeSlot> availableSlots() {
        return slotService.getAvailableSlots();
    }

    @PostMapping
    public ResponseEntity<?> createSlot(@RequestBody TimeSlot slot) {
        // Expect startTime and endTime in ISO-8601 LocalDateTime format in JSON
        if (slot.getStartTime() == null || slot.getEndTime() == null)
            return ResponseEntity.badRequest().body("startTime/endTime required");
        if (!slot.getStartTime().isBefore(slot.getEndTime()))
            return ResponseEntity.badRequest().body("startTime must be before endTime");
        if (slot.getStartTime().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("startTime must be future");
        return ResponseEntity.ok(slotService.createSlot(slot));
    }
}
