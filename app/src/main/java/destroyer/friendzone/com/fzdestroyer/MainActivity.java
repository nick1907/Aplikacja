package destroyer.friendzone.com.fzdestroyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

// glowna klasa zajmujaca sie logowaniem i opcjami programu
public class MainActivity extends FragmentActivity
{
    private CallbackManager callback;
    Logowanie logowanie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // inicjalizacja FACEBOOK API
        FacebookSdk.sdkInitialize(this);

        String profil = null;

        SharedPreferences settings = getSharedPreferences("PROFIL", 0);

        Toast.makeText(this, "Cofnalem sie!", Toast.LENGTH_SHORT).show();

        if (settings != null)
            profil = settings.getString("profil", "null");

        callback = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        // tworzenie nowego fragmentu (fragmentu logowania)
        FragmentManager fm = getSupportFragmentManager();
        logowanie = (Logowanie) fm.findFragmentById(R.id.logowanie_fragment);

        FragmentTransaction ft = fm.beginTransaction();
        ft.commit();

        // jesli zalogowany
        if (AccessToken.getCurrentAccessToken() != null && profil != null && !profil.equals("null")) // jesli jest zalogowany
        {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "Jestem", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
