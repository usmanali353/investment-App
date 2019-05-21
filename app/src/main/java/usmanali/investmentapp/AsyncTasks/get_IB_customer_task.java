package usmanali.investmentapp.AsyncTasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;
import usmanali.investmentapp.Admin_home;
import usmanali.investmentapp.AsyncTasks.add_refered_customer_task;
import usmanali.investmentapp.R;
import usmanali.investmentapp.user_info;

public class get_IB_customer_task extends AsyncTask {
    Context context;
    ProgressDialog pd;
     DatePickerDialog datePickerDialog;
     int day,mahena,saal;
     Button select_date;
    public get_IB_customer_task(Context context) {
        this.context = context;
        pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        datePickerDialog = new DatePickerDialog(
                context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                day=dayOfMonth;
                saal=year;
                mahena=month;
                select_date.setText(String.valueOf(day)+"."+String.valueOf(mahena+1)+"."+String.valueOf(saal));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }
    ArrayList<user_info> ib_customer_list;
    ArrayList<String> ib_customers_emails=new ArrayList<>();
    String json;
    StringBuilder sb=new StringBuilder();
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url=new URL("https://helloworldsolution12.000webhostapp.com/get_ib_customers.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((json=reader.readLine())!=null){
                sb.append(json);
            }
            ib_customer_list=new Gson().fromJson(sb.toString(),new TypeToken<ArrayList<user_info>>(){}.getType());
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
        if(ib_customer_list!=null&&ib_customer_list.size()>0){
            final View add_user_view=LayoutInflater.from(context).inflate(R.layout.add_refered_customer,null);
            final TextInputEditText name=add_user_view.findViewById(R.id.name_txt);
            final TextInputEditText email=add_user_view.findViewById(R.id.email_txt);
            final TextInputEditText password=add_user_view.findViewById(R.id.password_txt);
            final TextInputEditText investment=add_user_view.findViewById(R.id.investment_txt);
            final TextInputEditText father_name=add_user_view.findViewById(R.id.father_name_txt);
            final TextInputEditText cnic=add_user_view.findViewById(R.id.cnic_txt);
            final TextInputEditText percentage_profit=add_user_view.findViewById(R.id.profit_txt);
            final TextInputEditText ib_percentage_profit=add_user_view.findViewById(R.id.ib_profit_txt);
             select_date=add_user_view.findViewById(R.id.select_date);
             final TextInputEditText investment_period=add_user_view.findViewById(R.id.investment_period_txt);
            select_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.show();
                }
            });
            for(int i=0;i<ib_customer_list.size();i++){
                ib_customers_emails.add(ib_customer_list.get(i).getCNIC());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ib_customers_emails);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            final MaterialSpinner spinner = add_user_view.findViewById(R.id.select_ib);
            spinner.setAdapter(adapter);
            AlertDialog add_user_dialog=new AlertDialog.Builder(context)
                    .setTitle("Add Refered User")
                    .setCancelable(false)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setView(add_user_view)
                    .create();
            add_user_dialog.show();
            add_user_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString().isEmpty()){
                        name.setError("Name is Required");
                    }else if(email.getText().toString().isEmpty()){
                        email.setError("Email is Required");
                    }else if(password.getText().toString().isEmpty()){
                        password.setError("Password is Required");
                    }else if(investment.getText().toString().isEmpty()){
                        investment.setError("Investment is Required");
                    }else if(password.getText().toString().length()<6){
                        password.setError("Password too short");
                    }else if(Integer.valueOf(investment.getText().toString())<100){
                        investment.setError("Minimium investment that can be made is Rs 100");
                    }else if(father_name.getText().toString().isEmpty()){
                        father_name.setError("Father Name is Required");
                    }else if(cnic.getText().toString().isEmpty()){
                        cnic.setError("CNIC is Required");
                    }else if(cnic.getText().toString().length()<5){
                        cnic.setError("Account no is too short");
                    }else if(percentage_profit.getText().toString().isEmpty()){
                        percentage_profit.setError("Percentage Profit is Required");
                    }else if(spinner.getSelectedItem().toString().equals("Select IB")) {
                        spinner.setError("Select IB");
                    }else if(ib_percentage_profit.getText().toString().isEmpty()){
                        ib_percentage_profit.setError("Percentage Profit for IB is required");
                    }else if(select_date.getText().toString().equals("Select Date")) {
                        Toast.makeText(context,"Please Select Date",Toast.LENGTH_LONG).show();
                    }else if(investment_period.getText().toString().isEmpty()){
                        investment_period.setError("Investment Period is Required");
                    }else{
                        new add_refered_customer_task(context).execute(name.getText().toString(),email.getText().toString(),password.getText().toString(),investment.getText().toString(),father_name.getText().toString(),cnic.getText().toString(),"Customer",percentage_profit.getText().toString(),spinner.getSelectedItem().toString(),ib_percentage_profit.getText().toString(),select_date.getText().toString(),investment_period.getText().toString());
                        name.setText("");
                        cnic.setText("");
                        father_name.setText("");
                        password.setText("");
                        email.setText("");
                        percentage_profit.setText("");
                        investment.setText("");
                        investment_period.setText("");

                        select_date.setText("Select Date");
                    }
                }
            });

        }else{
            Toast.makeText(context,"No Referor IB Found to Refer Customer",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }
}
