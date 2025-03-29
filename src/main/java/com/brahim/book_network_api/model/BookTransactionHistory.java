package com.brahim.book_network_api.model;

import com.brahim.book_network_api.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private boolean returned;
    private boolean returnedApproved;
}
