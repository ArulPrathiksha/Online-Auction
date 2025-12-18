package com.auction.auction_system.repository;

import com.auction.auction_system.entity.Auction;
import com.auction.auction_system.entity.AuctionStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatus(AuctionStatus status);

    @Query("select a from Auction a where lower(a.productName) like lower(concat('%',:q,'%')) or lower(a.description) like lower(concat('%',:q,'%'))")
    Page<Auction> searchByKeyword(@Param("q") String q, Pageable p);

    @Query("select case when count(a)>0 then true else false end from Auction a where a.startTime < :end and a.endTime > :start")
    boolean existsOverlapping(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

