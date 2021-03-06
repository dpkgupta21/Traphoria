package com.app.traphoria.task.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.model.TaskDTO;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.DetailsViewHolder> {

    private Context context;
    private List<TaskDTO> taskList;
    private static MyClickListener myClickListener;


    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView status_pin;
        TextView task;
        TextView task_description;
        TextView shared_with;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            status_pin = (ImageView) itemView.findViewById(R.id.status_pin);
            task = (TextView) itemView.findViewById(R.id.task);
            task_description = (TextView) itemView.findViewById(R.id.task_description);
            shared_with = (TextView) itemView.findViewById(R.id.shared_with);


            status_pin.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public TaskAdapter(Context context, List<TaskDTO> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public void setTaskList(List<TaskDTO> taskList){
        this.taskList = taskList;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }




    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


        holder.shared_with.setText(taskList.get(position).getShared_with());
        holder.task.setText(taskList.get(position).getTitle());
        holder.task_description.setText(taskList.get(position).getDescription());
        if (taskList.get(position).isStatus()) {
            holder.status_pin.setImageResource(R.drawable.check_pin_icon);
        } else {
            holder.status_pin.setImageResource(R.drawable.pin_icon);
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
