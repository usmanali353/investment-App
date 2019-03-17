package usmanali.investmentapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Notifications extends AppCompatActivity {
List<user_info> user_infoList;
SharedPreferences prefs;
ListView notification_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());
        notification_list=findViewById(R.id.notification_list);
        if(getIntent().getStringExtra("role").equals("Customer")) {
            new fetch_withdraw_notifications(this, notification_list).execute(user_infoList.get(0).email);
        }else{
            new get_all_withdraw_notifications_task(this, notification_list).execute();
        }
    }

}
