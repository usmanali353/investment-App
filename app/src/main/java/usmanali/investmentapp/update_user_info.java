package usmanali.investmentapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import usmanali.investmentapp.AsyncTasks.register_task;
import usmanali.investmentapp.AsyncTasks.update_customer_info;

public class update_user_info extends AppCompatActivity {
    TextInputEditText name, password, profit_percentage, investment_period, date, investment, father_name, ac_no, email,referor_ac_no,user_type;
    user_info userInfo;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userInfo = new Gson().fromJson(getIntent().getStringExtra("user_data"), user_info.class);
        name = findViewById(R.id.name_txt);
        password = findViewById(R.id.password_txt);
        investment_period = findViewById(R.id.investment_period_txt);
        date = findViewById(R.id.date_txt);
        investment = findViewById(R.id.investment_txt);
        father_name = findViewById(R.id.father_name_txt);
        ac_no = findViewById(R.id.cnic_txt);
        email = findViewById(R.id.email_txt);
        referor_ac_no=findViewById(R.id.referor_ac_no_txt);
        profit_percentage = findViewById(R.id.profit_txt);
        user_type=findViewById(R.id.user_type_txt);
        update=findViewById(R.id.update);
        //Setting Values
        name.setText(userInfo.getName());
        password.setText(userInfo.getPassword());
        email.setText(userInfo.getEmail());
        ac_no.setText(userInfo.getCNIC());
        father_name.setText(userInfo.getFather_name());
        user_type.setText(userInfo.getUser_type());
        ac_no.setEnabled(false);
        if (userInfo.getInvestment_period() != null&&!userInfo.getInvestment_period().isEmpty()) {
            investment_period.setText(userInfo.getInvestment_period());
        }else{
            investment_period.setVisibility(View.GONE);
        }
        if (userInfo.getInvestment() > 0) {
            investment.setText(String.valueOf(userInfo.getInvestment()));
        }else{
            investment.setVisibility(View.GONE);
        }
        if (userInfo.getProfit_percentage() > 0) {
            profit_percentage.setText(String.valueOf(userInfo.getProfit_percentage()));
        }else{
            profit_percentage.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(userInfo.getReferer_email())){
            referor_ac_no.setText(userInfo.getReferer_email());
        }else{
            referor_ac_no.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(userInfo.getOpening_date())){
            date.setVisibility(View.GONE);
        }else{
            date.setText(userInfo.getOpening_date());
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_type.getText().toString().equals("Admin")) {
                    new update_customer_info(update_user_info.this).execute(name.getText().toString(), email.getText().toString(), password.getText().toString(), investment.getText().toString(), father_name.getText().toString(), ac_no.getText().toString(), user_type.getText().toString(), "", "", "", "", "");
                }else if(!TextUtils.isEmpty(referor_ac_no.getText().toString())){
                    new update_customer_info(update_user_info.this).execute(name.getText().toString(),email.getText().toString(),password.getText().toString(),investment.getText().toString(),father_name.getText().toString(),ac_no.getText().toString(),"Customer",profit_percentage.getText().toString(),referor_ac_no.getText().toString(),"",date.getText().toString(),investment_period.getText().toString());
                }else if(TextUtils.isEmpty(referor_ac_no.getText().toString())&&user_type.getText().toString().equals("Customer")){
                    new update_customer_info(update_user_info.this).execute(name.getText().toString(),email.getText().toString(),password.getText().toString(),investment.getText().toString(),father_name.getText().toString(),ac_no.getText().toString(),"Customer",profit_percentage.getText().toString(),"","",date.getText().toString(),investment_period.getText().toString());
                }
                name.setText("");
                ac_no.setText("");
                father_name.setText("");
                password.setText("");
                email.setText("");
                profit_percentage.setText("");
                investment.setText("");
                investment_period.setText("");
                date.setText("");
                referor_ac_no.setText("");
                user_type.setText("");

            }

        });

    }
}
