package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.concurrent.ExecutionException;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.MainActivity;
import tecnocard.com.chiringuito.UserViewModel;
import tecnocard.com.chiringuito.Usuario;

public class RecargasFragment extends Fragment {

    double total, saldo, recarga = 0;
    ArrayDeque<Double> valuesList = new ArrayDeque<>();
    private UserViewModel mUserViewModel;
    private Usuario usuario;

    TextView uidTextView;
    TextView saldoTextView, totalValueTextView,
            recargaValueTextView, saldoValueTextView;
    AlertDialog alertDialog;
    View view;

    SettingsFragment settingsFragment;

    public RecargasFragment(){
        this.settingsFragment = null;
    }
    @SuppressLint("ValidFragment")
    public RecargasFragment(SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recargas, container, false);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        usuario = null;
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

                uidTextView = view.findViewById(R.id.uidTextView);
                saldoTextView = view.findViewById(R.id.saldoTextView);

                saldo = usuario.getSaldo();
                uidTextView.setText(String.valueOf(usuario.getUid()));
                saldoTextView.setText("$ " + String.valueOf(saldo));
                recarga = 0;
                total = 0;
                setTextViews(saldo, recarga, total);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        totalValueTextView = view.findViewById(R.id.totalValueTxtView);
        saldoValueTextView = view.findViewById(R.id.saldoTVValue);
        recargaValueTextView = view.findViewById(R.id.recargaTVValue);

        Button btn10 = view.findViewById(R.id.Btn10);
        Button btn20 = view.findViewById(R.id.Btn20);
        Button btn50 = view.findViewById(R.id.Btn50);
        Button btn100 = view.findViewById(R.id.Btn100);
        Button btn200 = view.findViewById(R.id.Btn200);
        Button btn500 = view.findViewById(R.id.Btn500);
        btn10.setOnClickListener(new BtnListener(10));
        btn20.setOnClickListener(new BtnListener(20));
        btn50.setOnClickListener(new BtnListener(50));
        btn100.setOnClickListener(new BtnListener(100));
        btn200.setOnClickListener(new BtnListener(200));
        btn500.setOnClickListener(new BtnListener(500));

        Button okBtn = view.findViewById(R.id.okBtn);
        Button backBtn = view.findViewById(R.id.backBtn);
        okBtn.setOnClickListener(v -> {
            usuario.setSaldo(total);
            mUserViewModel.edit(usuario);
            saldo = usuario.getSaldo();
            saldoTextView.setText("$ " + String.valueOf(saldo));
            total = saldo;
            recarga = 0;
            setTextViews(saldo, recarga, total);
            valuesList.clear();
        });
        backBtn.setOnClickListener( v -> {
            if(!valuesList.isEmpty()) {
                recarga -= valuesList.removeLast();
                total = saldo + recarga;
                setTextViews(saldo, recarga, total);
            }
        });
        return view;
    }

    public void dismissAlert(){
        try {
            String uid = MainActivity.getUID();
            if(mUserViewModel.userExists(uid))
                usuario = mUserViewModel.get(uid);
            else{
                usuario = new Usuario(uid, 0);
                mUserViewModel.insert(usuario);
            }

            uidTextView = view.findViewById(R.id.uidTextView);
            saldoTextView = view.findViewById(R.id.saldoTextView);

            uidTextView.setText(String.valueOf(usuario.getUid()));
            String placeholder = "$ " + String.valueOf(usuario.getSaldo());
            saldoTextView.setText(placeholder);

            if(alertDialog != null)
                alertDialog.dismiss();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    class BtnListener implements View.OnClickListener {

        double value;
        BtnListener(double value){
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            saldo = usuario.getSaldo();
            recarga += value;
            total = saldo + recarga;

            setTextViews(saldo, recarga, total);
            valuesList.offer(value);

        }
    }


    void setTextViews(double saldo, double recarga, double total) {
        String placeHolder = "$ " + String.valueOf(saldo);
        saldoValueTextView.setText(placeHolder);
        placeHolder = "$ " + String.valueOf(recarga);
        recargaValueTextView.setText(placeHolder);
        placeHolder = "$ " + String.valueOf(total);
        totalValueTextView.setText(placeHolder);
    }

}
