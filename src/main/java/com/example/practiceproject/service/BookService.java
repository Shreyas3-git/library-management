package com.example.practiceproject.service;

import com.example.practiceproject.dto.request.CreateBookRequest;
import com.example.practiceproject.dto.request.IssueBookRequest;
import com.example.practiceproject.dto.request.ReturnBookRequest;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.dto.response.ErrorCode;
import com.example.practiceproject.dto.response.ResponseConstants;
import com.example.practiceproject.dto.response.Status;
import com.example.practiceproject.entity.Book;
import com.example.practiceproject.entity.Library;
import com.example.practiceproject.entity.LibraryCard;
import com.example.practiceproject.errorhandling.ResourceNotFoundException;
import com.example.practiceproject.repository.BookRepository;
import com.example.practiceproject.repository.LibraryCardRepository;
import com.example.practiceproject.repository.LibraryRepository;
import com.example.practiceproject.repository.UserRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BookService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @Autowired
    private LibraryRepository libraryRepository;


    public ResponseEntity<CommonResponse> createBook(CreateBookRequest createBook) {
        try {
            log.info(String.format("CreateBook Request => {} ",new Gson().toJson(createBook)));
            return userRepository.findByRrn(createBook.getRrn())
                .map(user -> {
                    Library library = libraryRepository.findByLibraryCode("1266")
                            .orElseThrow(() -> new RuntimeException("Library not found"));
                    Book book = Book.builder()
                        .title(createBook.getTitle())
                        .author(createBook.getAuthor())
                        .quantity(createBook.getQuantity())
                        .isAvailable(true)
                        .library(library)

                        .build();
                    bookRepository.save(book);
                    Set<Book> books = library.getBooks();
                    if(books.isEmpty() ) {books.add(book);
                        library.setBooks(books);
                    } else if(!books.contains(book))
                        library.getBooks().add(book);

                    libraryRepository.save(library);
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
        } catch (Exception e) {
            e.printStackTrace();
            CommonResponse commonResponse = CommonResponse.builder()
                    .message(ResponseConstants.RESTRICT_COMMON_MESSAGE)
                    .status(Status.FAILED.name())
                    .timestamp(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        }
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
                if(book.getQuantity() == 0)
                    book.setAvailable(false);

                Set<Book> books = user.getBooks();
                if(books.isEmpty() ) {
                    books.add(book);
                    user.setBooks(books);
                } else if(!books.contains(book))
                    user.getBooks().add(book);

                bookRepository.save(book);
                libraryCardRepository.save(libraryCard);
                userRepository.save(user);

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
                .message(ResponseConstants.BookResponse.FAILED_DUETO_LIBRARY_CARD)
                .status(Status.FAILED.name())
                .timestamp(LocalDateTime.now())
                .build();
            return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
        });
    }

    public ResponseEntity<CommonResponse> returnBook(ReturnBookRequest request) {
        return new ResponseEntity<>(CommonResponse.builder().build(),HttpStatus.OK);
    }
}
