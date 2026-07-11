package com.alssant.asclepio.inbox.repository;

import com.alssant.asclepio.inbox.domain.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InboxEventRepository extends JpaRepository<InboxEvent, UUID> {
}
