package com.example.study.controller;

import com.example.study.dto.BookDTO;
import com.example.study.dto.ResponseDTO;
import com.example.study.dto.SimpleResponseDTO;
import com.example.study.dto.UserDTO;
import com.example.study.model.BookEntity;
import com.example.study.model.UserEntity;
import com.example.study.service.BookService;
import com.example.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService service;
//    @Autowired
//    private UserService userService;

//    @GetMapping("/user/{userId}")
//    public UserDTO getUserEmail(
//            @AuthenticationPrincipal String u,
//            @PathVariable(required = false) String userId
//    ) {
//
//        UserEntity user = userService.getUserEmailAndUserName(userId);
//        final UserDTO responseUserDTO = UserDTO.builder()
//                .email(user.getEmail())
//                .username(user.getUsername())
//                .id(user.getId())
//                .build();
//        System.out.println(responseUserDTO);
//        return responseUserDTO;// !!!!!!!!!!!!!!!!!!!!!!!!!
//    }
    @PostMapping
    public ResponseEntity<?> createBook(
            @AuthenticationPrincipal String userId,
            @RequestBody BookDTO dto){

        try{

            // DYO -> ENTITY
            BookEntity entity = BookDTO.toEntity(dto);
            entity.setId(null); // UserId를 지정
            entity.setUserId(userId); // 로그인기능을 위해 userId를 지정
            // entity 생성
            List<BookEntity> entities = service.create(entity);
            // ENTITY -> DTO
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
            // DTO -> ResponseDTO
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder()
                    .data(dtos)
                    .build();
            // 반환
            return ResponseEntity.ok().body(response);
        }catch(Exception e){
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<?> retrieveTitle(
            @AuthenticationPrincipal String userId,
            @PathVariable(required = false) String title){

        List<BookEntity> entities = service.retrieveTitle(title);
        List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().
                data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

 // 모든 제품을 검색하는 메소드
    @GetMapping
    public ResponseEntity<?> retrieveBookList(@AuthenticationPrincipal String userId){

        List<BookEntity> entities = service.retrieve(userId);
        List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().
                data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }
    // title을 받아서 검색한후 반환
//    @GetMapping("/retrieve")
//    public ResponseEntity<?> retrieveTitle(@RequestBody BookDTO dto){
//
//            List<BookEntity> entities = service.retrieveTitle(dto.getTitle());
//            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
//            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().
//                    data(dtos)
//                    .build();
//            return ResponseEntity.ok().body(response);
//
//
//
//    }

//    @PutMapping // 수정
//    public ResponseEntity<?> updateTodo(@RequestBody BookDTO dto){
//        String temporaryUserId = "ChaeWonKang";
//        BookEntity entity = BookDTO.toEntity(dto); // dto를 entity로 변환
//        entity.setUserId(temporaryUserId);
//        List<BookEntity> entities = service.update(entity);
//        List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList()); //업데이트된 entity를 dto로 가져옴
//        ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder() // 반환할 TodoDTO 생성
//                .data(dtos)
//                .build();
//        return ResponseEntity.ok().body(response);
//    }

            @PutMapping // 수정: 수정한 entity하나만 리턴, 하나만 리턴하기 위해서 simpleResponseDTO 반환
        public ResponseEntity<?> updateBook(
                    @AuthenticationPrincipal String userId,
                @RequestBody BookDTO dto){
        try{
            BookEntity entity = BookDTO.toEntity(dto); // dto를 entity로 변환
            entity.setUserId(userId); // 로그인기능을 위해 userId를 지정
            BookEntity updateEntity = service.update(entity);
            BookDTO dtos = BookDTO.builder() // 반환할 1개의 dto
                    .title(updateEntity.getTitle())
                    .userId(updateEntity.getUserId())
                    .id(updateEntity.getId())
                    .author(updateEntity.getAuthor())
                    .publisher(updateEntity.getPublisher())
                    .url(updateEntity.getUrl())
                    .build();
            SimpleResponseDTO<BookDTO> response = SimpleResponseDTO.<BookDTO>builder() // 반환할 TodoDTO 생성
                    .data(dtos) // 하나의 dto만을 전달하는 SimpleResponseDTO(추가함)
                    .build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            String error = e.getMessage();
            SimpleResponseDTO<BookDTO> response = SimpleResponseDTO.<BookDTO>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        }

    @DeleteMapping
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal String userId,
            @RequestBody BookDTO dto){
        try{
            BookEntity entity = BookDTO.toEntity(dto);
            entity.setUserId(userId); // // 로그인기능을 위해 userId를 지정

            List<BookEntity> entities = service.delete(entity);
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }




}




















