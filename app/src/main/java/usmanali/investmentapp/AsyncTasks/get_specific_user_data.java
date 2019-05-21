package usmanali.investmentapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

import usmanali.investmentapp.update_user_info;
import usmanali.investmentapp.user_info;

public class get_specific_user_data extends AsyncTask<String,Void,String> {
StringBuilder sb;
 ProgressDialog pd;
 Context context;

    public get_specific_user_data(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        sb=new StringBuilder();
    }

   ArrayList<user_info>  userInfo;

    @Override
    protected String doInBackground(String... strings) {
        String CNIC=strings[0];
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/checkuser.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info= URLEncoder.encode("cnic","UTF-8")+"="+URLEncoder.encode(CNIC,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            userInfo=new Gson().fromJson(sb.toString(),new TypeToken<ArrayList<user_info>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(pd.isShowing()){
            pd.dismiss();
        }
        if(userInfo!=null&&userInfo.size()>0){
            Intent i=new Intent(context, update_user_info.class);
            i.putExtra("user_data",new Gson().toJson(userInfo.get(0)));
            context.startActivity(i);
        }else{
            Toast.makeText(context,"No User Associated with this Account no",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!pd.isShowing()) {
            pd.show();
        }
    }
}
