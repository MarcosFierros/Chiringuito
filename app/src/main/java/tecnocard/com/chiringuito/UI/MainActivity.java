package tecnocard.com.chiringuito.UI;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.defaultimpl.Utilities;
import com.nxp.nfclib.interfaces.IUtility;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.Fragments.ProductosFragment;
import tecnocard.com.chiringuito.UI.Fragments.RecargasFragment;
import tecnocard.com.chiringuito.UI.Fragments.SettingsFragment;
import tecnocard.com.chiringuito.UI.Fragments.VentasFragment;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private NxpNfcLib m_linInstance = null;
    private static byte[] UID;

    VentasFragment ventasFragment;
    ProductosFragment productosFragment;
    RecargasFragment recargasFragment;
    SettingsFragment settingsFragment;
    Fragment selectedFragment;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        settingsFragment = new SettingsFragment();
        ventasFragment = VentasFragment.newInstance(settingsFragment.isNFCOn());
        productosFragment = new ProductosFragment();
        recargasFragment = new RecargasFragment(settingsFragment);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, ventasFragment).commit();

        intializeLibrary();

    }

    @Override
    protected void onResume() {
        m_linInstance.startForeGroundDispatch();
        super.onResume();
    }

    @Override
    protected void onPause() {
        m_linInstance.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        cardLogic(intent);
        super.onNewIntent(intent);
    }

    private void cardLogic(final Intent intent) {
        Tag myTag =  intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        UID = myTag.getId();
        if (selectedFragment.equals(ventasFragment) && ventasFragment.isReading())
            ventasFragment.dismissAlert();
        else
            recargasFragment.dismissAlert();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {

                switch (menuItem.getItemId()){
                    case R.id.nav_ventas:
                        ventasFragment = new VentasFragment();
                        ventasFragment.setSettingsNFC(settingsFragment.isNFCOn());
                        selectedFragment = ventasFragment;
                        break;
                    case R.id.nav_products:
                        selectedFragment = productosFragment;
                        break;
                    case R.id.nav_recargas:
                        selectedFragment = recargasFragment;
                        break;
                    case R.id.nav_settings:
                        selectedFragment = settingsFragment;
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,
                            selectedFragment).commit();
                }

                return true;

            };

    private void intializeLibrary() {
        m_linInstance = NxpNfcLib.getInstance();
        String m_strKey = "a98dfaaa612de680d5153dfa80b33283";
        m_linInstance.registerActivity(this, m_strKey);
    }

    public static String getUID(){
        if(UID != null) {
            Utilities utils = new Utilities();
            return utils.bytesToString(UID).replace("0x", "");
        }
        return "1";
    }

}
