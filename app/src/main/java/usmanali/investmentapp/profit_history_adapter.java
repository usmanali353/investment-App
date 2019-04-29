package usmanali.investmentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class profit_history_adapter extends BaseAdapter {
    ArrayList<Profit> profitArrayList;

    public profit_history_adapter(ArrayList<Profit> profitArrayList) {
        this.profitArrayList = profitArrayList;
    }

    @Override
    public int getCount() {
        return profitArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return profitArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        profit_notification_viewholder vh;
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
            vh=new profit_notification_viewholder();
            vh.text=convertView.findViewById(R.id.notification_txt);
            vh.date=convertView.findViewById(R.id.notification_date);
            convertView.setTag(vh);
        }
        vh= (profit_notification_viewholder) convertView.getTag();
         vh.text.setText(profitArrayList.get(position).text);
         vh.date.setText(profitArrayList.get(position).date);

        return convertView;
    }
    class profit_notification_viewholder{
        TextView text,date;
    }
}
