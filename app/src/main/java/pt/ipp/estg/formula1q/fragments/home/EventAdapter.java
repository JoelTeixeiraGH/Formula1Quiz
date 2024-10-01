package pt.ipp.estg.formula1q.fragments.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.models.AnsweredQuestion;
import pt.ipp.estg.formula1q.models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> eventList;
    private EventClickListener eventClickListener;

    public interface EventClickListener{
        void onEventClick(List<AnsweredQuestion> answeredQuestionList);
    }

    public EventAdapter(EventClickListener eventClickListener) {
        eventList = new ArrayList<>();
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_event_item, parent, false);

        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event currEvent = eventList.get(position);
        if(currEvent.getQuestions() != null) {
            holder.tv_answeredQuestionsCount.setText(currEvent.getQuestionCount());
            holder.tv_eventDate.setText(currEvent.getEventDate());
            holder.tv_eventName.setText(currEvent.getEventName());
        }
    }

    public void setEventList(List<Event> eventList){
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_eventName, tv_eventDate, tv_answeredQuestionsCount;

        public ViewHolder(@NonNull View v) {
            super(v);
            this.tv_eventName = v.findViewById(R.id.rv_event_name);
            this.tv_eventDate = v.findViewById(R.id.rv_event_date);
            this.tv_answeredQuestionsCount = v.findViewById(R.id.rv_event_answered_questions_count);

            v.setOnClickListener(v1 -> {
                eventClickListener.onEventClick(eventList.get(getAdapterPosition()).getQuestions());
            });
        }
    }
}
