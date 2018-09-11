package tecnocard.com.chiringuito;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView, reciboRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Producto> productoList, finalList;
    private AlertAdapter alertAdapter;
    private ImageAdapter imageAdapter;
    private ReciboAdapter reciboAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        finalList = new ArrayList<>();
        productoList = new ArrayList<>();
        productoList.add(new Producto(1,"Papas",15.0));
        productoList.add(new Producto(2, "Tostilocos", 25.0));
        productoList.add(new Producto(3, "Frutas", 20.5));
        productoList.add(new Producto(4, "Verduras", 22.5));
        productoList.add(new Producto(5, "Conchitas", 17.5));

        final TextView totalValueTextView = findViewById(R.id.totalValueTxtView);
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        reciboAdapter = new ReciboAdapter(finalList, productoList, totalValueTextView);
        imageAdapter = new ImageAdapter(productoList, finalList, reciboAdapter, totalValueTextView);
        recyclerView.setAdapter(imageAdapter);

        reciboRecyclerView = findViewById(R.id.reciboRecyclerView);
        reciboRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reciboRecyclerView.setHasFixedSize(true);
        reciboRecyclerView.addItemDecoration(
                new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        layoutManager = new LinearLayoutManager(this);
        reciboRecyclerView.setLayoutManager(layoutManager);
        reciboRecyclerView.setAdapter(reciboAdapter);

        Button readyBtn = findViewById(R.id.readyBtn);
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View alertView = inflater.inflate(R.layout.alert_layout, null);
                TextView totalAlertTxtView = alertView.findViewById(R.id.alertTotalValueTV);
                totalAlertTxtView.setText(totalValueTextView.getText());
                RecyclerView alertRecyclerView = alertView.findViewById(R.id.alertRv);
                layoutManager = new LinearLayoutManager(view.getContext());
                alertRecyclerView.setLayoutManager(layoutManager);
                alertAdapter = new AlertAdapter(reciboAdapter.Collapse(), totalValueTextView);
                alertRecyclerView.setAdapter(alertAdapter);
                builder.setView(alertView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reciboAdapter.removeAll();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();  //<-- See This!
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_productos) {
            Intent intent = new Intent(getApplicationContext(), ProductosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recargas) {

        }  else if (id == R.id.nav_ventas) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
