package com.nowocode.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Lati on 13.01.2016.
 */
public class CreateTaskDialog extends DialogFragment {
    String taskName;
    private CreateTaskDialogInterface mActivity;
    public ImageView redBox;
    public ImageView greenBox;
    public ImageView yellowBox;
    private int priority = 3;
    private float alphaValue = (float) 0.3;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (CreateTaskDialogInterface) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.create_task_dialog,null);
        dialogBuilder.setView(v)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText text = (EditText) getDialog().findViewById(R.id.taskEdit);
                        taskName = text.getText().toString();
                        mActivity.createTask(taskName, priority);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateTaskDialog.this.getDialog().cancel();
            }
        });
        redBox = (ImageView) v.findViewById(R.id.priorityRed);
        greenBox = (ImageView) v.findViewById(R.id.priorityGreen);
        yellowBox = (ImageView) v.findViewById(R.id.priorityYellow);


        /**
         * Determining which Priority has been checked
         * @return 1 = red; 2 = green; 3 = yellow
         * If no Priority is Chosen it's 3 by default
         */

        redBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priority != 1){
                    priority = 1;
                    redBox.setAlpha((float) 1);
                    yellowBox.setAlpha(alphaValue);
                    greenBox.setAlpha(alphaValue);
                    System.out.println("Clicked redbox");
                }
            }
        });

        greenBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priority != 2){
                    priority = 2;
                    redBox.setAlpha((alphaValue));
                    yellowBox.setAlpha(alphaValue);
                    greenBox.setAlpha((float) 1);
                    System.out.println("Clicked green Box");

                }
            }
        });

        yellowBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priority != 3){
                    priority = 3;
                    redBox.setAlpha((alphaValue));
                    yellowBox.setAlpha((float) 1);
                    greenBox.setAlpha(alphaValue);
                    System.out.println("Clicked yellow box");

                }
            }
        });


        System.out.println("Builder created");
        return dialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Started");
    }



}
