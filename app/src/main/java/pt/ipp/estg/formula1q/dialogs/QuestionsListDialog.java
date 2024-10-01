package pt.ipp.estg.formula1q.dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.fragments.home.EventAdapter;
import pt.ipp.estg.formula1q.models.AnsweredQuestion;
import pt.ipp.estg.formula1q.models.Question;

public class QuestionsListDialog extends DialogFragment {
    private RecyclerView rv_questionsList;
    private QuestionsListAdapter adapter;

    public QuestionsListDialog(List<AnsweredQuestion> answeredQuestionList) {
        adapter = new QuestionsListAdapter(answeredQuestionList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_questions_list, container);

        rv_questionsList = view.findViewById(R.id.rv_questionsList);
        rv_questionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_questionsList.setAdapter(adapter);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }


    /**
     * RecyclerView adapter
     */
    private class QuestionsListAdapter extends RecyclerView.Adapter<QuestionsListDialog.QuestionsListAdapter.QuestionViewHolder>{
        private List<AnsweredQuestion> questionsList;

        public QuestionsListAdapter(List<AnsweredQuestion> questionsList){
            this.questionsList = questionsList;
        }

        @NonNull
        @Override
        public QuestionsListAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_questions_list_item, parent, false);

            return new QuestionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionsListAdapter.QuestionViewHolder holder, int position) {
            holder.tv_question.setText(questionsList.get(position).getQuestion());
            holder.tv_correctAnswer.setText("Correct Answer: " + questionsList.get(position).getStringAnswer());
            holder.tv_userAnswer.setText("Your Answer: " + questionsList.get(position).getStringUserAnswer());
            holder.tv_points.setText("Earned Points: " + String.valueOf(questionsList.get(position).getAwardedPoints()));
        }

        @Override
        public int getItemCount() {
            return questionsList.size();
        }

        public class QuestionViewHolder extends RecyclerView.ViewHolder {
            TextView tv_question, tv_userAnswer, tv_correctAnswer, tv_points;

            public QuestionViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_question = itemView.findViewById(R.id.rv_question_title);
                tv_userAnswer = itemView.findViewById(R.id.rv_question_userAnswer);
                tv_correctAnswer = itemView.findViewById(R.id.rv_question_correctAnswer);
                tv_points = itemView.findViewById(R.id.rv_question_points);
            }
        }
    }
}
