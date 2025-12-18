package com.auction.auction_system.controller;

import com.auction.auction_system.entity.Participant;
import com.auction.auction_system.repository.AuctionRepository;
import com.auction.auction_system.repository.ParticipantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions")
public class ParticipantController {
    private final ParticipantRepository participantRepository;
    private final AuctionRepository auctionRepository;

    public ParticipantController(ParticipantRepository participantRepository, AuctionRepository auctionRepository) {
        this.participantRepository = participantRepository;
        this.auctionRepository = auctionRepository;
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> register(@PathVariable Long id, @RequestParam Long userId, @RequestParam(required = false) String proof) {
        if (!auctionRepository.existsById(id)) return ResponseEntity.notFound().build();
        if (participantRepository.findByAuctionIdAndUserId(id, userId).isPresent()) {
            return ResponseEntity.status(409).body("Already registered");
        }
        Participant p = new Participant();
        p.setAuctionId(id);
        p.setUserId(userId);
        p.setProof(proof);
        participantRepository.save(p);
        return ResponseEntity.ok(p);
    }
}
