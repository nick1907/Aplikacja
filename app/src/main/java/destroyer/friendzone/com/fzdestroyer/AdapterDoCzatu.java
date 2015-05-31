package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// adapter do listy typu ListView
public class AdapterDoCzatu extends ArrayAdapter<Element>
{
    private Context kontekst;
    private int layout_id;
    private Element [] tablica;

    public AdapterDoCzatu(Context context, int layout_id, Element [] elementy)
    {
        super(context, layout_id, elementy);
        kontekst = context;
        this.layout_id = layout_id;
        tablica = elementy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View widok = convertView;
        Item item = null;

        if (widok == null)
        {
            LayoutInflater layoutInflater = ((Activity)kontekst).getLayoutInflater();
            widok = layoutInflater.inflate(layout_id, parent, false);

            item = new Item();
            item.obrazek = (ImageView)widok.findViewById(R.id.miniaturka_zdjecia);
            item.nazwa_uzytkownika = (TextView)widok.findViewById(R.id.nazwa_uzytkownika);

            widok.setTag(item);
        }
        else
            item = (Item)widok.getTag();

        Element nowy = tablica[position];

        String nazwa;

        if (!(nowy.imie.equals("")))
        {
            nazwa = nowy.imie + " " + nowy.nazwisko + " ";
            nazwa += "(" + nowy.login + ")";
        }
        else
            nazwa = nowy.login;

        item.nazwa_uzytkownika.setText(nazwa);
        item.obrazek.setImageResource(nowy.obrazek);

        return widok;
    }

    static class Item
    {
        ImageView obrazek;
        TextView nazwa_uzytkownika;
    }
}
