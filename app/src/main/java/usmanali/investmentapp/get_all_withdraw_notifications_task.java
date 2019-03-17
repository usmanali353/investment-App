package usmanali.investmentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

public class get_all_withdraw_notifications_task extends AsyncTask {
    ProgressDialog pd;
    Context context;
    ListView notifications_List;
    String json;
    List<withdraw_notifications> notificationsList;
    StringBuilder sb=new StringBuilder();
    public get_all_withdraw_notifications_task(Context context,ListView notificationsList) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        this.notifications_List=notificationsList;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/get_all_withdraw_notifications.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
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
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(pd.isShowing())
            pd.dismiss();
        if(notificationsList!=null&&notificationsList.size()>0){
             notifications_List.setAdapter(new all_notifications_adapter(notificationsList,context));
        }else{
            Toast.makeText(context,"No Notifications Yet",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}