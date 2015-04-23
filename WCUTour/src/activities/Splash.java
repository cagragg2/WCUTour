package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import edu.wcu.wcutour.R;

/**
 * Class to display a Splash screen at the start of the Applicaiton.
 * Shows a progress dialog and attepts to download the file of tours from
 * agora.
 */
public class Splash extends Activity {

    /**Dialog box to show while downloading.*/
    private ProgressDialog mProgressDialog;


    /**
     * OnCreate method to set the content view and start downloading the file.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startDownload();
    }

    /**
     * Method to start the async task of downloading the file from
     * "http://agora.cs.wcu.edu/~cagragg2/Tours.zip"
     */
    private void startDownload() {
        String url = "http://agora.cs.wcu.edu/~cagragg2/Tours.zip";
        new DownloadFileAsync().execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    /**
     * Async class to download the Tours.zip file from agora.
     * On pre execute displays a progress dialog and updates it as the file
     * downloads.
     */
    class DownloadFileAsync extends AsyncTask<String, String, String> {
        /**
         * Shows the dialog box and updates its settings.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(Splash.this, "Downloading",
                    "Downloading Tours....", true);
            mProgressDialog = new ProgressDialog(Splash.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        /**
         * Method to download from the server
         * @param aurl The url to use.
         * @return
         */
        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                /*for the progress dialog.*/
                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());

                OutputStream output;
                output = openFileOutput("Tours.zip", Context.MODE_PRIVATE);


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            Intent i = new Intent(Splash.this,MainActivity.class);
            mProgressDialog.dismiss();
            startActivity(i);

        }
    }
}
