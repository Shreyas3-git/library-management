package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notifications
{
    @Id
    @GeneratedValue(generator = "native",strategy = GenerationType.AUTO)
    @Column(name = "notification_id",columnDefinition = "BIGINT")
    private Long notificationId;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    @Column(name = "verification_sid", columnDefinition = "VARCHAR(50)")
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "phone_number",columnDefinition = "VARBINARY(255)")
    private byte[] phoneNumber;

    @Column(name = "email_id",columnDefinition = "VARBINARY(255)")
    private byte[] emailId;

    @Column(name = "status", columnDefinition = "VARCHAR(50)")
    private String status;

    @OneToMany(mappedBy = "notifications",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Attempts> sendCodeAttempts;
}
