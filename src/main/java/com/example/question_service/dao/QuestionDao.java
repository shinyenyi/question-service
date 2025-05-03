package com.example.question_service.dao;

import com.example.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    List<Question> findByCategoryIgnoreCase(String category);

    @Query(nativeQuery = true,
            value = "SELECT q.id FROM question q Where q.category ILIKE :category ORDER BY RANDOM() LIMIT :numQ")
    List<Integer> findRandomQuestionsByCategory(String category, int numQ);
}
