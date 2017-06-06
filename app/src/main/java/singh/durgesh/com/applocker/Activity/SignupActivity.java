package singh.durgesh.com.applocker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import singh.durgesh.com.applocker.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password,cnf_password;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password= (EditText) findViewById(R.id.passord);
        cnf_password= (EditText) findViewById(R.id.cnf_password);
        submit_btn= (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent=new Intent(this,AppListActivity.class);
        startActivity(mIntent);
    }
}
