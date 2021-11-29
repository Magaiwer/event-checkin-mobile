package dev.magaiver.eventcheckin.presentation.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.eventcheckin.R;

import dev.magaiver.eventcheckin.domain.model.Event;

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
        holder.txtDate.setText(events.get(position).dateTimeStr());
        holder.txtCapacity.setText(events.get(position).getCapacity());
        holder.imageViewEvent.setImageResource(R.drawable.ic_account_balance_black_24dp);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatus;
        TextView txtEventName;
        TextView txtDate;
        TextView txtCapacity;
        ImageView imageViewEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtCapacity = itemView.findViewById(R.id.txtCapacity);
            imageViewEvent = itemView.findViewById(R.id.imageViewEvent);

        }
    }
}
