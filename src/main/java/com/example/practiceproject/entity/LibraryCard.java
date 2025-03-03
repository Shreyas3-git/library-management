package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String address;

    @Column(name = "book_return_date",columnDefinition = "date")
    private LocalDate bookReturnDate;

    @OneToMany(mappedBy = "libraryCard",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Book> books;
}
