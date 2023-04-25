package com.sparta.hanghaemamo.repository;


import com.sparta.hanghaemamo.entity.Memo;
import com.sparta.hanghaemamo.entity.MemoLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemoLoveRepository extends JpaRepository<MemoLove, Long> {

//    @Query("select l.memoId, l.love, l.id, l.username from MemoLove l where l.memoId = :memoId and l.username = :username")
//    Boolean findMemoIdAndUserName(@Param("memoId") Long memoId, @Param("username") String username);

    @Query("select count (l) from MemoLove l where l.memoId = :memoId and l.username = :username")
    Long findMemoIdAndUserName(@Param("memoId") Long memoId, @Param("username") String username);

    @Query("select count (l) from MemoLove l where l.memoId = :memoId and l.love = true")
    Long getLoveCnt(@Param("memoId") Long memoId);

    @Query("select l.love from MemoLove l  where l.memoId = :memoId and l.username = :username")
    Boolean getLoveCheck(@Param("memoId") Long memoId, @Param("username") String username);

//    @Query("update MemoLove l set l.love = :love where l.memoId = :memoId and l.username = :username")
//    MemoLove updateLove(@Param("memoId") Long memoId, @Param("username") String username, @Param("love") boolean love);

    @Query("delete from MemoLove l where l.memoId = :memoId and l.username = :username")
    Boolean deleteLove(@Param("memoId") Long memoId, @Param("username") String username);

    MemoLove findByMemoIdAndUsername(Long memoId, String username);




}
