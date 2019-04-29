package usmanali.investmentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.levitnudi.legacytableview.LegacyTableView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class get_all_accounts extends AsyncTask{
    ProgressDialog pd;
    Context context;
    LegacyTableView notifications_List;
    String json;
    List<user_info> userInfoList;
    StringBuilder sb=new StringBuilder();
    String[] titles={"Name","CNIC","Email","Investment","Earning","Profit Percentage","Referor Email","Account Opening Date","Investment Period","User Type"};
    public get_all_accounts(Context context,LegacyTableView tableView) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        this.notifications_List=tableView;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/get_accounts_detail.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((json=reader.readLine())!=null){
                sb.append(json);
            }
            userInfoList=new Gson().fromJson(sb.toString(),new TypeToken<List<user_info>>(){}.getType());
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

        if(userInfoList!=null&&userInfoList.size()>0){
            LegacyTableView.insertLegacyTitle(titles);
            notifications_List.setTitle(LegacyTableView.readLegacyTitle());
            for (int i=0;i<userInfoList.size();i++){
                LegacyTableView.insertLegacyContent(userInfoList.get(i).Name,userInfoList.get(i).CNIC,userInfoList.get(i).email,String.valueOf(userInfoList.get(i).investment),String.valueOf(userInfoList.get(i).earning),String.valueOf(userInfoList.get(i).profit_percentage),userInfoList.get(i).referer_email,userInfoList.get(i).opening_date,userInfoList.get(i).investment_period,userInfoList.get(i).user_type);
                }

                notifications_List.setZoomEnabled(true);
                notifications_List.setShowZoomControls(true);
            notifications_List.setContent(LegacyTableView.readLegacyContent());
            notifications_List.build();
        }else{
            Toast.makeText(context,"No Accounts Found",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
