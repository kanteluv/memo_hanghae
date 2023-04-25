package com.sparta.hanghaemamo.repository;

import com.sparta.hanghaemamo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.memo.id = :memoId")
    List<Comment> findAllByMemoId(@Param("memoId") Long memoId);
}
