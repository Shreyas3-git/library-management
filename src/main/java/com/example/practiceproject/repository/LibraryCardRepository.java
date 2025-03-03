package com.example.practiceproject.repository;

import com.example.practiceproject.entity.LibraryCard;
import com.example.practiceproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard,Long>
{
    Optional<LibraryCard> findByUser(User user);
}
