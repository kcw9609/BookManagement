package com.example.study.dto;

import com.example.study.model.BookEntity;
import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDTO {

    private String id; // 클라이언트에 보낼때 아이디를 보내야하나
    private String userId;
    private String title;
    private String author;
    private String publisher;
    private String url;

    public BookDTO(final BookEntity entity){
        this.id = entity.getId();
        this.userId = entity.getUserId(); // 추가
        this.title = entity.getTitle();
        this.author = entity.getAuthor(); // 추가
        this.publisher = entity.getPublisher(); // 추가
        
        this.url = entity.getUrl(); // 추가기능
    }
    public static BookEntity toEntity(final BookDTO dto){
        return BookEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .userId(dto.getUserId()) // 추가
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .url(dto.getUrl()) // 추가
                .build();
    }
}
