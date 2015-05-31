package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
// import android.app.ActionBar;


public class MenuActivity extends ActionBarActivity
{
    ImageButton przycisk_czatu;
    ImageButton przycisk_ustawien;
    Intent intent;
    Activity aktywnosc = this;
    String profil1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences settings = this.getSharedPreferences("PROFIL", 0);
        profil1 = settings.getString("profil", "null");

        SharedPreferences.Editor edytor = settings.edit();
        edytor.putString("profil", profil1);
        edytor.putString("profil_rozmowcy", "900358920032103");
        edytor.apply();

        przycisk_czatu = (ImageButton) findViewById(R.id.przycisk_czatu);
        przycisk_czatu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(aktywnosc, Czat.class);
                intent.putExtra("profil", profil1);
                startActivity(intent);
            }
        });

        przycisk_ustawien = (ImageButton) findViewById(R.id.przycisk_ustawien);
        przycisk_ustawien.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(aktywnosc, Ustawienia.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_ustawienia:
            {
                Intent intencja = new Intent(this, Ustawienia.class);
                startActivity(intencja);

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
