package pt.ipp.estg.formula1q.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.formula1q.models.AnsweredQuestion;
import pt.ipp.estg.formula1q.models.Event;
import pt.ipp.estg.formula1q.models.Question;


public abstract class Firebase {

    public static DatabaseReference getUserRef(){
        return FirebaseDatabase
                .getInstance()
                .getReference(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid()
                );
    }

    public static DatabaseReference getScoreRef(){
        return getUserRef()
                .child("score");
    }

    public static void addPoints(int points){
        getScoreRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int prevPoints = snapshot.getValue(Integer.class).intValue();
                        getScoreRef()
                                .setValue(points + prevPoints);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // no-op
                    }
                });
    }

    public static void addAnsweredQuestion(AnsweredQuestion answeredQuestion, String eventName,
                                           String eventDate){

        getEventRef(eventName, eventDate)
                .child("eventName")
                .setValue(eventName);

        getEventRef(eventName, eventDate)
                .child("eventDate")
                .setValue(eventDate);

        getEventRef(eventName, eventDate)
                .child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<AnsweredQuestion> newList = (List<AnsweredQuestion>) snapshot.getValue();
                        if(newList == null){
                            newList = new ArrayList<>();
                        }
                        newList.add(answeredQuestion);

                        getEventRef(eventName, eventDate)
                                .child("questions")
                                .setValue(newList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // no-op
                    }
                });
    }

    public static DatabaseReference getEventsRef(){
        return getUserRef()
                .child("events");
    }

    private static DatabaseReference getEventRef(String eventName, String eventDate){
        return getEventsRef()
                .child(eventName.concat(eventDate));
    }

    public static void initializeUserIfNotExists() {
        getUserRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            getUserRef()
                                    .child("score").setValue(0);
                            getUserRef()
                                    .child("displayName")
                                    .setValue(FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getEmail()
                                            .split("@")[0]
                                    );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
