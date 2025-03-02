package com.example.practiceproject.service;

import com.example.practiceproject.dto.request.CreateBookRequest;
import com.example.practiceproject.dto.request.IssueBookRequest;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.dto.response.ErrorCode;
import com.example.practiceproject.dto.response.ResponseConstants;
import com.example.practiceproject.dto.response.Status;
import com.example.practiceproject.entity.Book;
import com.example.practiceproject.entity.LibraryCard;
import com.example.practiceproject.errorhandling.ResourceNotFoundException;
import com.example.practiceproject.repository.BookRepository;
import com.example.practiceproject.repository.LibraryCardRepository;
import com.example.practiceproject.repository.UserRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    public ResponseEntity<CommonResponse> createBook(CreateBookRequest createBook) {
        log.info(String.format("CreateBook Request => {} ",new Gson().toJson(createBook)));
        return userRepository.findByRrn(createBook.getRrn())
            .map(user -> {
                Book book = Book.builder()
                    .title(createBook.getTitle())
                    .author(createBook.getAuthor())
                    .quantity(createBook.getQuantity())
                    .isAvailable(true)
                    .build();
                bookRepository.save(book);
                CommonResponse commonResponse = CommonResponse.builder()
                    .message(ResponseConstants.BookResponse.SUCCESS_BOOK_MESSAGE)
                    .status(Status.SUCCESS.name())
                    .timestamp(LocalDateTime.now())
                    .build();
                return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        })
        .orElseGet(() -> {
            CommonResponse commonResponse = CommonResponse.builder()
                .message(ResponseConstants.RESTRICT_COMMON_MESSAGE)
                .status(Status.FAILED.name())
                .timestamp(LocalDateTime.now())
                .build();
            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        });
    }


    public ResponseEntity<CommonResponse> issueBook(IssueBookRequest request) {
        log.info(String.format("Issue Book Request => {} ",new Gson().toJson(request)) );
        return userRepository.findByIdAndRrn(request.getUserId(),request.getRrn()).map(user -> {
            Book book = bookRepository.findTopByTitleAndAuthorOrderByIdAsc(request.getTitle(), request.getAuthor())
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseConstants.BookResponse.FAILED_BOOK_MESSAGE,
                                    ErrorCode.BAD_REQUEST.name(),Status.FAILED.name(), LocalDateTime.now()));
            LibraryCard libraryCard = libraryCardRepository.findByUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseConstants.BookResponse.FAILED_DUETO_LIBRARY_CARD,
                            ErrorCode.BAD_REQUEST.name(), Status.FAILED.name(), LocalDateTime.now()));

            if(book.getQuantity() > 0) {
                libraryCard.setBookReturnDate(LocalDate.now().plusDays(14));
                book.setQuantity(book.getQuantity()-1);
                book.setIssuedTo(user);
                book.setIssuedDate(LocalDate.now());
                book.setAvailable(false);
                book.setLibraryCard(libraryCard);
                bookRepository.save(book);
                libraryCardRepository.save(libraryCard);

                CommonResponse commonResponse = CommonResponse.builder()
                    .message(ResponseConstants.BookResponse.SUCCESS_BOOK_ISSUE)
                    .status(Status.SUCCESS.name())
                    .timestamp(LocalDateTime.now())
                    .build();
                return new ResponseEntity<>(commonResponse, HttpStatus.OK);
            } else {
                CommonResponse commonResponse = CommonResponse.builder()
                        .message(ResponseConstants.BookResponse.FAILED_BOOK_MESSAGE)
                        .status(Status.FAILED.name())
                        .timestamp(LocalDateTime.now())
                        .build();
                return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
            }

        }).orElseGet(() -> {
            CommonResponse commonResponse = CommonResponse.builder()
                    .message(ResponseConstants.RESTRICT_COMMON_MESSAGE)
                    .status(Status.FAILED.name())
                    .timestamp(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        });
    }

}
