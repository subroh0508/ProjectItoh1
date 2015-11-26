package com.example.root.projectitoh1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CounterFragment extends Fragment {
    private static final int START_MIN = 100;
    private int remainTime = START_MIN*60;
    private TextView tvTimer;
    private Button btReset;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_counter, container, false);

        tvTimer = (TextView)rootView.findViewById(R.id.timer);
        btReset = (Button)rootView.findViewById(R.id.reset);

        TimerShow();

        if(timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                Handler handler = new Handler();

                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (remainTime > 0) remainTime--;
                            TimerShow();
                        }
                    });
                }
            }, 1000, 1000);
        }

        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remainTime > 0) remainTime--;
                TimerShow();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainTime = START_MIN*60;
                tvTimer.setText(START_MIN+"min 0sec");
            }
        });

        return rootView;
    }

    public void TimerShow(){
        if (remainTime <= 0) {
            tvTimer.setText("Congratulation!!");
        } else {
            int min = remainTime / 60;
            int sec = remainTime % 60;
            tvTimer.setText(min + "min " + sec + "sec");
        }
    }
}
