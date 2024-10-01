package pt.ipp.estg.formula1q.fragments.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.formula1q.database.Firebase;
import pt.ipp.estg.formula1q.models.Event;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Event>> eventListObservable;

    public HomeViewModel() {
        eventListObservable = new MutableLiveData<>();

        Firebase.getEventsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()){
                    eventList.add(child.getValue(Event.class));
                }
                eventListObservable.setValue(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //no-op
            }
        });
    }

    public LiveData<List<Event>> getEventListObservable() {
        return eventListObservable;
    }
}