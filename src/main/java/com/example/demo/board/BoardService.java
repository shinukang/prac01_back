package com.example.demo.board;

import com.example.demo.board.model.Board;
import com.example.demo.user.model.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import com.example.demo.board.model.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDto.RegRes register(AuthUserDetails user, BoardDto.RegReq dto) {

        Board entity = boardRepository.save(dto.toEntity(user));

        return BoardDto.RegRes.from(entity);
    }

    public BoardDto.PageRes list(int page, int size) {
        // List<Board> boardList = boardRepository.findAll();
        //return boardList.stream().map(BoardDto.ListRes::from).toList();

        PageRequest pageRequest = PageRequest.of(page, size);

        // 페이징 처리, 페이지 번호가 필요하다 -> Page 반환
        Page<Board> pageResult = boardRepository.findAll(pageRequest);

        // 페이징 처리, 페이지 번호가 필요없다. -> Slice 반환
        Slice<Board> sliceResult = boardRepository.findAll(pageRequest);
        return BoardDto.PageRes.from(pageResult);
    }

    public BoardDto.ReadRes read(Long idx) {
        Board board = boardRepository.findById(idx).orElseThrow();
        return BoardDto.ReadRes.from(board);
    }

    public BoardDto.RegRes update(Long idx, BoardDto.RegReq dto) {
        Board board = boardRepository.findById(idx).orElseThrow();
        board.update(dto);

        boardRepository.save(board);

        return BoardDto.RegRes.from(board);
    }

    public void delete(Long idx) {
        boardRepository.deleteById(idx);
    }
}
