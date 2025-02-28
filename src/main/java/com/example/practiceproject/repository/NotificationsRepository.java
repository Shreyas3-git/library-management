package com.example.practiceproject.repository;

import com.example.practiceproject.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Long>
{
    Optional<Notifications> findByReferenceNumber(String referenceNumber);
}
