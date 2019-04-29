package usmanali.investmentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import usmanali.investmentapp.AsyncTasks.login_task;

public class Login extends AppCompatActivity {
TextInputEditText email,password;
Button login_btn;
SharedPreferences prefs;
CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        email=findViewById(R.id.email_txt);
        password=findViewById(R.id.password_txt);
        login_btn=findViewById(R.id.btn);
        cb=findViewById(R.id.cb);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        List<user_info> user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());

        if(user_infoList!=null&&user_infoList.size()>0){
            if(user_infoList.get(0).user_type.equals("Admin")){
                startActivity(new Intent(Login.this,Admin_home.class));
                finish();
            }else{
                startActivity(new Intent(Login.this,user_home.class));
                finish();
            }
        }
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("CNIC is Required");
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Password is required");
                }else if(password.getText().toString().length()<6){
                    password.setError("Password is too short");
                }else{
                   new login_task(Login.this,cb.isChecked()).execute(email.getText().toString(),password.getText().toString());
                }
            }
        });
    }
}
