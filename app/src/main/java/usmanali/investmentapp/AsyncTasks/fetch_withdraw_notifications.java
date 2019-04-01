package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
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

import usmanali.investmentapp.notifications_list_adapter;
import usmanali.investmentapp.user_info;
import usmanali.investmentapp.withdraw_notifications;

public class fetch_withdraw_notifications extends AsyncTask<String,Void,Void> {
    ProgressDialog progressDialog;
    Context context;
   String json;
   ListView notification_list;
   List<withdraw_notifications> notificationsList;
   StringBuilder sb=new StringBuilder();
   SwipeRefreshLayout srl;
   SharedPreferences prefs;
    public fetch_withdraw_notifications(Context context,ListView notification_list,SwipeRefreshLayout srl) {
        this.context = context;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        this.notification_list=notification_list;
        this.srl=srl;
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String email=strings[0];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/get_withdraw_notifications.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("customer_email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((json=reader.readLine())!=null){
                sb.append(json);
            }
            notificationsList=new Gson().fromJson(sb.toString(),new TypeToken<List<withdraw_notifications>>(){}.getType());
            //reader.close();
            Log.e("json",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        srl.setRefreshing(true);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        srl.setRefreshing(false);
       final List<user_info> user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new fetch_withdraw_notifications(context,notification_list,srl).execute(user_infoList.get(0).getEmail());
            }
        });
        if(notificationsList!=null&&notificationsList.size()>0){
          notification_list.setAdapter(new notifications_list_adapter(notificationsList));
        }else{
            Toast.makeText(context,"No Notifications Yet",Toast.LENGTH_LONG).show();
        }
    }
}
