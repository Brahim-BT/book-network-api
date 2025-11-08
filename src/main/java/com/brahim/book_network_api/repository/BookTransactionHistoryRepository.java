package com.brahim.book_network_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.brahim.book_network_api.model.BookTransactionHistory;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("SELECT bth FROM BookTransactionHistory bth WHERE bth.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query("SELECT bth FROM BookTransactionHistory bth WHERE bth.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query("SELECT (COUNT(*)>0) AS isBorrowed FROM BookTransactionHistory bth WHERE bth.book.owner.id = :userId AND bth.book.id = :bookId AND bth.returnedApproved = false")
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    @Query("SELECT bth FROM BookTransactionHistory bth WHERE bth.user.id = :userId AND bth.book.id = :bookId AND bth.returned = false AND bth.returnedApproved = false")
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long userId);
    
    @Query("SELECT bth FROM BookTransactionHistory bth WHERE bth.book.id = :bookId AND bth.book.owner.id = :userId AND bth.returned = true AND bth.returnedApproved = false")
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long userId);

}
