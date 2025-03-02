package com.example.practiceproject.repository;

import com.example.practiceproject.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Long>
{
    Optional<Notifications> findTopByReferenceNumberOrderByNotificationIdDesc(String referenceNumber);
}
