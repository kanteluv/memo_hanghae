package com.sparta.hanghaemamo.repository;
import com.sparta.hanghaemamo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();
    Memo findByIdAndPwd(Long id, String pwd);
    void deleteByIdAndPwd(Long id, String pwd);
}