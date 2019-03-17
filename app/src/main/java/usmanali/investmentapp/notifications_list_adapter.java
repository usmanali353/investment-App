package usmanali.investmentapp;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class notifications_list_adapter extends BaseAdapter {
    List<withdraw_notifications> notificationsList;

    public notifications_list_adapter(List<withdraw_notifications> notificationsList) {
        this.notificationsList = notificationsList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        }
        TextView notification_text=convertView.findViewById(R.id.notification_txt);
        TextView notification_date=convertView.findViewById(R.id.notification_date);
        notification_text.setText(notificationsList.get(position).notification_text);
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                 Long.parseLong(notificationsList.get(position).notification_date),
                                 System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
        notification_date.setText(timeAgo);
        return convertView;
    }
}
