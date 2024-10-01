package pt.ipp.estg.formula1q.models;

import androidx.annotation.Nullable;

import java.util.List;

public class Event {
    private String eventName;
    private String eventDate;
    private List<AnsweredQuestion> questions;

    public Event(){}

    public Event(String eventName, @Nullable String eventDate, List<AnsweredQuestion> questions){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.questions = questions;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        if (eventDate.length() > 4){
            return eventDate.substring(0, eventDate.length()-7)+":00";
        }
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public List<AnsweredQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AnsweredQuestion> questions) {
        this.questions = questions;
    }

    public String getQuestionCount() {
        return String.valueOf(questions.size());
    }
}
