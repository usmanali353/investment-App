package usmanali.investmentapp;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import usmanali.investmentapp.AsyncTasks.approve_notifications_task;
import usmanali.investmentapp.AsyncTasks.send_withdrawal_request_task;
import usmanali.investmentapp.AsyncTasks.withdraw_earning;

public class all_notifications_adapter extends BaseAdapter {
    List<withdraw_notifications> notificationsList;
    Context context;
    public all_notifications_adapter(List<withdraw_notifications> notificationsList,Context context) {
        this.notificationsList = notificationsList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return notificationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        }
            TextView notification_text = convertView.findViewById(R.id.notification_txt);
            TextView notification_date = convertView.findViewById(R.id.notification_date);
            notification_text.setText(notificationsList.get(position).notification_text);
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(notificationsList.get(position).notification_date),
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            notification_date.setText(timeAgo);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(context);
                    confirm_dialog.setTitle("Approve Withdraw Request");
                    confirm_dialog.setMessage("Do you Want to Approve this withdraw request?");
                    confirm_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new approve_notifications_task(context).execute(String.valueOf(notificationsList.get(position).id));
                            new send_withdrawal_request_task(context).execute(String.valueOf(System.currentTimeMillis()),"Your withdraw Request for Rs "+notificationsList.get(position).withdraw_amount+" is Approved by admin",notificationsList.get(position).customer_email, String.valueOf(notificationsList.get(position).withdraw_amount),"Yes");
                            new withdraw_earning(context).execute(notificationsList.get(position).customer_email, String.valueOf(notificationsList.get(position).withdraw_amount));

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

        return convertView;
    }
}
