package usmanali.investmentapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import usmanali.investmentapp.AsyncTasks.get_all_withdraw_notifications_task;

public class Admin_notifications extends AppCompatActivity {
    ExpandableListView expandable_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        expandable_list=findViewById(R.id.expandable_list);
        new get_all_withdraw_notifications_task(Admin_notifications.this,expandable_list).execute();
    }

}
