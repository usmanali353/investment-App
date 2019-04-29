package usmanali.investmentapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import usmanali.investmentapp.AsyncTasks.profit_history_task;

public class Profit_history extends AppCompatActivity {
ListView profit_history_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profit_history_list=findViewById(R.id.profit_history_list);
        new profit_history_task(Profit_history.this,profit_history_list).execute();
    }

}
