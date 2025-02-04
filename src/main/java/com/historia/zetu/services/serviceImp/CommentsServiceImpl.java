package com.historia.zetu.services.serviceImp;

import com.historia.zetu.Repository.CommentsRepo;
import com.historia.zetu.model.Comments;
import com.historia.zetu.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentService {


    private final CommentsRepo commentsRepository;

    @Override
    public boolean saveComment(Comments comment) {
         commentsRepository.save(comment);
        return true;
    }

    @Override
    public List<Comments> getAllComments() {
        return List.of();
    }

    @Override
    public Comments getCommentById(Long commentId) {
        return commentsRepository.findById(commentId).orElseThrow();
    }

    @Override
    public void updateComment(Comments comment) {

    }

    @Override
    public List<Comments> getAllTopLevelComments() {
        return commentsRepository.findAllByParentCommentIsNull();
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public List<Comments> getCommentsByHistoryId(Long historyId) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByParentCommentId(Long parentCommentId) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommenter(String commenter) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommenterEmail(String commenterEmail) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommenterWebsite(String commenterWebsite) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommentDate(LocalDateTime commentDate) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommentContent(String commentContent) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommentAuthor(String commentAuthor) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByCommentId(Long commentId) {
        return List.of();
    }

    @Override
    public List<Comments> getCommentsByEmail(String commenterEmail) {
        return List.of();
    }


}