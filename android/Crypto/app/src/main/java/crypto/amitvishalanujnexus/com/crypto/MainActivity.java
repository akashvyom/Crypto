    package crypto.amitvishalanujnexus.com.crypto;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static android.widget.Toast.LENGTH_SHORT;
import static crypto.amitvishalanujnexus.com.crypto.AEShelper2.*;


    public class MainActivity extends ActionBarActivity {
    private EditText et_input;
    private EditText et_output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = (EditText)findViewById(R.id.et_input);
        et_output = (EditText)findViewById(R.id.et_output);

    }
        public void b_sendToWhatsapp(View v){
            String textToSend = et_output.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        public void clearText(View v){
        et_input.setText("");
    }
        private void passphraseDialog(final String what) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Passphrase");
            alert.setMessage("Enter your secret passphrase");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            input.setHint("Enter your passphrase here.");
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if (what.contentEquals("encrypt")) {
                        encryptText(value);
                    } else if (what.contentEquals("decrypt")) {
                        decryptText(value);
                    }
                    // Do something with value!
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
        }

        private void decryptText(String passphrase){
            String s = et_input.getText().toString();
            //String deText = simpleDecrypt(s);
            SecretKeySpec sks = AESHelper0.keygenerate(passphrase);
            String deText = AESHelper0.v_decrypt(s, sks);
            et_output.setText(deText);
        }

        public void b_decrypt(View v){
            passphraseDialog("decrypt");
        }

        private void encryptText(String passphrase){
            String s = et_input.getText().toString();
            //String deText = simpleDecrypt(s);
            SecretKeySpec sks = AESHelper0.keygenerate(passphrase);
            String deText = AESHelper0.v_encrypt(s, sks);
            et_output.setText(deText);
        }

        public void b_encrypt(View v){
            passphraseDialog("encrypt");
        }

    public void b_copyText(View v){
        String copyData = et_output.getText().toString();
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cld = ClipData.newPlainText("Text", copyData);
        cm.setPrimaryClip(cld);
        Toast.makeText(this,"Text Copied!", LENGTH_SHORT).show();
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
