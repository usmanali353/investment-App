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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Admin_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
 Button add_user,send_profit;
 SharedPreferences prefs;
 List<user_info> user_infoList;
 TextView name,email;
 ImageView roundicon;
 ColorGenerator generator;
 Button add_refered_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        generator=ColorGenerator.MATERIAL;
        int iconcolor=generator.getRandomColor();

        prefs=PreferenceManager.getDefaultSharedPreferences(this);
       user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header_view=LayoutInflater.from(this).inflate(R.layout.nav_header_admin_home,null);
        name=header_view.findViewById(R.id.name_txt);
        email=header_view.findViewById(R.id.email_txt);
        roundicon=header_view.findViewById(R.id.imageView);
        name.setText(user_infoList.get(0).Name);
        email.setText(user_infoList.get(0).email);
        TextDrawable drawable=TextDrawable.builder().beginConfig().width(150).height(150).endConfig().buildRound(user_infoList.get(0).Name.substring(0,1).toUpperCase(),iconcolor);
        roundicon.setImageDrawable(drawable);
        navigationView.addHeaderView(header_view);
        add_user=findViewById(R.id.add_user);
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View add_user_view=LayoutInflater.from(Admin_home.this).inflate(R.layout.add_user_layout,null);
                final TextInputEditText name=add_user_view.findViewById(R.id.name_txt);
                final TextInputEditText email=add_user_view.findViewById(R.id.email_txt);
                final TextInputEditText password=add_user_view.findViewById(R.id.password_txt);
                final TextInputEditText investment=add_user_view.findViewById(R.id.investment_txt);
                final TextInputEditText father_name=add_user_view.findViewById(R.id.father_name_txt);
                final TextInputEditText cnic=add_user_view.findViewById(R.id.cnic_txt);
                final TextInputEditText percentage_profit=add_user_view.findViewById(R.id.profit_txt);
                String[] ITEMS = {"Customer","IB"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin_home.this, android.R.layout.simple_spinner_item, ITEMS);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                final MaterialSpinner spinner = add_user_view.findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
                AlertDialog add_user_dialog=new AlertDialog.Builder(Admin_home.this)
                .setTitle("Add User")
                 .setCancelable(false)
                  .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setView(add_user_view)
                        .create();
                add_user_dialog.show();
                add_user_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         if(name.getText().toString().isEmpty()){
                             name.setError("Name is Required");
                         }else if(email.getText().toString().isEmpty()){
                             email.setError("Email is Required");
                         }else if(password.getText().toString().isEmpty()){
                             password.setError("Password is Required");
                         }else if(investment.getText().toString().isEmpty()){
                             investment.setError("Investment is Required");
                         }else if(password.getText().toString().length()<6){
                             password.setError("Password too short");
                         }else if(Integer.valueOf(investment.getText().toString())<1000){
                             investment.setError("Minimium investment that can be made is Rs 1000");
                         }else if(father_name.getText().toString().isEmpty()){
                             father_name.setError("Father Name is Required");
                         }else if(cnic.getText().toString().isEmpty()){
                             cnic.setError("CNIC is Required");
                         }else if(cnic.getText().toString().length()<13){
                             cnic.setError("CNIC is too short");
                         }else if(percentage_profit.getText().toString().isEmpty()){
                             percentage_profit.setError("Percentage Profit is Required");
                         }else if(Integer.valueOf(percentage_profit.getText().toString())<10){
                             percentage_profit.setError("Profit Percentage should be atleast 10 percent");
                         }else if(spinner.getSelectedItem().toString().equals("User Type")) {
                             spinner.setError("Select User Type");
                         }else {
                             new register_task(Admin_home.this).execute(name.getText().toString(),email.getText().toString(),password.getText().toString(),investment.getText().toString(),father_name.getText().toString(),cnic.getText().toString(),spinner.getSelectedItem().toString(),percentage_profit.getText().toString(),"");
                         }
                    }
                });

            }
        });
        send_profit=findViewById(R.id.send_profit);
        send_profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new update_profit_task(Admin_home.this).execute(String.valueOf(user_infoList.get(0).profit_percentage));
                }
        });
        add_refered_customer=findViewById(R.id.add_refered_customer);
        add_refered_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new get_IB_customer_task(Admin_home.this).execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
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
           if(id==R.id.signout){
               prefs.edit().remove("user_info").apply();
               finish();
           }else if(id==R.id.withdraw_request){
               startActivity(new Intent(Admin_home.this,Notifications.class).putExtra("role","Admin"));
           }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
