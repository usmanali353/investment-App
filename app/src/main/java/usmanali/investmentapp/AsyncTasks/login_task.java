package usmanali.investmentapp.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import usmanali.investmentapp.Admin_home;
import usmanali.investmentapp.user_home;
import usmanali.investmentapp.user_info;

public class login_task extends AsyncTask<String,Void,Void> {
    ProgressDialog pd;
    String json;
    boolean isChecked;
    public login_task( Context context,boolean isChecked) {
        this.context = context;
        prefs=PreferenceManager.getDefaultSharedPreferences(context);
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait....");
        this.isChecked=isChecked;
    }


    List<usmanali.investmentapp.user_info> user_info;
Context context;
SharedPreferences prefs;
    StringBuilder sb=new StringBuilder();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String email=strings[0];
        String password=strings[1];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/get_user_info.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((json=reader.readLine())!=null){
                sb.append(json);
            }
         user_info=new Gson().fromJson(sb.toString(),new TypeToken<List<user_info>>(){}.getType());
            //reader.close();
            Log.e("json",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(pd.isShowing())
            pd.dismiss();

        if(user_info!=null&&user_info.size()>0){
            Toast.makeText(context,"Login Sucess",Toast.LENGTH_LONG).show();
            prefs.edit().putString("user_info",new Gson().toJson(user_info)).apply();
            if (isChecked) {
                prefs.edit().putBoolean("keep_info", true).apply();
            }else{
                prefs.edit().putBoolean("keep_info", false).apply();
            }
            if(user_info.get(0).getUser_type().equals("Admin")){
                context.startActivity(new Intent(context,Admin_home.class));

                ((AppCompatActivity)context).finish();
            }else {
                context.startActivity(new Intent(context, user_home.class));
                ((AppCompatActivity)context).finish();
            }
        }else {
            Toast.makeText(context,"Provide Account no and password",Toast.LENGTH_LONG).show();
        }
    }
}
