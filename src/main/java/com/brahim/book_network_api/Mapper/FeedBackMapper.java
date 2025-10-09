package com.brahim.book_network_api.Mapper;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.brahim.book_network_api.dto.FeedbackRequest;
import com.brahim.book_network_api.dto.FeedbackResponse;
import com.brahim.book_network_api.model.Book;
import com.brahim.book_network_api.model.Feedback;
import com.brahim.book_network_api.model.User;

@Service
public class FeedBackMapper {

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder().id(request.bookId()).build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, User user) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), user.getId()))
                .build();
    }

}
