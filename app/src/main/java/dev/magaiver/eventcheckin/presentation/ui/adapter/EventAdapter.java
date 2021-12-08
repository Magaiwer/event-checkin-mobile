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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eventcheckin.R;

import dev.magaiver.eventcheckin.domain.model.Event;
import dev.magaiver.eventcheckin.presentation.ui.activity.CheckInActivity;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> events;
    private final Context context;


    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
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
        holder.imageViewEvent.setImageResource(R.drawable.ic_calendar_today_black_24dp);

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CheckInActivity.class);
            intent.putExtra("eventId", events.get(position).getId());
            view.getContext().startActivity(intent);
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

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDateClose = itemView.findViewById(R.id.txtDateClose);
            txtCapacity = itemView.findViewById(R.id.txtCapacity);
            imageViewEvent = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardViewItem);

        }
    }
}
