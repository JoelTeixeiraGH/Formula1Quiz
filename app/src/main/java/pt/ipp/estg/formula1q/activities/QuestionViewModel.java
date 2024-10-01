package pt.ipp.estg.formula1q.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import pt.ipp.estg.formula1q.database.question.QuestionRepository;
import pt.ipp.estg.formula1q.models.Question;

public class QuestionViewModel extends AndroidViewModel {
    private static MutableLiveData<Question> question = new MutableLiveData<>();
    private QuestionRepository repository;

    public QuestionViewModel(@NonNull Application application, boolean isSharedQuestion, int sharedQuestionId) {
        super(application);
        repository = QuestionRepository.getInstance(application);

        if(isSharedQuestion){
            repository.getQuestionById(result -> {
                        question.setValue(result);
                    },
                    sharedQuestionId);
        }else{
            repository.getRandomQuestion(result -> {
                question.setValue(result);
            });
        }

    }

    public MutableLiveData<Question> getQuestionObservable(){
        return question;
    }

    public static class QuestionViewModelFactory implements ViewModelProvider.Factory{
        private Application application;
        private boolean isSharedQuestion;
        private int sharedQuestionId;

        public QuestionViewModelFactory(Application application, boolean isSharedQuestion, int sharedQuestionId) {
            this.application = application;
            this.isSharedQuestion = isSharedQuestion;
            this.sharedQuestionId = sharedQuestionId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new QuestionViewModel(application, isSharedQuestion, sharedQuestionId);
        }
    }

}
