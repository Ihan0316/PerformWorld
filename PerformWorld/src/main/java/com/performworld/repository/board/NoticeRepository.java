package com.performworld.repository.board;

import com.performworld.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    //JPQL을 사용하여 특정 키워드가 포함된 제목의 공지사항을 페이징 처리
    @Query("select n from Notice n where n.title like concat('%', :keyword, '%')")
    Page<Notice> findByKeyword(String keyword, Pageable pageable);

    //특정 공지사항을 이미지와 함께 조회
    @EntityGraph(attributePaths = {"imageset"})
    @Query("select n from Notice n where n.noticeId = :noticeId")
    Optional<Notice> findByNoticeId(@Param("noticeId") Long noticeId);

    //최신 공지사항을 페이징 처리한 후 조회
    @Query("select n from Notice n order by n.regDate desc ")
    Page<Notice> findLatestNotices(Pageable pageable);

    //현재 날짜 조회
    @Query(value = "select now()", nativeQuery = true)
    String now();

    //기본 제공되는 delete 메서드 사용
    void deleteById(Long id);
}

