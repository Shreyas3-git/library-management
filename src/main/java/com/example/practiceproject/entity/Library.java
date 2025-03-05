package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Library
{
    @Id
    @GeneratedValue(generator = "native",strategy = GenerationType.AUTO)
    @Column(name = "library_id",columnDefinition = "BIGINT")
    private Long id;

    private String libraryCode;

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private Set<LibraryCard> libraryCards = new HashSet<>();

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> usersInfo = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return id != null && id.equals(library.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : super.hashCode();
    }

}
