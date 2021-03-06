package com.ssafy.springboot.service;


import com.ssafy.springboot.domain.board.*;
import com.ssafy.springboot.web.dto.board.*;
import com.ssafy.springboot.domain.user.*;

import com.sun.net.httpserver.HttpsConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final BoardPartyService boardPartyService;

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> selectAll() {
        return boardRepository.findAllDesc()
                .stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> save(BoardSaveRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getManager_email());
        if (user == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not exist... user_email=" + requestDto.getManager_email());
        Board board = boardRepository.save(requestDto.toEntity(user));
        if (!board.getPassword().equals(""))
            boardPartyService.update(board.getBoard_id(), new BoardPartySaveRequestDto(requestDto.getManager_email(), board.getPassword()));

        return ResponseEntity.status(HttpStatus.OK).body(board.getBoard_id());
    }

    @Transactional
    public ResponseEntity<?> update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board does not exist... " + id));
        //User user, String title, String contents, String topic, String type, String password
        User user = userRepository.findByEmail(requestDto.getManager_email());
        board.update(user, requestDto.getTitle(),
                requestDto.getContents(), requestDto.getTopic(), requestDto.getType(), requestDto.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @Transactional
    public void delete(Long id) {
        Board posts = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board does not exist... " + id));

        boardRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findByType(String type) {
        return boardRepository.findByType(type)
                .stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardListResponseDto> findByUser(String email) {
        User user = userRepository.findByEmail(email);

        return boardRepository.findByUser(user.getUser_id())
                .stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardListResponseDto> top10() {
        List<Board> list = boardRepository.top10();
        if (list.size() > 10)
            list.subList(0, 9);
        return list.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardListResponseDto> postCntTop10() {
        List<Board> list = boardRepository.postCntTop10();
        if (list.size() > 10)
            list.subList(0, 9);
        return list.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> isJoin(String email, Long id) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not exist... user_email=" + email);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board does not exist... " + id));

        return ResponseEntity.status(HttpStatus.OK).body(boardPartyService.isJoin(user.getUser_id(), board.getBoard_id()));
    }
}
