package com.sparta.hanghaemamo.repository;


import com.sparta.hanghaemamo.entity.CommentLove;
import com.sparta.hanghaemamo.entity.MemoLove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLoveRepository extends JpaRepository<CommentLove, Long> {
    @Query("select count (l) from CommentLove l where l.commentId = :commentId and l.username = :username")
    Long findCommentIdAndUserName(@Param("commentId") Long commentId, @Param("username") String username);

    @Query("select count (l) from CommentLove l where l.commentId = :commentId and l.love = true")
    Long getLoveCnt(@Param("commentId") Long commentId);

    @Query("select l.love from CommentLove l  where l.commentId = :commentId and l.username = :username")
    Boolean getLoveCheck(@Param("commentId") Long commentId, @Param("username") String username);

    @Query("delete from CommentLove l where l.commentId = :commentId and l.username = :username")
    Boolean deleteLove(@Param("commentId") Long commentId, @Param("username") String username);

    CommentLove findByCommentIdAndUsername(Long commentId, String username);

}
