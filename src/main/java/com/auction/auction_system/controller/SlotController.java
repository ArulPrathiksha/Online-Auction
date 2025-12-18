package com.auction.auction_system.controller;

import com.auction.auction_system.entity.TimeSlot;
import com.auction.auction_system.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/auctions/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/available")
    public List<TimeSlot> getAvailableSlots(@RequestParam("date") LocalDate date) {
        return slotService.getAvailableSlots(date);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAuctionSlot(@RequestParam("auctionId") Long auctionId,
                                             @RequestParam("timeSlotId") Long timeSlotId,
                                             @RequestParam("auctionDate") LocalDate auctionDate) {
        try {
            slotService.bookAuctionSlot(auctionId, timeSlotId, auctionDate);
            return ResponseEntity.ok("Auction booked successfully!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
