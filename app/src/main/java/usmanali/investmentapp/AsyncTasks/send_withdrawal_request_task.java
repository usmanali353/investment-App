package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class send_withdrawal_request_task extends AsyncTask<String,Void,String> {
    ProgressDialog pd;
    StringBuilder sb=new StringBuilder();
    public send_withdrawal_request_task(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
    }

    Context context;


    @Override
    protected String doInBackground(String... strings) {
       String notification_date=strings[0];
       String notification_text=strings[1];
       String customer_email=strings[2];
       String withdraw_amount=strings[3];
       String approved=strings[4];
        try {
            URL url =new URL("https://helloworldsolution12.000webhostapp.com/create_notification.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("notification_text","UTF-8")+"="+URLEncoder.encode(notification_text,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(customer_email,"UTF-8")+"&"+URLEncoder.encode("withdraw_amount","UTF-8")+"="+URLEncoder.encode(withdraw_amount,"UTF-8")+"&"+URLEncoder.encode("notification_date","UTF-8")+"="+URLEncoder.encode(notification_date,"UTF-8")+"&"+URLEncoder.encode("approved","UTF-8")+"="+URLEncoder.encode(approved,"UTF-8");
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
        if (pd.isShowing())
            pd.dismiss();
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        Log.e("response",s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
