package usmanali.investmentapp.AsyncTasks;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

public class withdraw_earning extends AsyncTask<String,Void,String> {
    String json;
    StringBuilder sb=new StringBuilder();
    Context context;
    ProgressDialog pd;
    public withdraw_earning(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... strings) {
        String email=strings[0];
        String profit=strings[1];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/withdraw_earning.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("profit","UTF-8")+"="+URLEncoder.encode(profit,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((json=reader.readLine())!=null){
                sb.append(json);
            }
            //user_info=new Gson().fromJson(sb.toString(),new TypeToken<List<user_info>>(){}.getType());
            //reader.close();
            Log.e("json",sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(pd.isShowing())
            pd.dismiss();
        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
