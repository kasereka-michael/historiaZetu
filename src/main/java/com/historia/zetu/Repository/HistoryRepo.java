package com.historia.zetu.Repository;

import com.historia.zetu.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface HistoryRepo extends JpaRepository<Story, Long> {
    Story findFirstByOrderByHistoryIdDesc();
    @Query("SELECT h FROM Story h WHERE LOWER(CAST(h.historyContent as string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Story> findByKeywordInHistoryContent(@Param("keyword") String keyword);

    @Query("SELECT h FROM Story h JOIN h.hastTags tags WHERE tags IN :hashtags")
    Page<Story> findByHashtagsIn(@Param("hashtags") List<String> hashtags, Pageable pageable);


    // Custom query to retrieve Story and order them by date
    @Query("SELECT h FROM Story h ORDER BY h.historyDate DESC")
    List<Story> findAllOrderByDateDesc();

    // Optionally, retrieve by specific year and month
    @Query("SELECT h FROM Story h WHERE YEAR(h.historyDate) = :year AND MONTH(h.historyDate) = :month")
    List<Story> findByYearAndMonth(int year, int month);

    Page<Story> findByStoryTitleContainingIgnoreCaseOrHistoryCategoryContainingIgnoreCase(
            String storyTitle, String historyCategory, Pageable pageable);


    Page<Story> findAllByHistoryCategory(Pageable pageable, String category);

    @Query("SELECT s FROM Story s WHERE s.historyCategory = :category AND s.id <> :id")
    Page<Story> findByHistoryCategoryAndIdNot(@Param("category") String category, @Param("id") long id, Pageable pageable);

}
