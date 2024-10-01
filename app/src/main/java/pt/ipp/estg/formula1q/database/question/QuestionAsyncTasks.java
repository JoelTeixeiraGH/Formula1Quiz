package pt.ipp.estg.formula1q.database.question;

import android.os.AsyncTask;

import pt.ipp.estg.formula1q.models.Question;

import java.util.Collections;
import java.util.List;

public abstract class QuestionAsyncTasks {

    public interface AsyncTaskListener<T> {
        void onComplete(T result);
    }

    public static class GetRandomQuestion extends AsyncTask<Void, Void, Question>{
        private AsyncTaskListener<Question> asyncTaskListener;
        private QuestionDao dao;

        public GetRandomQuestion(QuestionDao dao, AsyncTaskListener<Question>  asyncTaskListener) {
            super();
            this.asyncTaskListener = asyncTaskListener;
            this.dao = dao;
        }

        @Override
        protected Question doInBackground(Void... voids) {
            Question question;
            List<Question> shuffledQuestionList = dao.getAll();
            if(shuffledQuestionList.size() > 0) {
                Collections.shuffle(shuffledQuestionList);
                question = shuffledQuestionList.get(0);
            } else {
                question = null;
            }
            return question;
        }

        @Override
        protected void onPostExecute(Question question) {
            super.onPostExecute(question);
            asyncTaskListener.onComplete(question);
        }
    }

    public static class GetQuestionById extends AsyncTask<Integer, Void, Question>{
        private AsyncTaskListener<Question> asyncTaskListener;
        private QuestionDao dao;

        public GetQuestionById(QuestionDao dao, AsyncTaskListener<Question>  asyncTaskListener) {
            super();
            this.asyncTaskListener = asyncTaskListener;
            this.dao = dao;
        }

        @Override
        protected Question doInBackground(Integer...ints ) {
            return dao.getQuestionById(ints[0]);
        }

        @Override
        protected void onPostExecute(Question question) {
            super.onPostExecute(question);
            asyncTaskListener.onComplete(question);
        }
    }

    public static class SetAnswered extends AsyncTask<Integer, Void, Void> {
        private QuestionDao dao;

        public SetAnswered(QuestionDao dao) {
            super();
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            dao.setAsAnswered(integers[0]);
            return null;
        }
    }

}
