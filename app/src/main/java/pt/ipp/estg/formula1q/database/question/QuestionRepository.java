package pt.ipp.estg.formula1q.database.question;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import pt.ipp.estg.formula1q.database.Room;
import pt.ipp.estg.formula1q.models.Question;

public class QuestionRepository {
    private static QuestionDao dao;
    private static QuestionRepository instance;
    private static MutableLiveData<Boolean> hasMoreQuestions;

    private QuestionRepository(Context context){
        dao = Room.getInstance(context).questionDao();
        hasMoreQuestions = new MutableLiveData<>();
        hasMoreQuestions.setValue(true);
    }

    public static synchronized QuestionRepository getInstance(Context context){
        if (instance == null){
            instance = new QuestionRepository(context);
        }
        return instance;
    }

    public void getRandomQuestion(QuestionAsyncTasks.AsyncTaskListener<Question> listener){
        new QuestionAsyncTasks.GetRandomQuestion(dao, listener).execute();
    }

    public void getQuestionById(QuestionAsyncTasks.AsyncTaskListener<Question> listener, int id){
        new QuestionAsyncTasks.GetQuestionById(dao, listener).execute(new Integer[]{id});
    }


    public void setAnswered(int id) {
        new QuestionAsyncTasks.SetAnswered(dao).execute(id);
    }

    public void setHasMoreQuestions(boolean b) {
        instance.hasMoreQuestions.postValue(b);
    }

    public MutableLiveData<Boolean> getHasMoreQuestionsObservable(){
        return hasMoreQuestions;
    }
}
