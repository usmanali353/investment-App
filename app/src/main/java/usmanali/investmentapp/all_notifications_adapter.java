package usmanali.investmentapp;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import usmanali.investmentapp.AsyncTasks.approve_notifications_task;
import usmanali.investmentapp.AsyncTasks.get_all_withdraw_notifications_task;
import usmanali.investmentapp.AsyncTasks.send_withdrawal_request_task;
import usmanali.investmentapp.AsyncTasks.withdraw_earning;

public class all_notifications_adapter extends BaseAdapter {
    List<withdraw_notifications> notificationsList;
    Context context;
    ListView nl;
    SwipeRefreshLayout srl;
    SharedPreferences prefs;
    public all_notifications_adapter(List<withdraw_notifications> notificationsList, Context context, ListView notification_list, SwipeRefreshLayout srl) {
        this.notificationsList = notificationsList;
        this.context=context;
        this.nl=notification_list;
        this.srl=srl;
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
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
        notification_viewholder vh;
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
            vh=new notification_viewholder();
            vh.notification_text=convertView.findViewById(R.id.notification_txt);
            vh.notification_date=convertView.findViewById(R.id.notification_date);
            convertView.setTag(vh);
        }

          vh= (notification_viewholder) convertView.getTag();
            vh.notification_text.setText(notificationsList.get(position).notification_text);
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(notificationsList.get(position).notification_date),
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            vh.notification_date.setText(timeAgo);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        return convertView;
    }
    class notification_viewholder{
        TextView notification_text;
        TextView notification_date;
    }
}
