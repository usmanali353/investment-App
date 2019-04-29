package usmanali.investmentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import usmanali.investmentapp.AsyncTasks.approve_notifications_task;
import usmanali.investmentapp.AsyncTasks.get_all_withdraw_notifications_task;
import usmanali.investmentapp.AsyncTasks.send_withdrawal_request_task;
import usmanali.investmentapp.AsyncTasks.withdraw_earning;

public class expandable_list_adapter extends BaseExpandableListAdapter {
    ArrayList<String> header_list;
    HashMap<String,ArrayList<withdraw_notifications>> child_list;
    Context context;
    ExpandableListView elv;
    public expandable_list_adapter(ArrayList<String> header_list, HashMap<String, ArrayList<withdraw_notifications>> child_list,ExpandableListView elv,Context context) {
        this.header_list = header_list;
        this.child_list = child_list;
        this.elv=elv;
        this.context=context;
    }

    @Override
    public int getGroupCount() {
        return header_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child_list.get(header_list.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return header_list.get(groupPosition);
    }

    @Override
    public withdraw_notifications getChild(int groupPosition, int childPosition) {
        return child_list.get(header_list.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.group_layout,parent,false);

        }
          TextView group_name=convertView.findViewById(R.id.heading);
          group_name.setText(header_list.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        notification_expanded_list_viewholder vh;
        if(convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
            vh=new notification_expanded_list_viewholder();
            vh.text=convertView.findViewById(R.id.notification_txt);
            vh.date=convertView.findViewById(R.id.notification_date);
            convertView.setTag(vh);
        }
        vh= (notification_expanded_list_viewholder) convertView.getTag();
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(getChild(groupPosition,childPosition).notification_date),
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
         vh.text.setText(getChild(groupPosition,childPosition).notification_text);
         vh.date.setText(timeAgo);
         convertView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(getChild(groupPosition,childPosition).getApproved().equals("No")){
                     AlertDialog.Builder confirm_dialog = new AlertDialog.Builder(context);
                     confirm_dialog.setTitle("Approve Withdraw Request");
                     confirm_dialog.setMessage("Do you Want to Approve this withdraw request?");
                     confirm_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             new approve_notifications_task(context).execute(String.valueOf(getChild(groupPosition,childPosition).id));
                             new send_withdrawal_request_task(context).execute(String.valueOf(System.currentTimeMillis()),"Your withdraw Request for Rs "+getChild(groupPosition,childPosition).withdraw_amount+" is Approved by admin",getChild(groupPosition,childPosition).customer_email, String.valueOf(getChild(groupPosition,childPosition).withdraw_amount),"Yes");
                             new withdraw_earning(context).execute(getChild(groupPosition,childPosition).customer_email, String.valueOf(getChild(groupPosition,childPosition).withdraw_amount));
                             new get_all_withdraw_notifications_task(context,elv).execute();
                             notifyDataSetChanged();
                         }
                     }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     }).show();
                 }
             }
         });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class notification_expanded_list_viewholder{
        TextView text,date;
    }
}
