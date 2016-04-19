package com.nowocode.todolist;


import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> taskArray;

    public TaskAdapter(ArrayList<Task> taskList) {
        taskArray = taskList;
    }

    //Sorts Tasks based on their Priority
    public void sortTaskArray(){
        for(int i = 0; i < taskArray.size(); i++){
            for(int j= taskArray.size()-i; j < 0; j--){
                if(taskArray.get(j).getPriority() > taskArray.get(i).getPriority()){
                    Task swapTask = taskArray.get(j);
                    taskArray.set(j, taskArray.get(i));
                    taskArray.set(i, swapTask);
                }
            }

        }
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).inflate(R.layout.taskcard, parent, false);
        return new TaskViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int i) {
        holder.taskName.setText(taskArray.get(i).getTaskName());
        holder.setCurTask(taskArray.get(i));
        holder.taskName.setChecked(false);
        holder.setUpPriority();
        //   holder.description.setText("Course Description");
    }

    @Override
    public int getItemCount() {
        return taskArray.size();
    }


    /**
     * The TaskViewHolder represents each Task in the RecyclerView
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public CheckBox taskName;
        private Task curTask;
        private ImageView priorityStatus;


        public TaskViewHolder(View itemView) {
            super(itemView);
            taskName = (CheckBox) itemView.findViewById(R.id.checkbox_task);
            priorityStatus = (ImageView) itemView.findViewById(R.id.priorityStatus);

            taskName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        onTaskCompleted(isChecked);
                }
            });

        }

        public void setUpPriority(){
            //set the right priority Color

            switch(curTask.getPriority()){
                case 1:
                    priorityStatus.setBackgroundColor(Color.parseColor("#FFD70F00"));
                    break;
                case 2:
                    priorityStatus.setBackgroundColor(Color.parseColor("#FF160FF9"));
                    break;
                case 3:
                    priorityStatus.setBackgroundColor(Color.parseColor("#FF27BE1C"));
                    break;
            }
        }
        //This Method gets called once you click a Checkbox
        public void onTaskCompleted(boolean isChecked){
            //Striking out the Text
            if(isChecked) {
                MainActivity.tasksChecked++;
                taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                MainActivity.removeButton.show();
                MainActivity.tasksToRemove.add(curTask);
                    System.out.println("To remove: " + curTask.getTaskName());
            }
            else {
                taskName.setPaintFlags(taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                MainActivity.tasksChecked--;
                MainActivity.tasksToRemove.remove(curTask);
                if(MainActivity.tasksChecked == 0)
                    MainActivity.removeButton.hide();
            }
        }

        public void setCurTask(Task curTask) {
            this.curTask = curTask;
        }
    }
}

