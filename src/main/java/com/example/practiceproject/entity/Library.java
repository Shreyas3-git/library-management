package com.example.practiceproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Book> books;

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<LibraryCard> libraryCards;

    @OneToMany(mappedBy = "library",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<User> usersInfo;
}
