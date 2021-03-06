package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ExpandableListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import usmanali.investmentapp.expandable_list_adapter;
import usmanali.investmentapp.withdraw_notifications;

public class get_all_withdraw_notifications_task extends AsyncTask {
    ProgressDialog pd;
    Context context;
    ExpandableListView notifications_List;
    String json;
    ArrayList<withdraw_notifications> notificationsList,approved_notifications,unapproved_notifications;
   ArrayList<String> header_list;
    HashMap<String,ArrayList<withdraw_notifications>> childs;
    StringBuilder sb=new StringBuilder();

    //SwipeRefreshLayout srl;
    public get_all_withdraw_notifications_task(Context context, ExpandableListView notificationsList) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        this.notifications_List=notificationsList;
        approved_notifications=new ArrayList<>();
        unapproved_notifications=new ArrayList<>();
        header_list=new ArrayList<>();
        childs=new HashMap<>();
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
            notificationsList=new Gson().fromJson(sb.toString(),new TypeToken<ArrayList<withdraw_notifications>>(){}.getType());
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
        pd.dismiss();
        if(notificationsList!=null&&notificationsList.size()>0){
           for(int i=0;i<notificationsList.size();i++){
               if(notificationsList.get(i).getApproved().equals("No")){
                   unapproved_notifications.add(notificationsList.get(i));
               }else{
                   approved_notifications.add(notificationsList.get(i));
               }
           }
           Log.e("approved_notifications", String.valueOf(approved_notifications.size()));
           Log.e("unapproved_notification", String.valueOf(unapproved_notifications.size()));
           header_list.add("Approved Notifications");
           header_list.add("Un Approved Notifications");
           childs.put(header_list.get(0),approved_notifications);
           childs.put(header_list.get(1),unapproved_notifications);
           notifications_List.setAdapter(new expandable_list_adapter(header_list,childs,notifications_List,context));
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
