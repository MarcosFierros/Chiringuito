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

import java.util.concurrent.ExecutionException;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.MainActivity;
import tecnocard.com.chiringuito.UserViewModel;
import tecnocard.com.chiringuito.Usuario;

public class RecargasFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private Usuario usuario;

    TextView uidTextView;
    TextView saldoTextView;
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

                uidTextView.setText(String.valueOf(usuario.getUid()));
                saldoTextView.setText("$ " + String.valueOf(usuario.getSaldo()));

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

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
            usuario.setSaldo(usuario.getSaldo() + value);
            mUserViewModel.edit(usuario);
            String placeHolder = "$ " + String.valueOf(usuario.getSaldo());
            saldoTextView.setText(placeHolder);
        }
    }

}
