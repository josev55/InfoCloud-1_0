package com.example.InfoCloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import cl.colabra.adapters.MenuAdapter;
import cl.colabra.pojos.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);
        List menuItems = new ArrayList();
        menuItems.add(new MenuModel("Nuevo",R.drawable.nuevo));
        menuItems.add(new MenuModel("Borradores",R.drawable.edit));
        menuItems.add(new MenuModel("Bandeja de Salida", R.drawable.outbox));
        menuItems.add(new MenuModel("Ajustes",R.drawable.settings));
        ListView menuList = (ListView)findViewById(R.id.menu_list);
        menuList.setAdapter(new MenuAdapter(this,menuItems));
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i){
                    case 0:
                        intent = new Intent();
                        intent.setClass(getApplicationContext(),FormListView.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent();
                        intent.setClass(getApplicationContext(),DraftActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
