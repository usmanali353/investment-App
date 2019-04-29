package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import usmanali.investmentapp.Profit;
import usmanali.investmentapp.profit_history_adapter;
import usmanali.investmentapp.user_info;

public class profit_history_task extends AsyncTask {
    ProgressDialog pd;
    ArrayList<Profit> profit_notification_list;
    String json;
    StringBuilder sb=new StringBuilder();
    Context context;
    ListView profit_history_list;

    public profit_history_task(Context context, ListView profit_history_list) {
        this.context = context;
        this.profit_history_list = profit_history_list;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/profit_history.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((json=reader.readLine())!=null){
                sb.append(json);
            }
            profit_notification_list=new Gson().fromJson(sb.toString(),new TypeToken<List<Profit>>(){}.getType());
            //reader.close();
            //Log.e("json",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(pd.isShowing()){
            pd.dismiss();
        }
        if(profit_notification_list!=null&&profit_notification_list.size()>0){
            profit_history_list.setAdapter(new profit_history_adapter(profit_notification_list));
        }else{
            Toast.makeText(context,"No Notifications Found",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
