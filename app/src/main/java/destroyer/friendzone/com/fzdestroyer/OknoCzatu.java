package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

// to bedzie fragment zajmujacy sie obsluga czatu
public class OknoCzatu extends Fragment
{
    ListView lista;
    Activity aktywnosc;
    ArrayList<Wiadomosc> wiadomosci;
    AdapterWiadomosci adapterWiadomosci;
    Bundle dane;
    String profil1;
    String profil2;
    String sciezka_do_zdjecia;

    // odswieza liste
    Handler handlerPobierania = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (wiadomosci != null) {
                // odswierz liste
                adapterWiadomosci = new AdapterWiadomosci(getActivity(), R.layout.wiadomosc_czatu, wiadomosci);
                lista.setAdapter(adapterWiadomosci);
                adapterWiadomosci.notifyDataSetChanged();
            }
        }
    };

    Handler handlerWysylania = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            pole_tekstowe.setText("");
        }
    };

    ImageButton przycisk_wyslania;
    EditText pole_tekstowe;

    public OknoCzatu()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View widok = inflater.inflate(R.layout.fragment_okno_czatu, container, false);

        dane = getActivity().getIntent().getExtras();

        // wczytanie danych profilowych
        SharedPreferences settings = getActivity().getSharedPreferences("PROFIL", 0);
        profil1 = settings.getString("profil", "null");
        profil2 = settings.getString("profil_rozmowcy", "null");
        sciezka_do_zdjecia = settings.getString("zdjecie", "null");

        aktywnosc = getActivity();
        wiadomosci = new ArrayList<>();
        lista = (ListView) widok.findViewById(R.id.rozmowa);
        adapterWiadomosci = new AdapterWiadomosci(getActivity(), R.layout.wiadomosc_czatu, wiadomosci);

        lista.setAdapter(adapterWiadomosci);

        // zacznij pobierac nowe wiadomosci
        new Thread(new PobieranieWiadomosci()).start();

        pole_tekstowe = (EditText) widok.findViewById(R.id.nowa_wiadomosc);
        pole_tekstowe.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                    new WyslijWiadomosc().execute();

                return false;
            }
        });

        // dodanie sluchacza do przycisku wyslania wiadomosci
        przycisk_wyslania = (ImageButton) widok.findViewById(R.id.przycisk_wyslania_wiadomosci);
        przycisk_wyslania.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new WyslijWiadomosc().execute();
            }
        });


        return widok;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        handlerPobierania = null;
        handlerWysylania = null;
        lista = null;
        aktywnosc = null;
    }

    class WyslijWiadomosc extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            String wczytany_tekst = pole_tekstowe.getText().toString();

            //jesli podano jakas tresc
            if (!wczytany_tekst.isEmpty()) {
                try {
                    // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                    String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                    data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");
                    data += "&" + URLEncoder.encode("wiadomosc", "UTF-8") + "=" + URLEncoder.encode(wczytany_tekst, "UTF-8");

                    try {
                        URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/wstaw_wiadomosc_rev3.php");

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        BufferedReader reader;
                        wr.write(data);
                        wr.flush();

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        int numer_linii = 0;
                        Wiadomosc wiadomosc = new Wiadomosc();

                        // odczytaj odpowiezdz serwera
                        while ((line = reader.readLine()) != null)
                        {
                            // jesli udalo sie wyslac wiadomosc to wyczysc pole do wpisywania tresci
                            if (line.equals("OK") && handlerWysylania != null)
                                handlerWysylania.sendMessage(new Message());
                        }

                        reader.close();
                        wr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    class PobieranieWiadomosci implements Runnable
    {
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                new PobierzWiadomosc().execute();

                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                wiadomosci.clear();
            }
        }
    }

    // funkcja odwraca liste
    private void odwrocListe()
    {
        ArrayList<Wiadomosc> temp = new ArrayList<>();
        for (int i = wiadomosci.size() - 1; i >= 0; --i)
            temp.add(wiadomosci.get(i));

        wiadomosci = temp;
    }


    // funkcja pobierajaca wiadomosci zgodnie z oczekiwaniami
    class PobierzWiadomosc extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                // zapytanie do bazy danych (utworzenie nowego uzytkownika programu)
                String data = URLEncoder.encode("login_nadawcy", "UTF-8") + "=" + URLEncoder.encode(profil1, "UTF-8");
                data += "&" + URLEncoder.encode("login_odbiorcy", "UTF-8") + "=" + URLEncoder.encode(profil2, "UTF-8");
                data += "&" + URLEncoder.encode("ilosc_wiadomosci", "UTF-8") + "=" + URLEncoder.encode("5", "UTF-8");

                try
                {
                    URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/odczytaj_wiadomosc2.php");

                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    BufferedReader reader;
                    wr.write(data);
                    wr.flush();

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    int numer_linii = 0;
                    Wiadomosc pobrana_wiadomosc = new Wiadomosc();

                    pobrana_wiadomosc.zdjecie_uzytkownika = R.drawable.ja;
                    pobrana_wiadomosc.zdjecie_rozmowcy = R.drawable.rozmowca;

                    // TODO
                    // wstawianie zdjecia uzytkownika (do czatu)
                    /*
                    Bitmap bitmap;
                    // jesli jest zdjecie profilowe
                    if (sciezka_do_zdjecia != null && !sciezka_do_zdjecia.equals("null")) {
                        File plik = new File(sciezka_do_zdjecia);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        bitmap = BitmapFactory.decodeFile(sciezka_do_zdjecia, options);

                        pobrana_wiadomosc.zdjecie_uzytkownika = bitmap.
                    }
                    */

                    // odczytaj odpowiedz serwera
                    while ((line = reader.readLine()) != null)
                    {
                        Log.d("serwer_odeslal:", line);
                        if (numer_linii == 0) {
                            if (line.equals("Error") || line.equals("Brak"))
                                break;
                            ++numer_linii;
                        }
                        else if (numer_linii == 1) // ID nadawcy
                        {
                            if (line.equals(profil2)) // wiadomosc nie jest od nas
                                pobrana_wiadomosc.czy_od_rozmowcy = true;
                            else
                                pobrana_wiadomosc.czy_od_rozmowcy = false;

                            ++numer_linii;
                        }
                        else if (numer_linii == 2) // ID odbiorcy
                            ++numer_linii;
                        else if (numer_linii == 3) // tresc wiadomosci
                        {
                            pobrana_wiadomosc.tresc = line;

                            ++numer_linii;
                        }

                        if (numer_linii == 4)
                        {
                            wiadomosci.add(pobrana_wiadomosc);
                            pobrana_wiadomosc = new Wiadomosc();

                            // TODO
                            // chwilowa wersja na stale przypisujaca obrazki do rozmowcow
                            pobrana_wiadomosc.tresc = "";
                            pobrana_wiadomosc.zdjecie_rozmowcy = R.drawable.rozmowca;
                            pobrana_wiadomosc.zdjecie_uzytkownika = R.drawable.ja;

                            numer_linii = 0; // pobierz nastepna wiadomosc
                        }
                    }

                    if (wiadomosci.size() > 0)
                    {
                        odwrocListe();

                        if (handlerPobierania != null)
                            handlerPobierania.sendMessage(new Message());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
