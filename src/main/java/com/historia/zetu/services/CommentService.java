package com.historia.zetu.services;


import com.historia.zetu.model.Comments;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    List<Comments> saveComment(Comments comment, long historyId);

    List<Comments> getAllComments();

    Comments getCommentById(Long commentId);

    List<Comments> getHistoryCommentsById(long historyId, Long commentId);

    void updateComment(Comments comment);
    List<Comments> getAllTopLevelComments();

    void deleteComment(Long commentId);

    List<Comments> getCommentsByHistoryId(Long historyId);

    List<Comments> getCommentsByParentCommentId(Long parentCommentId);

    List<Comments> getCommentsByCommenter(String commenter);

    List<Comments> getCommentsByCommenterEmail(String commenterEmail);

    List<Comments> getCommentsByCommenterWebsite(String commenterWebsite);

    List<Comments> getCommentsByCommentDate(LocalDateTime commentDate);

    List<Comments> getCommentsByCommentContent(String commentContent);

    List<Comments> getCommentsByCommentAuthor(String commentAuthor);

    List<Comments> getCommentsByCommentId(Long commentId);

    List<Comments> getCommentsByEmail(String commenterEmail);
}
