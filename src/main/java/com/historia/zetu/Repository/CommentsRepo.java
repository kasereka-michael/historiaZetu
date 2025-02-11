package com.historia.zetu.Repository;

import com.historia.zetu.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long> {
    List<Comments> findAllByParentCommentIsNull();

    List<Comments> findAllByHistory_HistoryIdAndCommentId(long historyId, Long commentId);
}
