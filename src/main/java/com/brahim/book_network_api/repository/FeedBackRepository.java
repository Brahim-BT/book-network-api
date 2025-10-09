package com.brahim.book_network_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.brahim.book_network_api.model.Feedback;

@Repository
public interface FeedBackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f WHERE f.book.id = :bookId")
    Page<Feedback> findAllByBookId(Long bookId, Pageable pageable);

}
