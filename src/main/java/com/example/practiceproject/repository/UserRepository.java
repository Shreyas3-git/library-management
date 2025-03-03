package com.example.practiceproject.repository;

import com.example.practiceproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    public Optional<User> findByEmailAndContact(byte[] email,byte[] contact);
    public Optional<User> findByRrn(String rrn);

    public Optional<User> findByIdAndRrn(long userId,String rrn);
}
