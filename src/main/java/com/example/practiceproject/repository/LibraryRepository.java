package com.example.practiceproject.repository;

import com.example.practiceproject.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library,Long>
{
    Optional<Library> findByLibraryCode(String libraryCode);
}
