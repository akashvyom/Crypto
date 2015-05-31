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


public class ChangePinActivity extends ActionBarActivity {

    private SharedPreferences spKey;
    EditText et_oldpin;
    EditText et_newpin1;
    EditText et_newpin2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        spKey = this.getSharedPreferences("com.amitvishalanujnexus.crypto.safepin", Context.MODE_PRIVATE);
        et_oldpin = (EditText)findViewById(R.id.et_oldpin);
        et_newpin1 = (EditText)findViewById(R.id.et_newpin1);
        et_newpin2 = (EditText)findViewById(R.id.et_newpin2);
    }

    public void b_savenewpin(View view){
        String userpin = et_oldpin.getText().toString();
        String newpin1 = et_newpin1.getText().toString();
        String newpin2 = et_newpin2.getText().toString();
        boolean do_old_match = checkoldpinmatch(userpin);
        if(do_old_match==true){
            boolean do_new_match = checknewpinmatch(newpin1,newpin2);
            if (do_new_match==true){
                SharedPreferences.Editor editor = spKey.edit();
                editor.putString("pin",md5hash(newpin1));
                editor.commit();
                Toast.makeText(this,"Pin changes successfully",Toast.LENGTH_SHORT).show();
                this.finish();
            }else{
                Toast.makeText(this,"Confirmation pin do not match",Toast.LENGTH_SHORT).show();
                et_newpin1.setText("");
                et_newpin2.setText("");
                et_newpin1.setFocusable(true);
            }
        }else{
            Toast.makeText(this,"You  have entered wrong pin",Toast.LENGTH_SHORT).show();
            et_oldpin.setText("");
            et_oldpin.setFocusable(true);
        }
    }

    private boolean checknewpinmatch(String p1,String p2){
        if(p1.contentEquals(p2)){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkoldpinmatch(String userpin){
        String pin = spKey.getString("pin","");
        String md5pin = md5hash(userpin);
        if(md5pin.contentEquals(pin)){
            return true;
        }else{
            return false;
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
        getMenuInflater().inflate(R.menu.menu_change_pin, menu);
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
