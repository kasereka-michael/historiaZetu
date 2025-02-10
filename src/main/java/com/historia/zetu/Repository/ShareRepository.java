package com.historia.zetu.Repository;

import com.historia.zetu.model.Reads;
import com.historia.zetu.model.Shares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Shares, Long> {
    boolean existsByStoryHistoryId(long storyId);
}
