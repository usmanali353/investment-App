package usmanali.investmentapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class dashboard_adapter extends RecyclerView.Adapter<dashboard_adapter.dashboard_viewholder> {
    ArrayList<dashboard_model> model;
     SharedPreferences prefs;
    public dashboard_adapter(ArrayList<dashboard_model> model, Context context) {
        this.model = model;
        prefs=PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public dashboard_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_layout,viewGroup,false);
        return new dashboard_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dashboard_viewholder dashboard_viewholder, int i) {
       List<user_info> user_infoList=new Gson().fromJson(prefs.getString("user_info",""),new TypeToken<List<user_info>>(){}.getType());
        dashboard_viewholder.long_text.setText(model.get(i).long_text);
     if(i==0){
         dashboard_viewholder.short_text.setText("Rs "+user_infoList.get(0).investment);
     }else if(i==1){
         dashboard_viewholder.short_text.setText("Rs "+user_infoList.get(0).earning);
     }
      dashboard_viewholder.img.setImageResource(model.get(i).image);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    class dashboard_viewholder extends RecyclerView.ViewHolder{
            TextView long_text,short_text;
            ImageView img;
        public dashboard_viewholder(@NonNull View itemView) {
            super(itemView);
            long_text=itemView.findViewById(R.id.long_text);
            short_text=itemView.findViewById(R.id.short_text);
            img=itemView.findViewById(R.id.investment_image);
        }
    }
}
