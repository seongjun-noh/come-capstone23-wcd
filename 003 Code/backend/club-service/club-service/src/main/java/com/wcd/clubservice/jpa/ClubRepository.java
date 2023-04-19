package com.wcd.clubservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

    // 모임 상세 조회 (club-id)
    Optional<ClubEntity> findById(Long clubId);
}
