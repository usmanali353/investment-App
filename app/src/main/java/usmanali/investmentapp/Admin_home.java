package usmanali.investmentapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import usmanali.investmentapp.AsyncTasks.delete_customer_task;
import usmanali.investmentapp.AsyncTasks.get_IB_customer_task;
import usmanali.investmentapp.AsyncTasks.login_task;
import usmanali.investmentapp.AsyncTasks.register_task;
import usmanali.investmentapp.AsyncTasks.send_withdrawal_request_task;
import usmanali.investmentapp.AsyncTasks.update_profit_task;

public class Admin_home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DatePickerDialog.OnDateSetListener {
 SharedPreferences prefs;
 ArrayList<user_info> user_infoList;
 TextView name,email;
 ImageView roundicon;
 ColorGenerator generator;
 int day,month,year;
     Button select_date;
     DatePickerDialog datePickerDialog;
     int images[]={R.drawable.add_customer,R.drawable.add_ib,R.drawable.share_profit,R.drawable.add_refered_customer,R.drawable.remove_user};
     String texts[]={"Add Customer","Add IB","Share Profit","Add Refered Customer","Remove Customer"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        generator = ColorGenerator.MATERIAL;
        int iconcolor = generator.getRandomColor();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        user_infoList = new Gson().fromJson(prefs.getString("user_info", ""), new TypeToken<ArrayList<user_info>>() {}.getType());
        Log.e("list",prefs.getString("user_info", "NO DATA FOUND"));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 1) {
                                add_ib();
                            } else if (index == 3) {
                                new get_IB_customer_task(Admin_home.this).execute();
                            } else if (index == 2) {
                                new update_profit_task(Admin_home.this).execute(String.valueOf(user_infoList.get(0).profit_percentage));
                            } else if (index == 0) {
                                 add_customer();
                            }else if(index==4){
                                delete_customer();
                            }
                        }
                    })
                    .normalImageRes(images[i])
                    .normalText(texts[i])
                    .shadowEffect(true)
                    .containsSubText(false);

            bmb.addBuilder(builder);
        }
        navigationView.setNavigationItemSelectedListener(this);
        View header_view = LayoutInflater.from(this).inflate(R.layout.nav_header_admin_home, null);
        name = header_view.findViewById(R.id.name_txt);
        email = header_view.findViewById(R.id.email_txt);
        roundicon = header_view.findViewById(R.id.imageView);
        name.setText(user_infoList.get(0).Name);
        email.setText(user_infoList.get(0).email);
        datePickerDialog = new DatePickerDialog(
                Admin_home.this, Admin_home.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        TextDrawable drawable = TextDrawable.builder().beginConfig().width(150).height(150).endConfig().buildRound(user_infoList.get(0).Name.substring(0, 1).toUpperCase(), iconcolor);
        roundicon.setImageDrawable(drawable);
        navigationView.addHeaderView(header_view);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!prefs.getBoolean("keep_info",false)){
            prefs.edit().remove("user_info").apply();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
           if(id==R.id.signout){
               prefs.edit().remove("user_info").apply();
               prefs.edit().remove("keep_info").apply();
               finish();
           }else if(id==R.id.withdraw_request){
               startActivity(new Intent(Admin_home.this,Notifications.class).putExtra("role","Admin"));
           }else if(id==R.id.accounts_details){
               startActivity(new Intent(Admin_home.this,Account_detail.class).putExtra("role","Admin"));
           }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
           this.day=dayOfMonth;
           this.month=month;
           this.year=year;
        select_date.setText(String.valueOf(day)+"."+String.valueOf(month+1)+"."+String.valueOf(year));
    }
 private void add_ib(){
     View add_user_view=LayoutInflater.from(Admin_home.this).inflate(R.layout.add_user_layout,null);
     final TextInputEditText name=add_user_view.findViewById(R.id.name_txt);
     final TextInputEditText email=add_user_view.findViewById(R.id.email_txt);
     final TextInputEditText password=add_user_view.findViewById(R.id.password_txt);
     final TextInputEditText investment=add_user_view.findViewById(R.id.investment_txt);
     final TextInputEditText father_name=add_user_view.findViewById(R.id.father_name_txt);
     final TextInputEditText cnic=add_user_view.findViewById(R.id.cnic_txt);
     final TextInputEditText percentage_profit=add_user_view.findViewById(R.id.profit_txt);
     final TextInputEditText investment_period=add_user_view.findViewById(R.id.investment_period_txt);
     select_date=add_user_view.findViewById(R.id.select_date);
       investment.setVisibility(View.GONE);
       percentage_profit.setVisibility(View.GONE);
       investment_period.setVisibility(View.GONE);
     select_date.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             datePickerDialog.show();
         }
     });
     String[] ITEMS = {"Customer","IB"};
     ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin_home.this, android.R.layout.simple_spinner_item, ITEMS);
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     final MaterialSpinner spinner = add_user_view.findViewById(R.id.spinner);
     spinner.setAdapter(adapter);
     spinner.setVisibility(View.GONE);
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
             }else if(password.getText().toString().isEmpty()) {
                 password.setError("Password is Required");
             }else if(password.getText().toString().length()<6){
                 password.setError("Password too short");
             }else if(father_name.getText().toString().isEmpty()){
                 father_name.setError("Father Name is Required");
             }else if(cnic.getText().toString().isEmpty()){
                 cnic.setError("CNIC is Required");
             }else if(cnic.getText().toString().length()<13){
                 cnic.setError("CNIC is too short");
             }else if(select_date.getText().toString().equals("Select Date")) {
                 Toast.makeText(Admin_home.this,"Please Select Date",Toast.LENGTH_LONG).show();
             }else{

                     new register_task(Admin_home.this).execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), String.valueOf(0), father_name.getText().toString(), cnic.getText().toString(), "IB", String.valueOf(0), "", select_date.getText().toString(), "");
             }
         }
     });
 }
 private void add_customer(){
     View add_user_view=LayoutInflater.from(Admin_home.this).inflate(R.layout.add_user_layout,null);
     final TextInputEditText name=add_user_view.findViewById(R.id.name_txt);
     final TextInputEditText email=add_user_view.findViewById(R.id.email_txt);
     final TextInputEditText password=add_user_view.findViewById(R.id.password_txt);
     final TextInputEditText investment=add_user_view.findViewById(R.id.investment_txt);
     final TextInputEditText father_name=add_user_view.findViewById(R.id.father_name_txt);
     final TextInputEditText cnic=add_user_view.findViewById(R.id.cnic_txt);
     final TextInputEditText percentage_profit=add_user_view.findViewById(R.id.profit_txt);
     final TextInputEditText investment_period=add_user_view.findViewById(R.id.investment_period_txt);
     select_date=add_user_view.findViewById(R.id.select_date);

     select_date.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             datePickerDialog.show();
         }
     });
     String[] ITEMS = {"Customer","IB"};
     ArrayAdapter<String> adapter = new ArrayAdapter<String>(Admin_home.this, android.R.layout.simple_spinner_item, ITEMS);
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     final MaterialSpinner spinner = add_user_view.findViewById(R.id.spinner);
     spinner.setAdapter(adapter);
     spinner.setVisibility(View.GONE);
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
             }else if(select_date.getText().toString().equals("Select Date")) {
                 Toast.makeText(Admin_home.this,"Please Select Date",Toast.LENGTH_LONG).show();
             }else if(investment_period.getText().toString().isEmpty()){
                 investment_period.setError("Investment Period is Required");
             }else{
                 new register_task(Admin_home.this).execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), investment.getText().toString(), father_name.getText().toString(), cnic.getText().toString(), "Customer", percentage_profit.getText().toString(), "", select_date.getText().toString(), investment_period.getText().toString());
             }
         }
     });
 }
 private void delete_customer(){
     View send_profit_view=LayoutInflater.from(Admin_home.this).inflate(R.layout.give_profit,null);
     final TextInputEditText profit=send_profit_view.findViewById(R.id.profit_txt);
     TextInputLayout txt=send_profit_view.findViewById(R.id.profit_textinputlayout);
     txt.setHint("Account no or Email");
      profit.setInputType(InputType.TYPE_CLASS_TEXT);
     AlertDialog send_profit_dialog=new AlertDialog.Builder(Admin_home.this)
             .setTitle("Delete Customer")
             .setMessage("Enter Email or Account no to delete")
             .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
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
                 profit.setError("Enter Account no or Email");
             }else {
                new delete_customer_task(Admin_home.this).execute(profit.getText().toString(),profit.getText().toString());
             }
         }
     });
 }
}
