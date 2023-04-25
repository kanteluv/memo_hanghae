package com.sparta.hanghaemamo.repository;


import com.sparta.hanghaemamo.entity.CommentLove;
import com.sparta.hanghaemamo.entity.MemoLove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLoveRepository extends JpaRepository<CommentLove, Long> {
}
