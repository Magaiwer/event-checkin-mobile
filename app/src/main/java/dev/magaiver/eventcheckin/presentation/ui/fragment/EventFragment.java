package dev.magaiver.eventcheckin.presentation.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventcheckin.R;

import java.util.ArrayList;
import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.presentation.ui.adapter.EventAdapter;
import dev.magaiver.eventcheckin.presentation.view.EventViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventFragment extends Fragment {

    private RecyclerView recyclerEvent;
    private EventViewModel eventViewModel;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
         eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEventList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event_list_item, container, false);
        recyclerEvent = view.findViewById(R.id.recyclerViewEvent);
        recyclerEvent.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    private void loadEventList() {
        new Thread() {
            @Override
            public void run() {

                List<Event> events = new ArrayList<>();
                events = eventViewModel.findAll();
                EventAdapter eventAdapter = new EventAdapter(events , requireContext());

                new Handler(Looper.getMainLooper()).post(() -> {
                    recyclerEvent.setAdapter(eventAdapter);
                });
            }
        }.start();
    }
}
