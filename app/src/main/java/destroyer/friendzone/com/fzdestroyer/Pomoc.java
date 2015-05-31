package destroyer.friendzone.com.fzdestroyer;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by ehhh on 29.05.15.
 */
public class Pomoc extends Activity{

    private ListView listaHelp ;
    private String[] tutajpomusz;;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomoc);


        listaHelp = (ListView) findViewById(R.id.listHelp);
        initResources1();

    }

    private void initResources1() {
        Resources res = getResources();
        tutajpomusz = res.getStringArray(R.array.tutajpomusz);
        initListaHelp();
    }
    private void initListaHelp() {
        listaHelp.setBackgroundColor(Color.RED);
        listaHelp.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,tutajpomusz));
    }

}