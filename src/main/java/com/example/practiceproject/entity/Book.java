package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book
{
    @Id
    @GeneratedValue(generator = "native",strategy = GenerationType.AUTO)
    @Column(name = "book_id",columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "title",columnDefinition = "VARCHAR(80)")
    private String title;

    @Column(name = "author",columnDefinition = "VARCHAR(50)")
    private String author;

    @Column(name = "is_available",columnDefinition = "BOOLEAN")
    private boolean isAvailable = true;

    @Column(name = "issued_date",columnDefinition = "date")
    private LocalDate issuedDate;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User issuedTo;

    @ManyToOne(targetEntity = Library.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }
}
