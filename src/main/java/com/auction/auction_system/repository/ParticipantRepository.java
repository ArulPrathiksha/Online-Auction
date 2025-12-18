package com.auction.auction_system.repository;

import com.auction.auction_system.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByAuctionIdAndUserId(Long auctionId, Long userId);

    long countByAuctionId(Long auctionId);
}
