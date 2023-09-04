package com.example.study.service;

import com.example.study.model.BookEntity;
import com.example.study.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository repository;
    public List<BookEntity> create(final BookEntity entity){
        validate(entity);
        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return repository.findByUserId((entity.getUserId()));
    }

    public List<BookEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public List<BookEntity> retrieveTitle(final String title){ return repository.findByTitle(title);}

//    public List<BookEntity> update(final BookEntity entity){
//        validate(entity); //
//        final Optional<BookEntity> original = repository.findById(entity.getId()); // null 과 entity객체
//
//        original.ifPresent(todo->{
//            todo.setTitle(entity.getTitle());
//            log.warn("제목 업데이트 됨");
//
//            todo.setAuthor(entity.getAuthor()); // 추가된 내용: 저자 업데이트
//            log.warn("저자 업데이트 됨");
//
//            todo.setPublisher(entity.getPublisher()); // 추가된 내용: 출판사 업데이트
//            log.warn("출판사 업데이트 됨");
//            repository.save(todo);
//        });
//        return retrieve(entity.getUserId()); // 사용자의 모든 Todo 리스트를 리턴한다.
//
//    }
    public BookEntity update(final BookEntity entity){
        validate(entity); //
        final Optional<BookEntity> original = repository.findById(entity.getId()); // null 과 entity객체
        BookEntity returnEntity = null;

        if(original.isPresent()){
            final BookEntity book = original.get();
            book.setTitle(entity.getTitle());
            book.setAuthor(entity.getAuthor());
            book.setPublisher(entity.getPublisher());


            repository.save(book);
            returnEntity = book;
        }
        return returnEntity;
    }

    public List<BookEntity> delete(final BookEntity entity){
        validate(entity);

        try{
            repository.delete(entity); // 리포지터리 계층에 요청
        }catch(Exception e){
            log.error("error deleting entity", entity.getId(), e);
            throw new RuntimeException("error deleting entity"+ entity.getId());
        }
        return retrieve(entity.getUserId()); // 삭제 후 BookList 가져와 리턴
    }

    private static void validate(BookEntity entity) {
        if(entity == null){
            log.warn("Entity cannot be null.");
        }

        if(entity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
