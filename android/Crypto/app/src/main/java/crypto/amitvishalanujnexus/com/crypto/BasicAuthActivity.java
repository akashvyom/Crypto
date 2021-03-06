package crypto.amitvishalanujnexus.com.crypto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BasicAuthActivity extends ActionBarActivity {
    private SharedPreferences spKey;
    private Context context = this;
    private EditText et_pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_auth);
        et_pin = (EditText)findViewById(R.id.et_authpin);
        spKey = this.getSharedPreferences("com.amitvishalanujnexus.crypto.safepin",Context.MODE_PRIVATE);
        String p = spKey.getString("pin","-1");
        if(p.contentEquals("-1")){
            Intent i = new Intent(this,CreatePin.class);
            startActivity(i);
        }
    }

    public void authenticatePin(View v){
        String pin = et_pin.getText().toString();
        authenticateUserPin(pin);
    }

    private void authenticateUserPin(String pin){
        String userpin = spKey.getString("pin","");
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(pin.getBytes(),0,pin.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        if(userpin.contentEquals(hash)){
            Intent i = new Intent(context,HomeActivity.class);
            startActivity(i);
            this.finish();
        }else{
            Toast.makeText(this,"You entered wrong pin",Toast.LENGTH_SHORT).show();
            et_pin.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
