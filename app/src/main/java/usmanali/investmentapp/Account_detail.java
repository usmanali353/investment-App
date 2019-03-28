package usmanali.investmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.levitnudi.legacytableview.LegacyTableView;

public class Account_detail extends AppCompatActivity {
LegacyTableView table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        table=findViewById(R.id.legacy_table_view);
        new get_all_accounts(Account_detail.this,table).execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
                startActivity(new Intent(this,Admin_home.class));
                finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
