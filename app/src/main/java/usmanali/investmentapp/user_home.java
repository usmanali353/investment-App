package usmanali.investmentapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class user_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<dashboard_model> model;
    RecyclerView list;
    SharedPreferences prefs;
    TextView name,email;

    List<user_info> user_infoList;
    ColorGenerator generator;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         prefs=PreferenceManager.getDefaultSharedPreferences(this);
        generator=ColorGenerator.MATERIAL;
        int iconcolor=generator.getRandomColor();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header_view=LayoutInflater.from(this).inflate(R.layout.nav_header_admin_home,null);
        name=header_view.findViewById(R.id.name_txt);
        email=header_view.findViewById(R.id.email_txt);
        imageView=header_view.findViewById(R.id.imageView);
        name.setText(user_infoList.get(0).Name);
        email.setText(user_infoList.get(0).email);
        String n=user_infoList.get(0).Name.substring(0,1);
        TextDrawable drawable=TextDrawable.builder().beginConfig().width(150).height(150).endConfig().buildRound(n.toUpperCase(),iconcolor);
        imageView.setImageDrawable(drawable);
        navigationView.addHeaderView(header_view);
        list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        model=new ArrayList<>();
        model.add(new dashboard_model(R.drawable.investment_icon,"Your Investment"));
        model.add(new dashboard_model(R.drawable.return_on_investment,"Your Earning"));
        list.setAdapter(new dashboard_adapter(model,user_home.this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
           super.onBackPressed();
        }
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id==R.id.redeem){
           View send_profit_view=LayoutInflater.from(user_home.this).inflate(R.layout.give_profit,null);
           final TextInputEditText profit=send_profit_view.findViewById(R.id.profit_txt);
           TextInputLayout txt=send_profit_view.findViewById(R.id.profit_textinputlayout);
           txt.setHint("Earning");
           AlertDialog send_profit_dialog=new AlertDialog.Builder(user_home.this)
                   .setTitle("Redeem Earning")
                   .setMessage("Enter amount you want to Redeem")
                   .setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                       }
                   }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   }).setView(send_profit_view).create();
           send_profit_dialog.show();
           send_profit_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(profit.getText().toString().isEmpty()){
                       profit.setError("Enter Earning");

                   }else {
                       new send_withdrawal_request_task(user_home.this).execute(String.valueOf(System.currentTimeMillis()),user_infoList.get(0).email+" has requested withdraw of Rs "+profit.getText().toString(),user_infoList.get(0).email,profit.getText().toString(),"No");
                   }
               }
           });
       }else if(id==R.id.signout){
           prefs.edit().remove("user_info").apply();
           finish();
       }else if(id==R.id.withdraw_request){
           startActivity(new Intent(user_home.this,Notifications.class).putExtra("role","Customer"));
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
