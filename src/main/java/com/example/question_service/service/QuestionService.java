package com.example.question_service.service;

import com.example.question_service.dao.QuestionDao;
import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.ResponseAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategoryIgnoreCase(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Question> addQuestion(Question question) {
        try {
            return new ResponseEntity<>(questionDao.save(question), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, int numQ) {

        return new ResponseEntity<>(questionDao.findRandomQuestionsByCategory(category, numQ), HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestions(List<Integer> questionIds) {
        List<Question> questionsFromDB = questionDao.findAllById(questionIds);
        List<QuestionWrapper> questionsForUser = getQuestionWrapperList(questionsFromDB);
        if (questionsForUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
        }
    }

    private List<QuestionWrapper> getQuestionWrapperList(List<Question> questionsFromDB) {
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for (Question question : questionsFromDB) {
            QuestionWrapper questionWrapper = new QuestionWrapper(
                    question.getId(),
                    question.getQuestionTitle(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4()
            );
            questionsForUser.add(questionWrapper);
        }
        return questionsForUser;
    }

    public ResponseEntity<Integer> getScore(List<ResponseAnswer> responseAnswers) {
        int score = 0;
        for (ResponseAnswer responseAnswer : responseAnswers) {
            Question question = questionDao.findById(responseAnswer.getQuestionId()).orElse(null);
            if (question != null && question.getRightAnswer().equalsIgnoreCase(responseAnswer.getResponseAnswer())) {
                score++;
            }
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
