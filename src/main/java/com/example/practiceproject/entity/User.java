package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @Column(name = "user_id",columnDefinition = "BIGINT")
    private Long id;
    private String name;

    @Column(name = "email",columnDefinition = "VARBINARY(255)")
    private byte[] email;

    @Column(name = "contact",columnDefinition = "VARBINARY(255)")
    private byte[] contact;

    @Column(name = "address",columnDefinition = "VARCHAR(80)")
    private String address;

    @Column(name = "pin_code",columnDefinition = "INT")
    private int pinCode;

    @OneToMany(mappedBy = "issuedTo", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Book> books;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private LibraryCard libraryCard;

    private String password;

    private String rrn;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Notifications> notifications;
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
