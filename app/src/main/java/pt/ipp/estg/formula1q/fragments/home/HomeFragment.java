package pt.ipp.estg.formula1q.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.dialogs.QuestionsListDialog;
import pt.ipp.estg.formula1q.models.AnsweredQuestion;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rv_eventList;
    private EventAdapter adapter;
    private HomeViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rv_eventList = root.findViewById(R.id.rv_events);
        rv_eventList.setLayoutManager(new LinearLayoutManager(getContext()));

        EventAdapter.EventClickListener eventClickListener = new EventAdapter.EventClickListener() {
            @Override
            public void onEventClick(List<AnsweredQuestion> answeredQuestionList) {
                new QuestionsListDialog(answeredQuestionList)
                        .show(getActivity().getSupportFragmentManager(), null);
            }
        };

        adapter = new EventAdapter(eventClickListener);
        rv_eventList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getEventListObservable()
                .observe(getViewLifecycleOwner(), eventList -> {
                    if(eventList.size() == 0){
                        root.findViewById(R.id.rv_event_empty_text).setVisibility(View.VISIBLE);
                        rv_eventList.setVisibility(View.GONE);
                    } else {
                        root.findViewById(R.id.rv_event_empty_text).setVisibility(View.GONE);
                        rv_eventList.setVisibility(View.VISIBLE);
                        adapter.setEventList(eventList);
                    }
                });
        return root;
    }
}