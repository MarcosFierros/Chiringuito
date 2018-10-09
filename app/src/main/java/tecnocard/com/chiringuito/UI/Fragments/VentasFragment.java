package tecnocard.com.chiringuito.UI.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

//  Variables de UI
    View view;
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    AlertDialog alertDialog;
    TextView totalValueTextView;
    TextView saldoValueTextView;
    TextView saldoRValueTextView;

//  Adaptadores de recyclerView
    RecyclerView.LayoutManager layoutManager;
    AlertAdapter alertAdapter;
    ImageAdapter imageAdapter;
    ReciboAdapter reciboAdapter;

//  Listas de recyclerView
    List<Producto> finalList;
    List<Producto> productoList;

//  Variables Room
    UserViewModel mUserViewModel;
    Usuario usuario;

//  Arguments
    boolean settingsNFC;
    boolean isReading;
    double saldo;
    double total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ventas_include, container, false);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        readBundle(getArguments());

        layoutBottomSheet = view.findViewById(R.id.test_layout);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        totalValueTextView = view.findViewById(R.id.totalValueTxtView);
        saldoValueTextView = view.findViewById(R.id.saldoValueTxtView);
        saldoRValueTextView = view.findViewById(R.id.saldoRValueTxtView);
        RecyclerView recyclerView = view.findViewById(R.id.imageRecyclerView);
        RecyclerView reciboRecyclerView = view.findViewById(R.id.finalListRecyclerView);
        Button readyBtn = layoutBottomSheet.findViewById(R.id.readyBtn);

        finalList = new ArrayList<>();
        productoList = new ArrayList<>();
        reciboAdapter = new ReciboAdapter(finalList, totalValueTextView, saldoRValueTextView, saldo);
        imageAdapter = new ImageAdapter(productoList, finalList, reciboAdapter, totalValueTextView, saldoRValueTextView, saldo);
        showAlert(saldoValueTextView, saldoRValueTextView);

        layoutManager = new GridLayoutManager(view.getContext(), 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration( new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);

        layoutManager = new LinearLayoutManager(view.getContext());
        reciboRecyclerView.setHasFixedSize(true);
        reciboRecyclerView.addItemDecoration( new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        reciboRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reciboRecyclerView.setLayoutManager(layoutManager);
        reciboRecyclerView.swapAdapter(reciboAdapter, true);

        ProductViewModel productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, productos -> {
            productoList = productos;
            imageAdapter.setProducts(productos);
        });

        readyBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            View alertView = View.inflate(getContext() ,R.layout.alert_layout, null);

            total = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
            double newSaldo = saldo-total;

            TextView totalAlertTxtView = alertView.findViewById(R.id.alertTotalValueTV);
            String placeholder = "$ " + String.valueOf(total);
            totalAlertTxtView.setText(placeholder);
            TextView saldoAlertTxtView = alertView.findViewById(R.id.alertSaldoValueTV);
            placeholder = "$ " + String.valueOf(saldo);
            saldoAlertTxtView.setText(placeholder);
            TextView newSaldoAlertTxtView = alertView.findViewById(R.id.alertNewSaldoValueTV);
            placeholder = "$ " + String.valueOf(newSaldo);
            newSaldoAlertTxtView.setText(placeholder);

            RecyclerView alertRecyclerView = alertView.findViewById(R.id.alertRv);
            layoutManager = new LinearLayoutManager(view.getContext());
            alertRecyclerView.setLayoutManager(layoutManager);
            alertAdapter = new AlertAdapter(Collapse());
            alertRecyclerView.setAdapter(alertAdapter);

            builder.setView(alertView);
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                usuario.setSaldo(saldo-total);
                mUserViewModel.edit(usuario);
                reciboAdapter.removeAll();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                View alertView1 = View.inflate(getContext(), R.layout.confirm_layout, null);
                TextView newSaldoValueTextView = alertView1.findViewById(R.id.newSaldoValueTextView);
                String placeholder1 = "$ " + usuario.getSaldo();
                newSaldoValueTextView.setText(placeholder1);
                placeholder1 = "$ 0.00";
                totalValueTextView.setText(placeholder1);
                saldoValueTextView.setText(placeholder1);
                saldoRValueTextView.setText(placeholder1);

                builder1.setPositiveButton("Ok", (dialog, which) -> showAlert(saldoValueTextView, saldoRValueTextView));
                builder1.setView(alertView1);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            });
            builder.setNegativeButton("Cancelar", null);
            AlertDialog alertDialog = builder.create();

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

        return view;

    }

    public static VentasFragment newInstance(boolean settingsNFC) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("settingsNFC", settingsNFC);

        VentasFragment fragment = new VentasFragment();
        fragment.setArguments(bundle);

        return fragment;
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

    public void setSettingsNFC(boolean settingsNFC) {
        this.settingsNFC = settingsNFC;
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
            Log.i("Ventas Fragment", "SALDO USUARIO: " + usuario.getSaldo());

            total = 0;
            saldo = usuario.getSaldo();
            String placeholder = "$ "  + total;
            totalValueTextView.setText(placeholder);
            placeholder = "$ " + saldo;
            saldoValueTextView.setText(placeholder);
            placeholder = "$ " + saldo;
            saldoRValueTextView.setText(placeholder);
            imageAdapter.setSaldo(saldo);
            reciboAdapter.setSaldo(saldo);
            isReading = false;

            if(alertDialog != null)
                alertDialog.dismiss();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void readBundle(Bundle bundle) {
        if (bundle != null)
            settingsNFC = bundle.getBoolean("settingsNFC");
    }

    void showAlert(TextView saldoValueTextView, TextView saldoRValueTextView) {
        saldo = 0;
        total = 0;
        if (settingsNFC) {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            View alertView = View.inflate(getContext(), R.layout.ventas_alert_wait, null);
            builder.setView(alertView);
            alertDialog = builder.create();
            alertDialog.show();
            isReading = true;
        } else {

            try {
                usuario = mUserViewModel.get("1");
                saldo = usuario.getSaldo();
                total = saldo;
                reciboAdapter.setSaldo(saldo);
                imageAdapter.setSaldo(saldo);
                String placeHolder = "$ " + saldo;
                saldoValueTextView.setText(placeHolder);
                placeHolder = "$ " + total;
                saldoRValueTextView.setText(placeHolder);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isReading() {
        return isReading;
    }
}
