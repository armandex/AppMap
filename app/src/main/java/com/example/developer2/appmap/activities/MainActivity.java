package com.example.developer2.appmap.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.developer2.appmap.R;
import com.example.developer2.appmap.fragments.MapFragment;
import com.example.developer2.appmap.fragments.WelcomeFragment;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            //currentFragment = new WelcomeFragment();
            //changeFragment(currentFragment);
            permisos();
        } else {

        }

    }

    public void permisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Habilitar permisos para la version de API 23 a mas
            int verificarPermisoEnableUbication = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int verificarPermisoEnableWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //Verificamos si el permiso no existe
            if (verificarPermisoEnableUbication != PackageManager.PERMISSION_GRANTED || verificarPermisoEnableWrite != PackageManager.PERMISSION_GRANTED) {
                //verifico si el usuario a rechazado el permiso anteriormente
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION /*shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)*/)) {
                    //Si a rechazado el permiso anteriormente muestro un mensaje
                    mostrarExplicacion();
                } else {
                    //De lo contrario carga la ventana para autorizar el permiso
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                }

            } else {
                //Si el permiso ya fue concedido abrimos en intent de contactos
                currentFragment = new WelcomeFragment();
                changeFragment(currentFragment);
            }

        } else {//Si la API es menor a 23 - abrimos en intent de contactos
            currentFragment = new WelcomeFragment();
            changeFragment(currentFragment);
            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarExplicacion() {
        new AlertDialog.Builder(this)
                .setTitle("Autorización")
                .setMessage("Necesito permiso para acceder a los contactos de tu dispositivo.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Mensaje acción cancelada
                        mensajeAccionCancelada();
                    }
                })
                .show();
    }

    public void mensajeAccionCancelada() {
        Toast.makeText(getApplicationContext(),
                "Haz rechazado la petición, por favor considere en aceptarla.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_welcome:
                currentFragment = new WelcomeFragment();
                break;
            case R.id.menu_map:
                currentFragment = new MapFragment();
                break;
        }
        changeFragment(currentFragment);
        return super.onOptionsItemSelected(item);
    }

    private void changeFragment(Fragment fragment) {
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //if (fragment.isAdded()){
            //transaction.hide(currentFragment).show(fragment);
            //Toast.makeText(MainActivity.this,"IF" ,Toast.LENGTH_SHORT ).show();;
        //}else{
            //transaction/*.hide(currentFragment)*/.add(R.id.fragment_container, fragment);
            //Toast.makeText(MainActivity.this,"ELSE" ,Toast.LENGTH_SHORT ).show();;
        //}
        //Toast.makeText(MainActivity.this,"AFUERA" ,Toast.LENGTH_SHORT ).show();;
        //transaction.commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();

    }

}
