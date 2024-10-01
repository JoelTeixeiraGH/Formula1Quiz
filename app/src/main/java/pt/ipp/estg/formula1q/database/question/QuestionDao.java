package pt.ipp.estg.formula1q.database.question;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import pt.ipp.estg.formula1q.models.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert
    void insert(Question question);

    @Update
    void update(Question question);

    @Query("DELETE FROM Question")
    void deleteAllQuestions();

    @Query("SELECT * FROM Question WHERE NOT answered")
    List<Question> getAll();

    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestionById(int id);

    @Query("UPDATE Question SET answered = 1 WHERE id = :id")
    void setAsAnswered(int id);
}
