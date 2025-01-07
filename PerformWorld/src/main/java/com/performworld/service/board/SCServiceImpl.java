package com.performworld.service.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.repository.board.FAQRepository;
import com.performworld.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SCServiceImpl implements SCService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;



    @Override
    public List<NoticeDTO> getNotice() {
        return noticeRepository.findAll().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
    }
}


