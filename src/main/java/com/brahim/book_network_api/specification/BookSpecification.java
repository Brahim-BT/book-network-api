package com.brahim.book_network_api.specification;

import org.springframework.data.jpa.domain.Specification;

import com.brahim.book_network_api.model.Book;

public class BookSpecification {

    public static Specification<Book> withOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), ownerId);
    }
}
