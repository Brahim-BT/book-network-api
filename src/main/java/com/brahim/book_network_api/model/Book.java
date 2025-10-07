package com.brahim.book_network_api.model;

import java.beans.Transient;
import java.util.List;

import com.brahim.book_network_api.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public Double getRating() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rating = feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
        Double roundedRating = Math.round(rating * 10.0) / 10.0;
        return roundedRating;
    }
}
