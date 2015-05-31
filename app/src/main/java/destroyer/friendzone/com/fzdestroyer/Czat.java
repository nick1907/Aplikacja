package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

// klasa zajmujaca sie calym czatem
public class Czat extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_czat);

        FragmentManager fm = getSupportFragmentManager();
        OknoCzatu oknoCzatu = (OknoCzatu)fm.findFragmentById(R.id.okno_czatu_fragment);

        FragmentTransaction ft = fm.beginTransaction();
        ft.commit();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_czat, menu);
        return true;
    }
}
