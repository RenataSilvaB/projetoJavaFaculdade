package com.example.projeto;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStartStop, btnClearList;
    private ListView lvTimes;

    private boolean isRunning = false;
    private long startTime = 0;
    private Handler handler = new Handler();

    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                int hours = (int) (elapsedTime / 3600000);
                int minutes = (int) (elapsedTime % 3600000) / 60000;
                int seconds = (int) (elapsedTime % 60000) / 1000;

                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tvTimer.setText(time);

                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnClearList = findViewById(R.id.btnClearList);
        lvTimes = findViewById(R.id.lvTimes);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeList);
        lvTimes.setAdapter(adapter);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    isRunning = false;
                    btnStartStop.setText("Iniciar");

                    timeList.add(tvTimer.getText().toString());
                    adapter.notifyDataSetChanged();
                } else {

                    tvTimer.setText("00:00:00");
                    isRunning = true;
                    startTime = System.currentTimeMillis();
                    handler.post(timerRunnable);
                    btnStartStop.setText("Parar");
                }

            }
        });

        btnClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmar")
                        .setMessage("Tem certeza que deseja apagar todos os tempos?")
                        .setPositiveButton("Sim", (dialog, which) -> {

                            timeList.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Lista apagada!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });
    }
}