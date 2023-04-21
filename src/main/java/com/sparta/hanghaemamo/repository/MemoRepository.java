package com.sparta.hanghaemamo.repository;
import com.sparta.hanghaemamo.entity.Memo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MemoRepository extends JpaRepository<Memo, Long> {
//    @EntityGraph(attributePaths = "comments")
    //Memo 엔티티를 조회할 때 comments 필드도 함께 로딩하도록 지시
//    @Query("select m, c from Memo m LEFT JOIN Comment c On m.id = c.memo.id ORDER BY m.modifiedAt DESC ")
    @Query("select m, c from Memo m LEFT JOIN fetch Comment c On m.id = c.memo.id ORDER BY m.modifiedAt DESC ")
    Set<Memo> findAllMemoAndComments();


    @Query("SELECT m, c FROM Memo m LEFT JOIN Comment c ON m.id = c.memo.id WHERE m.id = :memoId ORDER BY m.modifiedAt DESC")
    Set<Memo> findMemoAndComments(@Param("memoId") Long memoId);
    //JPQL 문에 사용되는 파라미터 바인딩을 위한 이름표와 메서드의 파라미터 명과 일치하게 만들기 위해 @Param을 사용
}