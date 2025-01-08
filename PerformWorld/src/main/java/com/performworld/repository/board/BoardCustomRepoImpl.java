package com.performworld.repository.board;

import com.performworld.domain.Notice;
import com.performworld.domain.QQnA;
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
    public List<QnADTO> getQnaList(QnADTO qnadto) {
        QQnA qna = QQnA.qnA;

        BooleanBuilder builder = new BooleanBuilder();
        if(qnadto.getTitle() != null && !qnadto.getTitle().isEmpty()) {
            builder.and(qna.title.contains(qnadto.getTitle()));
        }
        if(qnadto.getUserId() != null && !qnadto.getUserId().isEmpty()) {
            builder.and(qna.user.userId.contains(qnadto.getUserId()));
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
}
