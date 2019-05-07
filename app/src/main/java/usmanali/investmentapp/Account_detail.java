package usmanali.investmentapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.levitnudi.legacytableview.LegacyTableView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import usmanali.investmentapp.AsyncTasks.delete_customer_task;

public class Account_detail extends AppCompatActivity {
    LegacyTableView table;
    LocalBroadcastManager localBroadcastManager;
    ArrayList<user_info> userInfoList;
    String[] titles = {"Name", "CNIC", "Email", "Investment", "Earning", "Profit Percentage", "Referor IB", "Account Opening Date", "Investment Period", "User Type"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        table = findViewById(R.id.legacy_table_view);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        new get_all_accounts(Account_detail.this, table).execute();
        localBroadcastManager.registerReceiver(reciever, new IntentFilter("all_account_data"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.export_to_excel) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{
                if (userInfoList != null && userInfoList.size() > 0) {
                    File exportDir = new File(Environment.getExternalStorageDirectory(), "Investment_app_account_data/");
                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }

                    File file = new File(exportDir, "all_accounts_data.csv");
                    try {
                        file.createNewFile();
                        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                        csvWrite.writeNext(titles);
                        for (int i = 0; i < userInfoList.size(); i++) {
                            String[] data = {userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type};
                            csvWrite.writeNext(data);
                        }


                        csvWrite.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }else if(item.getItemId()==R.id.filter_by_customer){
                for (int i=0;i<userInfoList.size();i++){
                    if(userInfoList.get(i).getUser_type().equals("Customer")) {
                        LegacyTableView.insertLegacyContent(userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type);
                    }
                }

                table.setZoomEnabled(true);
                table.setShowZoomControls(true);
                table.setContent(LegacyTableView.readLegacyContent());
                table.rebuild();
            }else if(item.getItemId()==R.id.filter_by_ib){
                for (int i=0;i<userInfoList.size();i++){
                    if(userInfoList.get(i).getUser_type().equals("IB")) {
                        LegacyTableView.insertLegacyContent(userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type);
                    }
                }

                table.setZoomEnabled(true);
                table.setShowZoomControls(true);
                table.setContent(LegacyTableView.readLegacyContent());
                table.rebuild();
            }else if(item.getItemId()==R.id.show_all){
                for (int i=0;i<userInfoList.size();i++){
                        LegacyTableView.insertLegacyContent(userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type);
                }

                table.setZoomEnabled(true);
                table.setShowZoomControls(true);
                table.setContent(LegacyTableView.readLegacyContent());
                table.rebuild();
            }else if(item.getItemId()==R.id.search){
                View send_profit_view= LayoutInflater.from(Account_detail.this).inflate(R.layout.give_profit,null);
                final TextInputEditText profit=send_profit_view.findViewById(R.id.profit_txt);
                TextInputLayout txt=send_profit_view.findViewById(R.id.profit_textinputlayout);
                txt.setHint("Account no");
                profit.setInputType(InputType.TYPE_CLASS_TEXT);
                AlertDialog send_profit_dialog=new AlertDialog.Builder(Account_detail.this)
                        .setTitle("Search Customer")
                        .setMessage("Enter Account no to Search")
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
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
                            profit.setError("Enter Account no");
                        }else {
                            for (int i=0;i<userInfoList.size();i++){
                                if(userInfoList.get(i).getCNIC().equals(profit.getText().toString())) {
                                    LegacyTableView.insertLegacyContent(userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type);
                                }
                            }

                            table.setZoomEnabled(true);
                            table.setShowZoomControls(true);
                            table.setContent(LegacyTableView.readLegacyContent());
                            table.rebuild();
                        }
                    }
                });
            }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            userInfoList = new Gson().fromJson(intent.getStringExtra("all_account_data"), new TypeToken<ArrayList<user_info>>() {
            }.getType());
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (userInfoList != null && userInfoList.size() > 0) {
                        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
                        if (!exportDir.exists()) {
                            exportDir.mkdirs();
                        }

                        File file = new File(exportDir, "all_accounts_data.csv");
                        try {
                            file.createNewFile();
                            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                            csvWrite.writeNext(titles);
                            for (int i = 0; i < userInfoList.size(); i++) {
                                String[] data = {userInfoList.get(i).Name, userInfoList.get(i).CNIC, userInfoList.get(i).email, String.valueOf(userInfoList.get(i).investment), String.valueOf(userInfoList.get(i).earning), String.valueOf(userInfoList.get(i).profit_percentage), userInfoList.get(i).referer_email, userInfoList.get(i).opening_date, userInfoList.get(i).investment_period, userInfoList.get(i).user_type};
                                csvWrite.writeNext(data);
                            }
                            csvWrite.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                } else {
                    return;
                }

            }
        }
    }
}

