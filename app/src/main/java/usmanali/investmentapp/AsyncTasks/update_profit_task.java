package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class update_profit_task extends AsyncTask<String,Void,String> {
    Context context;
     SharedPreferences prefs;
    public update_profit_task(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    ProgressDialog pd;
    StringBuilder sb=new StringBuilder();

    @Override
    protected String doInBackground(String... strings) {
        String profit=strings[0];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/update_profit.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("profit","UTF-8")+"="+URLEncoder.encode(profit,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(pd.isShowing())
            pd.dismiss();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        new send_profit_shared_notification(context).execute("Admin has shared the profit",formattedDate);
        prefs.edit().putString("shared_profit",formattedDate).apply();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
