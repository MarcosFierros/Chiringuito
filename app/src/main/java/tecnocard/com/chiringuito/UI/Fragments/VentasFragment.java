package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnocard.com.chiringuito.ProductViewModel;
import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.RecyclerViewAdapters.AlertAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.ImageAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.ReciboAdapter;
import tecnocard.com.chiringuito.UI.MainActivity;
import tecnocard.com.chiringuito.UserViewModel;
import tecnocard.com.chiringuito.Usuario;

public class VentasFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private AlertAdapter alertAdapter;
    private ImageAdapter imageAdapter;
    private ReciboAdapter reciboAdapter;

    private List<Producto> finalList;
    private List<Producto> productoList;

    private SettingsFragment settingsFragment;

    AlertDialog alertDialog;
    UserViewModel mUserViewModel;
    private Usuario usuario;
    View view;

    public VentasFragment() {
        this.alertDialog = null;
        this.settingsFragment = null;
    }

    @SuppressLint("ValidFragment")
    public VentasFragment(SettingsFragment settingsFragment) {
        alertDialog = null;
        this.settingsFragment = settingsFragment;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ventas, container, false);

        finalList = new ArrayList<>();
        productoList = new ArrayList<>();

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        usuario = null;

        final TextView totalValueTextView = view.findViewById(R.id.totalValueTxtView);
        RecyclerView recyclerView = view.findViewById(R.id.imageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new GridLayoutManager(view.getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        reciboAdapter = new ReciboAdapter(finalList, productoList, totalValueTextView);
        imageAdapter = new ImageAdapter(productoList, finalList, reciboAdapter, totalValueTextView);
        recyclerView.setAdapter(imageAdapter);

        ProductViewModel productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, productos -> {
            productoList = productos;
            imageAdapter.setProducts(productos);
        });

        RecyclerView reciboRecyclerView = view.findViewById(R.id.finalListRecyclerView);
        reciboRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reciboRecyclerView.setHasFixedSize(true);
        reciboRecyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        layoutManager = new LinearLayoutManager(view.getContext());
        reciboRecyclerView.setLayoutManager(layoutManager);
        reciboRecyclerView.setAdapter(reciboAdapter);


        Button readyBtn = view.findViewById(R.id.readyBtn);
        readyBtn.setOnClickListener(view -> {

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater1 = LayoutInflater.from(view.getContext());
            @SuppressLint("InflateParams") View alertView = inflater1.inflate(R.layout.alert_layout, null);

            double saldo = usuario.getSaldo();
            double total = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
            final double newSaldo = saldo-total;

            TextView totalAlertTxtView = alertView.findViewById(R.id.alertTotalValueTV);
            totalAlertTxtView.setText("$ " + String.valueOf(total));
            TextView saldoAlertTxtView = alertView.findViewById(R.id.alertSaldoValueTV);
            saldoAlertTxtView.setText("$ " + String.valueOf(saldo));
            final TextView newSaldoAlertTxtView = alertView.findViewById(R.id.alertNewSaldoValueTV);
            newSaldoAlertTxtView.setText("$ " + String.valueOf(newSaldo));

            RecyclerView alertRecyclerView = alertView.findViewById(R.id.alertRv);
            layoutManager = new LinearLayoutManager(view.getContext());
            alertRecyclerView.setLayoutManager(layoutManager);
            alertAdapter = new AlertAdapter(Collapse());
            alertRecyclerView.setAdapter(alertAdapter);

            builder.setView(alertView);
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                usuario.setSaldo(newSaldo);
                mUserViewModel.edit(usuario);
                reciboAdapter.removeAll();
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(dialog -> {
                if(newSaldo < 0) {
                    newSaldoAlertTxtView.setTextColor(Color.RED);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    Toast toast = Toast.makeText(view.getContext(), "SALDO INSUFICIENTE",Toast.LENGTH_SHORT );
                    toast.show();
                }
            });

            alertDialog.show();
        });

        if (settingsFragment.isNFCOn()) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater1 = LayoutInflater.from(view.getContext());
            @SuppressLint("InflateParams") View alertView = inflater1.inflate(R.layout.ventas_alert_wait, null);
            builder.setView(alertView);
            alertDialog = builder.create();
            alertDialog.show();
        } else {
            try {
                usuario = mUserViewModel.get("1");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public List<Producto> Collapse(){

        List<Producto> collapsedList = new ArrayList<>();
        for(Producto p: productoList){
            p.setQty(0);
            collapsedList.add(p);
        }

        for(Producto p1: finalList){
            for(Producto p2: collapsedList){
                if(p1.compareTo(p2) == 0)
                    p2.addQty();
            }
        }

        for (Iterator<Producto> iterator = collapsedList.iterator(); iterator.hasNext(); ) {
            Producto p = iterator.next();
            if(p.getQty() ==0){
                iterator.remove();
            }
        }

        return collapsedList;
    }


    public void dismissAlert(){
        try {
            String uid = MainActivity.getUID();
            if(mUserViewModel.userExists(uid))
                usuario = mUserViewModel.get(uid);
            else{
                usuario = new Usuario( uid, 0);
                mUserViewModel.insert(usuario);
            }
            if(alertDialog != null)
                alertDialog.dismiss();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
