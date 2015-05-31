package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Zdjecie extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdjecie);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    String sciezka = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zdjecie, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap bitmapa = (Bitmap) data.getExtras().get("data");
            bitmapa.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            try
            {
                File zdjecie_profilowe = null;
                Calendar kalendarz = Calendar.getInstance();
                String nazwa_pliku = "profil_" + kalendarz.get(Calendar.YEAR) + kalendarz.get(Calendar.MONTH)
                        + kalendarz.get(Calendar.DAY_OF_MONTH) + kalendarz.get(Calendar.HOUR)
                        + kalendarz.get(Calendar.MINUTE) + kalendarz.get(Calendar.SECOND);

                String rozszerzenie = ".jpg";

                sciezka = Environment.getExternalStorageDirectory() + File.separator + nazwa_pliku + rozszerzenie;
                // otworz katalog ze zdjeciami
                File plik = new File(sciezka);
                plik.createNewFile();
                FileOutputStream fos = new FileOutputStream(plik);
                fos.write(bytes.toByteArray());
                fos.close();

                // zapisz sciezke do zdjecia do ustawien profilu
                SharedPreferences settings = this.getSharedPreferences("PROFIL", 0);
                SharedPreferences.Editor edytor = settings.edit();
                edytor.putString("zdjecie", sciezka);
                edytor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        finish();
    }
}
