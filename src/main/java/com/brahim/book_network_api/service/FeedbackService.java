package com.brahim.book_network_api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.brahim.book_network_api.Mapper.FeedBackMapper;
import com.brahim.book_network_api.common.PageResponse;
import com.brahim.book_network_api.dto.FeedbackRequest;
import com.brahim.book_network_api.dto.FeedbackResponse;
import com.brahim.book_network_api.exception.OperationNotPermittedException;
import com.brahim.book_network_api.model.Book;
import com.brahim.book_network_api.model.Feedback;
import com.brahim.book_network_api.model.User;
import com.brahim.book_network_api.repository.BookRepository;
import com.brahim.book_network_api.repository.FeedBackRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedBackRepository feedbackRepository;
    private final FeedBackMapper feedBackMapper;

    public Long saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with id " + request.bookId()));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException(
                    "You can not give feedback for an archived or non-shareable book");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can not give feedback to your own book");
        }
        Feedback feedback = feedBackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Long bookId, int page, int size,
            Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedBackMapper.toFeedbackResponse(feedback, user))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast());
    }

}
