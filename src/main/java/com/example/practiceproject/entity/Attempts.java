package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attempts
{
    @Id
    @GeneratedValue(generator = "native",strategy = GenerationType.AUTO)
    @Column(name = "attempts_id",columnDefinition = "BIGINT")
    private Long attemptsId;

    @Column(name = "attempt_sid",columnDefinition = "VARCHAR(80)")
    private String attemptsSid;

    @Column(name = "channel",columnDefinition = "VARCHAR(5)")
    private String channel;

    @Column(name = "sent_time",columnDefinition = "DATETIME")
    private LocalDateTime sentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id",nullable = false)
    private Notifications notifications;
}
