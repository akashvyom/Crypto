package crypto.amitvishalanujnexus.com.crypto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.crypto.spec.SecretKeySpec;


public class file_encryption extends ActionBarActivity {

    private  String fileData;
    private String fileName;
    private String decryptedData;
    private String encryptedData;
    private SecretKeySpec sks;
    private String filePath = null;
    public static final int REQUEST_SAVE = 1;
    public final int ACTIVITY_CHOOSE_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_encryption);

    }

    public void b_chooseFile(View v){
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE: {
                if (resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    filePath = uri.getPath();
                    TextView tv_fileloc = (TextView)findViewById(R.id.tv_filechoose);
                    tv_fileloc.setText(filePath);
                }
            }
        }
    }

    private void b_readFile(){
        try {
            String[] mm = filePath.split("/");
            fileName = mm[4];
            File myFile = new File("/sdcard/"+mm[4]);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }

            fileData = aBuffer;
            Log.d("data",fileData);
            myReader.close();
            Toast.makeText(getBaseContext(),
                    "Done reading file",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void passphraseDialog(final String what){
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
                if(what.contentEquals("encrypt")){
                encryptFile(value);}
                else if(what.contentEquals("decrypt"))
                {decryptFile(value);}
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

    public void b_encryptFile(View v){
        b_readFile();
        passphraseDialog("encrypt");
    }
    /*
    private void open_filechooser(){
        Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");

        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);

        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });

        startActivityForResult(intent, REQUEST_SAVE);
    }*/

    private void encryptFile(String passphrase){
        sks = AESHelper0.keygenerate(passphrase);
        try{
            encryptedData = AESHelper0.v_encrypt(fileData,sks);}
        catch(Exception e){
            Toast.makeText(this,"Unable to encrypt file.",Toast.LENGTH_SHORT).show();
        }
        createFile(encryptedData,fileName+"_encrypted");
    }

    public void b_decryptFile(View v){
        b_readFile();
        passphraseDialog("decrypt");
    }

    private void decryptFile(String passphrase){
        sks = AESHelper0.keygenerate(passphrase);

        try{
            decryptedData = AESHelper0.v_decrypt(fileData, sks);
            createFile(decryptedData,fileName+"_decrypted");
        }
        catch(Exception e ){
            Toast.makeText(this,"Unable to decrypt file",Toast.LENGTH_SHORT).show();
        }

    }

    private void createFile(String textData,String fileName){
        try {
            File myFile = new File("/sdcard/"+fileName);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(textData);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(getBaseContext(),
                    "Done writing SD file'",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_encryption, menu);
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
