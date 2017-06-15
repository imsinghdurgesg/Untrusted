package singh.durgesh.com.applocker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import singh.durgesh.com.applocker.R;
import singh.durgesh.com.applocker.Services.SecureMyAppsService;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password, cnf_password;
    Button submit_btn, start;

    /**
     * this activity is just for testing purpose,we will remove this once our code will be stable.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = (EditText) findViewById(R.id.passord);
        cnf_password = (EditText) findViewById(R.id.cnf_password);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        start = (Button) findViewById(R.id.start);
        submit_btn.setOnClickListener(this);
        start.setOnClickListener(this);
        startService(new Intent(this, SecureMyAppsService.class));
        //
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                Intent mIntent = new Intent(this, AppListActivity.class);
                startActivity(mIntent);
                break;
            case R.id.start:
                //startService();

        }

    }

}
