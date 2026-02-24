package com.example.demo.board;

import com.example.demo.board.model.Board;
import lombok.RequiredArgsConstructor;
import com.example.demo.board.model.BoardDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDto.RegRes register(BoardDto.RegReq dto) {
        Board entity = boardRepository.save(dto.toEntity());

        return BoardDto.RegRes.from(entity);
    }

}
