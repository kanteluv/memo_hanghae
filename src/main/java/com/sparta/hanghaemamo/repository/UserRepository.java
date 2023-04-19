package com.sparta.hanghaemamo.repository;

import com.sparta.hanghaemamo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
