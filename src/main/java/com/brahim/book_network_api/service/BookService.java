package com.brahim.book_network_api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brahim.book_network_api.common.PageResponse;
import com.brahim.book_network_api.dto.BookRequest;
import com.brahim.book_network_api.dto.BookResponse;
import com.brahim.book_network_api.dto.BorrowedBookResponse;
import com.brahim.book_network_api.exception.OperationNotPermittedException;
import com.brahim.book_network_api.model.Book;
import com.brahim.book_network_api.model.BookTransactionHistory;
import com.brahim.book_network_api.model.User;
import com.brahim.book_network_api.repository.BookRepository;
import com.brahim.book_network_api.repository.BookTransactionHistoryRepository;
import com.brahim.book_network_api.specification.BookSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

        private final BookRepository bookRepository;
        private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
        private final BookMapper bookMapper;
        private final FileStorageService fileStorageService;

        public Long saveBook(BookRequest request, Authentication connectedUser) {
                User user = (User) connectedUser.getPrincipal();
                Book book = bookMapper.toBook(request);
                book.setOwner(user);
                return bookRepository.save(book).getId();
        }

        public BookResponse findBookById(Long id) {
                return bookRepository.findById(id).map(bookMapper::toBookResponse)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + id));
        }

        public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
                User user = (User) connectedUser.getPrincipal();
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                Page<Book> bookPage = bookRepository.findAllDisplayableBooks(pageable, user.getId());
                List<BookResponse> bookResponses = bookPage.stream().map(bookMapper::toBookResponse).toList();
                return new PageResponse<>(
                                bookResponses,
                                bookPage.getNumber(),
                                bookPage.getSize(),
                                bookPage.getTotalElements(),
                                bookPage.getTotalPages(),
                                bookPage.isFirst(),
                                bookPage.isLast());
        }

        public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
                User user = (User) connectedUser.getPrincipal();
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                Page<Book> bookPage = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
                List<BookResponse> bookResponses = bookPage.stream().map(bookMapper::toBookResponse).toList();
                return new PageResponse<>(
                                bookResponses,
                                bookPage.getNumber(),
                                bookPage.getSize(),
                                bookPage.getTotalElements(),
                                bookPage.getTotalPages(),
                                bookPage.isFirst(),
                                bookPage.isLast());
        }

        public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size,
                        Authentication connectedUser) {
                User user = (User) connectedUser.getPrincipal();
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                Page<BookTransactionHistory> allBorrowedBooksPage = bookTransactionHistoryRepository
                                .findAllBorrowedBooks(pageable, user.getId());
                List<BorrowedBookResponse> borrowedBookResponses = allBorrowedBooksPage.stream()
                                .map(bookMapper::toBorrowedBookResponse).toList();
                return new PageResponse<>(
                                borrowedBookResponses,
                                allBorrowedBooksPage.getNumber(),
                                allBorrowedBooksPage.getSize(),
                                allBorrowedBooksPage.getTotalElements(),
                                allBorrowedBooksPage.getTotalPages(),
                                allBorrowedBooksPage.isFirst(),
                                allBorrowedBooksPage.isLast());
        }

        public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size,
                        Authentication connectedUser) {
                User user = (User) connectedUser.getPrincipal();
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                Page<BookTransactionHistory> allBorrowedBooksPage = bookTransactionHistoryRepository
                                .findAllReturnedBooks(pageable, user.getId());
                List<BorrowedBookResponse> borrowedBookResponses = allBorrowedBooksPage.stream()
                                .map(bookMapper::toBorrowedBookResponse).toList();
                return new PageResponse<>(
                                borrowedBookResponses,
                                allBorrowedBooksPage.getNumber(),
                                allBorrowedBooksPage.getSize(),
                                allBorrowedBooksPage.getTotalElements(),
                                allBorrowedBooksPage.getTotalPages(),
                                allBorrowedBooksPage.isFirst(),
                                allBorrowedBooksPage.isLast());
        }

        public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                User user = (User) connectedUser.getPrincipal();
                if (!Objects.equals(book.getOwner().getId(), user.getId())) {
                        throw new OperationNotPermittedException("You can not upadate others book shareable status");
                }
                book.setShareable(!book.isShareable());
                return bookRepository.save(book).getId();
        }

        public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                User user = (User) connectedUser.getPrincipal();
                if (!Objects.equals(book.getOwner().getId(), user.getId())) {
                        throw new OperationNotPermittedException("You can not upadate others book archived status");
                }
                book.setArchived(!book.isArchived());
                return bookRepository.save(book).getId();
        }

        public Long borrowBook(Long bookId, Authentication connectedUser) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                if (book.isArchived() || !book.isShareable()) {
                        throw new OperationNotPermittedException(
                                        "The requested book cannot be borrowed since it is not shareable or archived");
                }
                User user = (User) connectedUser.getPrincipal();
                if (Objects.equals(book.getOwner().getId(), user.getId())) {
                        throw new OperationNotPermittedException("You can not borrow your own book");
                }
                final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId,
                                user.getId());
                if (isAlreadyBorrowed) {
                        throw new OperationNotPermittedException("The requested book is already borrowed");
                }
                BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                                .book(book)
                                .user(user)
                                .returnedApproved(false)
                                .build();
                return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
        }

        public Long ReturnBorrowedBook(Long bookId, Authentication connectedUser) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                if (book.isArchived() || !book.isShareable()) {
                        throw new OperationNotPermittedException(
                                        "The requested book cannot be borrowed since it is not shareable or archived");
                }
                User user = (User) connectedUser.getPrincipal();
                if (Objects.equals(book.getOwner().getId(), user.getId())) {
                        throw new OperationNotPermittedException("You can not borrow or return your own book");
                }

                BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                                .findByBookIdAndUserId(bookId, user.getId())
                                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

                bookTransactionHistory.setReturned(true);
                return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
        }

        public Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                if (book.isArchived() || !book.isShareable()) {
                        throw new OperationNotPermittedException(
                                        "The requested book cannot be borrowed since it is not shareable or archived");
                }
                User user = (User) connectedUser.getPrincipal();
                if (Objects.equals(book.getOwner().getId(), user.getId())) {
                        throw new OperationNotPermittedException("You can not borrow or return your own book");
                }
                BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                                .findByBookIdAndOwnerId(bookId, user.getId())
                                .orElseThrow(() -> new OperationNotPermittedException(
                                                "The book is not yet returned. You can not approve its return"));
                bookTransactionHistory.setReturnedApproved(true);
                return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
        }

        public void uploadBookCoverPicture(Long bookId, Authentication connectedUser, MultipartFile file) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + bookId));
                User user = (User) connectedUser.getPrincipal();
                var bookCover = fileStorageService.saveFile(user, file);
                book.setBookCover(bookCover);
                bookRepository.save(book);
        }

}
