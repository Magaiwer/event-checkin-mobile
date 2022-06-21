package dev.magaiver.eventcheckin.presentation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eventcheckin.R;

import java.util.List;

import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.presentation.ui.activity.CheckInActivity;
import dev.magaiver.eventcheckin.presentation.ui.listener.SwitchChangeListener;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> events;
    private final Context context;
    private static SwitchChangeListener switchChangeListener;

    public EventAdapter(List<Event> events, Context context, SwitchChangeListener changeListener) {
        this.events = events;
        this.context = context;
        switchChangeListener = changeListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_event_list_item, parent, false);
        return new EventViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.txtEventName.setText(events.get(position).getName());
        holder.txtEventDescription.setText(events.get(position).getDescription());
        holder.txtDate.setText(String.format("Date: %s", events.get(position).dateTimeStr()));
        holder.txtDateClose.setText(String.format("Subscription until: %s", events.get(position).dateCloseStr()));
        holder.txtStatus.setText(events.get(position).status());
//        holder.txtCapacity.setText(events.get(position).getCapacity());
        String eventId = events.get(position).getId();
        holder.imageViewEvent.setImageResource(R.drawable.ic_calendar_today_black_24dp);
        holder.swSubscribe.setEnabled(events.get(position).isOpen());
        holder.swSubscribe.setChecked(events.get(position).isSubscribed());

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CheckInActivity.class);
            intent.putExtra("eventId", eventId);
            view.getContext().startActivity(intent);
        });

        holder.swSubscribe.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            switchChangeListener.switchChangeListener(events.get(position), isChecked);
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatus;
        TextView txtEventName;
        TextView txtEventDescription;
        TextView txtDate;
        TextView txtDateClose;
        TextView txtCapacity;
        ImageView imageViewEvent;
        CardView cardView;
        SwitchCompat swSubscribe;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDateClose = itemView.findViewById(R.id.txtDateClose);
            //txtCapacity = itemView.findViewById(R.id.txtCapacity);
            imageViewEvent = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardViewItem);
            swSubscribe = itemView.findViewById(R.id.swSubscribe);
        }
    }
}
