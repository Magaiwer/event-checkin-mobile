package dev.magaiver.eventcheckin.presentation.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
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

import dev.magaiver.eventcheckin.core.App;
import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.domain.model.LoggedInUser;
import dev.magaiver.eventcheckin.domain.model.StatusSub;
import dev.magaiver.eventcheckin.domain.model.Subscription;
import dev.magaiver.eventcheckin.domain.repository.LoginRepository;
import dev.magaiver.eventcheckin.presentation.ui.adapter.EventAdapter;
import dev.magaiver.eventcheckin.presentation.ui.listener.SwitchChangeListener;
import dev.magaiver.eventcheckin.presentation.view.EventViewModel;
import dev.magaiver.eventcheckin.presentation.view.SubscriptionViewModel;

@RequiresApi(api = Build.VERSION_CODES.O)
@SuppressLint("NotifyDataSetChanged")
public class EventFragment extends Fragment implements SwitchChangeListener {

    private RecyclerView recyclerEvent;
    private EventViewModel eventViewModel;
    private SubscriptionViewModel subViewModel;

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        subViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        eventViewModel.findAllLiveData().observe(this, events -> {
            events.forEach(e -> {
                boolean isSubscribed = subViewModel.verifyAlreadySubscribed(e.getId());
                e.setSubscribed(isSubscribed);
            });

            EventAdapter eventAdapter = new EventAdapter(events, requireContext(), this);
            recyclerEvent.setAdapter(eventAdapter);
            eventAdapter.notifyDataSetChanged();
        });
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

    @Override
    public void switchChangeListener(Event event, boolean isChecked) {
        LoggedInUser user = LoginRepository.getInstance(App.getInstance()).getUser();
        String status = isChecked ? StatusSub.ENABLED.name() : StatusSub.CANCELED.name();
        Subscription sub = new Subscription(event.getId(), user.getEmail(), status);
        subViewModel.subscribeEvent(sub);
        System.out.println(event);
        System.out.println(isChecked);
    }
}
