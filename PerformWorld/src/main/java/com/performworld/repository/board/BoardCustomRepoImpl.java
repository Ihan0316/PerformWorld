package com.performworld.repository.board;

import com.performworld.domain.Notice;
import com.performworld.domain.QNotice;
import com.performworld.domain.QQnA;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.QnADTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class BoardCustomRepoImpl extends QuerydslRepositorySupport implements BoardCustomRepo {
    public BoardCustomRepoImpl() {
        super(Notice.class);
    }

    // QnA 목록 조회
    @Override
    public List<QnADTO> getQnaList(QnADTO qnaDTO) {
        QQnA qna = QQnA.qnA;

        BooleanBuilder builder = new BooleanBuilder();
        if(qnaDTO.getTitle() != null && !qnaDTO.getTitle().isEmpty()) {
            builder.and(qna.title.contains(qnaDTO.getTitle()));
        }
        if(qnaDTO.getUserId() != null && !qnaDTO.getUserId().isEmpty()) {
            builder.and(qna.user.userId.contains(qnaDTO.getUserId()));
        }

        List<Tuple> result = from(qna)
                .where(builder)
                .orderBy(qna.qnaId.desc())

                .select(
                        qna.qnaId
                        , qna.user.userId
                        , qna.title
                        , qna.content
                        , qna.response
                        , qna.responseDatetime
                        , qna.regDate
                ).fetch();

        return result.stream().map(tuple -> QnADTO.builder()
                        .qnaId(tuple.get(qna.qnaId))
                        .userId(tuple.get(qna.user.userId))
                        .title(tuple.get(qna.title))
                        .content(tuple.get(qna.content))
                        .response(tuple.get(qna.response))
                        .responseDate(tuple.get(qna.responseDatetime))
                        .regDate(tuple.get(qna.regDate))
                        .build())
                .collect(Collectors.toList());
    }

    // 공지사항 목록 조회
    @Override
    public List<NoticeDTO> getNoticeList(NoticeDTO noticeDTO) {
        QNotice notice = QNotice.notice;

        BooleanBuilder builder = new BooleanBuilder();
        if(noticeDTO.getTitle() != null && !noticeDTO.getTitle().isEmpty()) {
            builder.and(notice.title.contains(noticeDTO.getTitle()));
        }

        List<Tuple> result = from(notice)
                .where(builder)
                .orderBy(notice.noticeId.desc())

                .select(
                        notice.noticeId
                        , notice.title
                        , notice.content
                        , notice.regDate
                ).fetch();

        return result.stream().map(tuple -> NoticeDTO.builder()
                        .noticeId(tuple.get(notice.noticeId))
                        .title(tuple.get(notice.title))
                        .content(tuple.get(notice.content))
                        .regDate(tuple.get(notice.regDate))
                        .build())
                .collect(Collectors.toList());
    }
}
