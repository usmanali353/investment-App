package usmanali.investmentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import usmanali.investmentapp.AsyncTasks.fetch_withdraw_notifications;
import usmanali.investmentapp.AsyncTasks.get_all_withdraw_notifications_task;

public class Notifications extends AppCompatActivity {
List<user_info> user_infoList;
SharedPreferences prefs;
ListView notification_list;
SwipeRefreshLayout srl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        srl=findViewById(R.id.swipe_refresh);
        user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());
        notification_list=findViewById(R.id.notification_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getStringExtra("role").equals("Customer")) {
            new fetch_withdraw_notifications(this, notification_list,srl).execute(user_infoList.get(0).email);
        }else{
            new get_all_withdraw_notifications_task(this, notification_list,srl).execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(user_infoList.get(0).getUser_type().equals("Admin")){
                startActivity(new Intent(this,Admin_home.class));
                finish();
            }else{
                startActivity(new Intent(this,user_home.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);

    }
}
