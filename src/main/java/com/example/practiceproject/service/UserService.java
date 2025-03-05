package com.example.practiceproject.service;

import com.example.practiceproject.dto.request.UserRequest;
import com.example.practiceproject.dto.response.CommonResponse;
import com.example.practiceproject.dto.response.ErrorCode;
import com.example.practiceproject.dto.response.ResponseConstants;
import com.example.practiceproject.dto.response.Status;
import com.example.practiceproject.entity.Library;
import com.example.practiceproject.entity.LibraryCard;
import com.example.practiceproject.entity.User;
import com.example.practiceproject.repository.LibraryCardRepository;
import com.example.practiceproject.repository.LibraryRepository;
import com.example.practiceproject.repository.UserRepository;
import com.example.practiceproject.utils.DatabaseEncryptionUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    public ResponseEntity<CommonResponse> createUser(UserRequest request) throws Exception {
        try {
            log.info(String.format("Create User Request Body %s",request.toString()));
            byte[] encryptedEmail = DatabaseEncryptionUtils.encrypt(request.getEmail());
            byte[] encryptedContact = DatabaseEncryptionUtils.encrypt(request.getContact());
            Library library = libraryRepository.findByLibraryCode("1266")
                    .orElseThrow(() -> new RuntimeException("Library not found"));
            return userRepository.findByRrn(request.getRrn()).map(userInfo -> {
                return userRepository.findByEmailAndContact(encryptedEmail,encryptedContact)
                    .map(existingUser -> {
                        return new ResponseEntity<>(CommonResponse.builder().rrn(existingUser.getRrn())
                                .message(ResponseConstants.CONFLICT_MESSAGE).errorCode(ErrorCode.CONFLICT.name())
                                .status(Status.FAILED.name()).build(), HttpStatus.CONFLICT);
                    }).orElseGet(() -> {
                        userInfo.setName(request.getName());
                        userInfo.setEmail(encryptedEmail);
                        userInfo.setAddress(request.getAddress());
                        userInfo.setPinCode(Integer.parseInt(request.getPinCode()));
                        userInfo.setPassword(request.getPassword());
                        userInfo.setCreatedAt(LocalDateTime.now());
                        userInfo.setUpdatedAt(LocalDateTime.now());

                        LibraryCard libraryCard = userInfo.getLibraryCard();
                        assignLibraryCardToUser(libraryCard,request,userInfo);
                        userRepository.save(userInfo);

                        Set<User> users = library.getUsersInfo();
                        Set<LibraryCard> libraryCards = library.getLibraryCards();
                        assignUserToLibrary(users,userInfo,library);
                        assignLibraryCardToLibrary(libraryCards,library,libraryCard);

                        libraryRepository.save(library);

                        return new ResponseEntity<>(CommonResponse.builder()
                        .message(ResponseConstants.SUCCESS_MESSAGE)
                        .status(Status.SUCCESS.name())
                        .errorCode(ErrorCode.OK.name())
                        .build(), HttpStatus.CREATED);
                    });
            }).orElseGet(() -> {
                return new ResponseEntity<>(CommonResponse.builder()
                    .message(ResponseConstants.RESTRICT_COMMON_MESSAGE)
                    .status(Status.FAILED.name())
                    .errorCode(ErrorCode.BAD_REQUEST.name())
                    .build(), HttpStatus.BAD_REQUEST);
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("Error while registration of User : %s",e.getLocalizedMessage()));
            return new ResponseEntity<>(CommonResponse.builder()
                .message(ResponseConstants.FAILED_REGISTRATION_MESSAGE)
                .status(Status.FAILED.name())
                .errorCode(ErrorCode.UNEXPECTED_ERROR.name())
                .build(), HttpStatus.OK);
        }
    }


    private String getAddress(UserRequest request) {
        return request != null && request.getLibraryCard() != null ?
                request.getLibraryCard().getAddress() : null;
    }

    private byte[] getCardNumber(String rrn) {
        try {
            return DatabaseEncryptionUtils.encrypt(String.valueOf(LocalDate.now()) + "-" + System.currentTimeMillis());
        } catch (Exception ex) {
            log.error(String.format("Failed to generate cardNumber  %s for this rrn %s",ex.getLocalizedMessage(),rrn));
        }
        return null;
    }


    private byte[] getEmail(String email,String rrn) {
        try {
            return DatabaseEncryptionUtils.encrypt(email);
        } catch (Exception ex) {
            log.error(String.format("Failed to Encrypt Email for this rrn %s",rrn,ex.getLocalizedMessage()));
        }
        return null;
    }

    private byte[] getContact(String contact,String rrn) {
        try {
            return DatabaseEncryptionUtils.encrypt(contact);
        } catch (Exception ex) {
            log.error(String.format("Failed to Encrypt Contact details for this rrn %s",rrn,ex.getLocalizedMessage()));
        }
        return null;
    }


    private void assignUserToLibrary(Set<User> users, User userInfo,
                            Library library) {
        if (users.isEmpty() ) {
            users.add(userInfo);
            library.setUsersInfo(users);
        } else if(!users.contains(userInfo)) {
            library.getUsersInfo().add(userInfo);
        }
    }


    private void assignLibraryCardToLibrary(Set<LibraryCard> libraryCards, Library library,
                                   LibraryCard libraryCard) {
        if (libraryCards.isEmpty()) {
            libraryCards.add(libraryCard);
            library.setLibraryCards(libraryCards);
        } else if(libraryCards.contains(libraryCard)) {
            library.getLibraryCards().add(libraryCard);
        }
    }

    private void assignLibraryCardToUser(LibraryCard libraryCard, UserRequest request ,
                                         User userInfo) {
        if (libraryCard == null) {
            libraryCard = LibraryCard.builder()
                    .address(getAddress(request))
                    .cardNumber(getCardNumber(userInfo.getRrn()))
                    .issueDate(LocalDate.now())
                    .issueTime(LocalTime.now())
                    .user(userInfo)
                    .build();
            userInfo.setLibraryCard(libraryCard);
            libraryCardRepository.save(libraryCard);
        }

    }
}
