package com.performworld.repository;

import com.performworld.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 제목에 키워드 포함된 공지사항 검색
    List<Notice> findByTitleContaining(String keyword);

    // 최신 5개의 공지사항 조회
    List<Notice> findTop5ByOrderByCreatedAtDesc();

    // 내용에 키워드 포함된 공지사항 검색
    List<Notice> findByContentContaining(String content);

    // 특정 기간 동안의 공지사항 조회
    List<Notice> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 제목에 키워드 포함된 공지사항을 페이징 처리하여 조회
    Page<Notice> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);

    // 제목이 주어지지 않으면 모든 공지사항을 최신순으로 조회
    Page<Notice> findByTitleContainingOrTitleIsNullOrderByCreatedAtDesc(String title, Pageable pageable);

    // JPQL을 사용하여 특정 키워드가 포함된 제목의 공지사항을 페이징 처리
    @Query("select n from Notice n where n.title like concat('%', :keyword, '%')")
    Page<Notice> findByKeyword(String keyword, Pageable pageable);

    // 특정 공지사항을 이미지와 함께 조회
    @EntityGraph(attributePaths = {"imageSet"})  // 이미지와 같은 연관된 데이터를 Eager 로딩
    @Query("select n from Notice n where n.noticeId = :noticeId")
    Optional<Notice> findByIdWithImages(Long noticeId);

    // 최신 공지사항을 페이징 처리하여 조회
    @Query("select n from Notice n order by n.createdAt desc")
    Page<Notice> findLatestNotices(Pageable pageable);

    // 현재 날짜 조회 (native query 사용)
    @Query(value = "select now()", nativeQuery = true)
    String now();
}

