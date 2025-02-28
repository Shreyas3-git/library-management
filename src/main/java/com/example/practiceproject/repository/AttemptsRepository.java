package com.example.practiceproject.repository;

import com.example.practiceproject.entity.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptsRepository extends JpaRepository<Attempts,Long> {
}
