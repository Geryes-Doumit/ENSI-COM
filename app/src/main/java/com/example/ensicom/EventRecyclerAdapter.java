package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    private ArrayList<EventPost> eventsList;

    public EventRecyclerAdapter(ArrayList<EventPost> eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventPost event = eventsList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventDesc.setText(event.getEventDescription());
        holder.eventLoc.setText(event.getEventLocation());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView eventDate, eventName, eventDesc, eventLoc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventname);
            eventDesc = itemView.findViewById(R.id.eventdesc);
            eventLoc = itemView.findViewById(R.id.eventloc);
        }
    }
}

