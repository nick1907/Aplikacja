package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class Ustawienia extends ActionBarActivity
{
    // lista potrzebna do ustawienia spinneru na wybor orientacji seksualnej
    ArrayList<String> orientacja;

    Spinner wybor_orientacji;
    Spinner wybor_dnia;
    Spinner wybor_miesiaca;
    Spinner wybor_roku;

    String wybrana_orientacja;
    EditText opis_pole; // opis uzytkownika (o sobie)
    EditText zasieg_pole; // zasieg w kilometrach potrzebny do poszukiwan drugiej "polowki"
    EditText miejscowosc_pole; // pole na miejscowosc

    // listy potrzebne do Spinnerow przy ustawianiu daty urodzenia
    ArrayList<String> dni;
    ArrayList<String> miesiace;
    ArrayList<String> lata;

    String wybrany_dzien;
    String wybrany_miesiac;
    String wybrany_rok;

    String profil_ID;
    String pass;

    static final String ostatni_opis = "opis";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);

        SharedPreferences settings = getSharedPreferences("PROFIL", 0);
        if (settings != null)
        {
            profil_ID = settings.getString("profil", "null");
            pass = settings.getString("profil", "null");
        }

        // przypisanie odpowiednich pol tekstowych
        opis_pole = (EditText) findViewById(R.id.pole_opisu);
        zasieg_pole = (EditText) findViewById(R.id.pole_zasiegu);
        miejscowosc_pole = (EditText) findViewById(R.id.miejscowosc_pole);

        wybor_orientacji = (Spinner) findViewById(R.id.wybor_orientacji);
        // etykiety do spinnera z wyborem orientacji
        orientacja = new ArrayList<>();
        orientacja.add("heteroseksualna");
        orientacja.add("biseksualna");
        orientacja.add("homoseksualna");

        wybrana_orientacja = "";

        // dodanie adaptera do spinnera
        ArrayAdapter<String> orientacja_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orientacja);
        orientacja_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_orientacji.setAdapter(orientacja_adapter);
        wybor_orientacji.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                wybrana_orientacja = orientacja.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        // ustawienie listy z dniami miesiecy
        dni = new ArrayList<>();
        for (int i = 1; i < 32; ++i)
            dni.add(Integer.toString(i));

        // dodanie adaptera do spinnera z dniami miesiecy
        wybor_dnia = (Spinner) findViewById(R.id.wybor_dnia);
        ArrayAdapter adapter_dni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dni);
        adapter_dni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_dnia.setAdapter(adapter_dni);

        wybrany_dzien = "";

        wybor_dnia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                wybrany_dzien = dni.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // wstawine do listy nazw miesiecy
        miesiace = new ArrayList<>();
        miesiace.add("Styczeń");
        miesiace.add("Luty");
        miesiace.add("Marzec");
        miesiace.add("Kwiecień");
        miesiace.add("Maj");
        miesiace.add("Czerwiec");
        miesiace.add("Lipiec");
        miesiace.add("Sierpień");
        miesiace.add("Wrzesień");
        miesiace.add("Październik");
        miesiace.add("Listopad");
        miesiace.add("Grudzień");

        // w przyszlosci zaimplementowac:
        // Spinner.setSelection(int position, boolean animate)

        // dodanie adaptera do spinnera z miesiacami
        wybor_miesiaca = (Spinner) findViewById(R.id.wybor_miesiaca);
        ArrayAdapter adapter_miesiecy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, miesiace);
        adapter_miesiecy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_miesiaca.setAdapter(adapter_miesiecy);

        wybrany_miesiac = "";
        wybor_miesiaca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                wybrany_miesiac = Integer.toString(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        // pobranie aktualnego roku
        Calendar kalendarz = Calendar.getInstance();
        int rok = kalendarz.get(Calendar.YEAR);

        // zapisanie lat do listy
        lata = new ArrayList<>();
        // wstawienie lat do listy
        for (int i = rok - 10; i > (rok - 66) ; --i)
            lata.add(Integer.toString(i));

        wybrany_rok = "";

        // dodanie adaptera do spinnera z latami
        wybor_roku = (Spinner) findViewById(R.id.wybor_roku);
        ArrayAdapter<String> adapter_lat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lata);
        adapter_lat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wybor_roku.setAdapter(adapter_lat);
        wybor_roku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wybrany_rok = lata.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (savedInstanceState != null)
            opis_pole.setText((String)savedInstanceState.get(ostatni_opis));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(ostatni_opis, opis_pole.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_ustawienia, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // metoda do aktualizacji danych w bazie danych
    public void zaaktualizujDane(View widok)
    {
        new ZmienUstawienia().execute();

        finish();
    }

    class ZmienUstawienia extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            String opis = opis_pole.getText().toString();
            int zasieg = 0;
            if (!zasieg_pole.getText().toString().isEmpty())
                zasieg = Integer.parseInt(zasieg_pole.getText().toString(), 0);
            String miejscowosc = miejscowosc_pole.getText().toString();

            // utworzenie zapytania do bazy danych i jego wykoanie
            try
            {
                int rozmiar;

                // zapytanie do bazy danych (zmiana danych uzytkownika programu)
                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(profil_ID, "UTF-8");
                data += "&" + URLEncoder.encode("haslo", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

                rozmiar = data.length(); // pobranie aktualnego rozmiaru danych

                if (!opis.isEmpty()) // jesli opis nie jest pusty
                    data += "&" + URLEncoder.encode("opis", "UTF-8") + "=" + URLEncoder.encode(opis, "UTF-8");
                if (zasieg > 0) // jesli podano zasieg
                    data += "&" + URLEncoder.encode("zasieg", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(zasieg), "UTF-8");
                if (!miejscowosc.isEmpty()) // jesli podano miejscowosc
                    data += "&" + URLEncoder.encode("miejscowosc", "UTF-8") + "=" + URLEncoder.encode(miejscowosc, "UTF-8");
                if (!wybrana_orientacja.isEmpty()) // jesli wybrano orientacje
                    data += "&" + URLEncoder.encode("orientacja", "UTF-8") + "=" + URLEncoder.encode(wybrana_orientacja, "UTF-8");
                if (!wybrany_dzien.isEmpty()) // jesli wybrano dzien miesiaca
                    data += "&" + URLEncoder.encode("dzien_urodzenia", "UTF-8") + "=" + URLEncoder.encode(wybrany_dzien, "UTF-8");
                if (!wybrany_miesiac.isEmpty()) // jesli wybrano miesiac
                    data += "&" + URLEncoder.encode("miesiac_urodzenia", "UTF-8") + "=" + URLEncoder.encode(wybrany_miesiac, "UTF-8");
                if (!wybrany_rok.isEmpty()) // jesli wybrano rok
                    data += "&" + URLEncoder.encode("rok_urodzenia", "UTF-8") + "=" + URLEncoder.encode(wybrany_rok, "UTF-8");

                if (rozmiar != data.length()) // jesli zmieniono dane
                {
                    try {
                        URL url = new URL("http://vigorous-cheetah-65-226242.euw1.nitrousbox.com/update_uzytkownika.php");

                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        // wyslij zapytanie do bazy danych
                        // String text = "";
                        BufferedReader reader;
                        wr.write(data);
                        wr.flush();

                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;

                        // odczytaj odpowiedz serwera
                        while ((line = reader.readLine()) != null)
                            sb.append(new StringBuilder(line + "\n"));

                        Log.d("wiadomosc", sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_menu:
            {
                finish();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // wywolanie aktywnosci do zrobienia zdjecia profilowego
    public void zrobZdjecie(View view)
    {
        Intent intencja = new Intent(this, Zdjecie.class);
        startActivity(intencja);
    }
}
