package crypto.amitvishalanujnexus.com.crypto;

import android.content.Context;
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


public class CreatePin extends ActionBarActivity {

    private SharedPreferences spKey;
    private EditText et_pin1;
    private EditText et_pin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        spKey = this.getSharedPreferences("com.amitvishalanujnexus.crypto.safepin", Context.MODE_PRIVATE);
        et_pin1 = (EditText)findViewById(R.id.et_pin1);
        et_pin2 = (EditText)findViewById(R.id.et_pin2);
    }

    public void b_savepin(View v){
        String pin1 = et_pin1.getText().toString();
        String pin2 = et_pin2.getText().toString();
        if(pin1.contentEquals(pin2)){
            String md5pin = md5hash(pin1);
            SharedPreferences.Editor editor = spKey.edit();
            editor.putString("pin",md5pin);
            editor.commit();
            Toast.makeText(this,"Pin saved successfully",Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            Toast.makeText(this,"Pin do not match",Toast.LENGTH_SHORT).show();
            et_pin1.setText("");
            et_pin2.setText("");
            et_pin1.setFocusable(true);
        }
    }

    private String md5hash(String pin){
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(pin.getBytes(),0,pin.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_pin, menu);
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
