package destroyer.friendzone.com.fzdestroyer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AdapterWiadomosci extends ArrayAdapter<Wiadomosc>
{
    Context kontekst;
    int layout_id;
    List<Wiadomosc> lista_wiadomosci;

    public AdapterWiadomosci(Context context, int layout_id, List<Wiadomosc> lista)
    {
        super(context, layout_id, lista);
        kontekst = context;
        this.layout_id = layout_id;
        lista_wiadomosci = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater;
        View widok = convertView;
        Item dodawany;

        if (widok == null)
        {
            layoutInflater = ((Activity)kontekst).getLayoutInflater();
            widok = layoutInflater.inflate(layout_id, parent, false);

            // przypisz uchwyty do nowej wiadomosci
            dodawany = new Item();
            dodawany.obrazek_rozmowcy = (ImageView) widok.findViewById(R.id.zdjecie_rozmowcy);
            dodawany.wiadomosc_rozmowcy = (TextView) widok.findViewById(R.id.wiadomosc_rozmowcy);
            dodawany.nasz_obrazek = (ImageView) widok.findViewById(R.id.nasze_zdjecie);
            dodawany.nasza_wiadomosc = (TextView) widok.findViewById(R.id.nasza_wiadomosc);
            widok.setTag(dodawany);
        }
        else
            dodawany = (Item) widok.getTag();

        Wiadomosc nowa = lista_wiadomosci.get(position);

        if (nowa.czy_od_rozmowcy) // jesli to od naszego rozmowcy
        {
            dodawany.obrazek_rozmowcy.setImageResource(nowa.zdjecie_rozmowcy);
            dodawany.wiadomosc_rozmowcy.setText(nowa.tresc);
        }
        else // jesli to od biezacego uzytkownika
        {
            dodawany.obrazek_rozmowcy.setImageResource(nowa.zdjecie_uzytkownika);
            dodawany.wiadomosc_rozmowcy.setText(nowa.tresc);
        }

        return widok;
    }

    static class Item
    {
        ImageView obrazek_rozmowcy;
        TextView wiadomosc_rozmowcy;
        ImageView nasz_obrazek;
        TextView nasza_wiadomosc;
    }
}
