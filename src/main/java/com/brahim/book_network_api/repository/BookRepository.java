package com.brahim.book_network_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.brahim.book_network_api.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM Book b WHERE b.archived = false AND b.shareable = true AND b.owner.id != :userId")
    Page<Book> findAllDisplayableBooks(Pageable pageable, Long userId);
}
