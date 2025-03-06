package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "library_card")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryCard
{
    @Id
    @GeneratedValue(generator = "native",strategy = GenerationType.AUTO)
    @Column(name = "library_card_id",columnDefinition = "BIGINT")
    private Long id;
    @Column(name = "card_number",columnDefinition = "VARBINARY(255)")
    private byte[] cardNumber;
    @Column(name = "issue_date",columnDefinition = "DATE")
    private LocalDate issueDate;
    @Column(name = "issue_time",columnDefinition = "TIME")
    private LocalTime issueTime;
    @OneToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    private String address;

    @Column(name = "book_return_date",columnDefinition = "date")
    private LocalDate bookReturnDate;

    @ManyToOne(targetEntity = Library.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryCard libraryCard = (LibraryCard) o;
        return Objects.equals(id, libraryCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
