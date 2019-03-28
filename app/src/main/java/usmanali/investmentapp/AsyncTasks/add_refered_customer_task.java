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

public class add_refered_customer_task extends AsyncTask<String,Void,String> {
    ProgressDialog pd;
    Context context;

    public add_refered_customer_task(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
    }

    StringBuilder sb=new StringBuilder();

    @Override
    protected String doInBackground(String... strings) {
        String name=strings[0];
        String email=strings[1];
        String password=strings[2];
        String investment=strings[3];
        String father_name=strings[4];
        String cnic=strings[5];
        String user_type=strings[6];
        String percentage_profit=strings[7];
        String referer_email=strings[8];
        String ib_percentage_profit=strings[9];
        String opening_date=strings[10];
        try {
            URL url =new URL("https://helloworldsolution12.000webhostapp.com/add_refered_customer.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String info=URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("investment","UTF-8")+"="+URLEncoder.encode(investment,"UTF-8")+"&"+URLEncoder.encode("father_name","UTF-8")+"="+URLEncoder.encode(father_name,"UTF-8")+"&"+URLEncoder.encode("cnic","UTF-8")+"="+URLEncoder.encode(cnic,"UTF-8")+"&"+URLEncoder.encode("user_type","UTF-8")+"="+URLEncoder.encode(user_type,"UTF-8")+"&"+URLEncoder.encode("percentage_profit","UTF-8")+"="+URLEncoder.encode(percentage_profit,"UTF-8")+"&"+URLEncoder.encode("referer_email","UTF-8")+"="+URLEncoder.encode(referer_email,"UTF-8")+"&"+URLEncoder.encode("ib_percentage_profit","UTF-8")+"="+URLEncoder.encode(ib_percentage_profit,"UTF-8")+"&"+URLEncoder.encode("opening_date","UTF-8")+"="+URLEncoder.encode(opening_date,"UTF-8");
            writer.write(info);
            writer.flush();
            writer.close();
            BufferedReader reader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            //reader.close();
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
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
