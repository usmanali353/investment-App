package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class send_profit_shared_notification extends AsyncTask<String,Void,String> {
StringBuilder sb=new StringBuilder();

    public send_profit_shared_notification(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait....");
    }

    Context context;
ProgressDialog pd;
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(pd.isShowing())
            pd.dismiss();
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String text=strings[0];
        String date=strings[1];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/sendprofit_noti.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info= URLEncoder.encode("text","UTF-8")+"="+URLEncoder.encode(text,"UTF-8")+"&"+URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");
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
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
