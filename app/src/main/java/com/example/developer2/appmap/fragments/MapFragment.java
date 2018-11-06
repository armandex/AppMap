package com.example.developer2.appmap.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.developer2.appmap.R;
import com.example.developer2.appmap.helpers.AdminSQLiteOpenHelper;
import com.example.developer2.appmap.models.Marcadores;
import com.example.developer2.appmap.models.Registro;
import com.example.developer2.appmap.templates.TemplatePDF;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener {


    private View rootView;
    private MapView mapView;
    private GoogleMap gMap;

    private List<Address> addresses;
    private Geocoder geocoder;
    private MarkerOptions marker;

    private FloatingActionButton fab;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private List<Registro> registros;
    private List<Marcadores> marcadores;
    private Date fecha;

    private static final int UPDATE_INTERVAL = 30000;
    private static final int FASTEST_INTERVAL = 30000;
    private static final int DISTANCE = 0;
    private static int CONTEO = 0;
    private static int contador = 0;
    private static final String TAG = "LocationService";
    private TemplatePDF templatePDF;
    private String[] header = {"fecha", "latitud", "longitud"};
    private String shortText = "Hola";
    private String longText = "Nunca consideré el estudio como una obligación, sino como una oportunidad";
    private String titulo = null;
    private String snipet = null;
    static int count = 1;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);


        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        gMap = googleMap;
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        //Check location permission for sdk >= 23
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buttonMyPosition();
                marcadores = listarMarkadores();
                for (int i = 0; i < listarMarkadores().size(); i++) {
                    LatLng posicion = new LatLng(marcadores.get(i).getLatitud(), marcadores.get(i).getLongitud());
                    titulo = marcadores.get(i).getTitulo();
                    snipet = marcadores.get(i).getDetalle();
                    gMap.addMarker(new MarkerOptions().position(posicion).title(titulo).draggable(false).snippet(snipet).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
                }
                //gMap.getUiSettings().setMyLocationButtonEnabled(false);//esto bloquea el boton de ir a la posicion actual
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, DISTANCE, new LocationListener() {


                    @Override
                    public void onLocationChanged(Location location) {
                        fecha = calcularFecha(location);
                        Location myLocation = googleMap.getMyLocation();
                        LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        LatLng place = new LatLng(myLatLng.latitude, myLatLng.longitude);
                        marker = new MarkerOptions();
                        marker.position(place);
                        marker.title("Mi marcador");
                        marker.draggable(true);
                        marker.snippet("Hola soy Armando");

                        gMap.addMarker(marker);
                        //gMap.addMarker(new MarkerOptions().position(place).title("Hola desde OL").draggable(true));
                        //gMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                        //CameraPosition cameraPosition = new CameraPosition.Builder()
                        //.target(place)
                        //.zoom(18)               //limit 21
                        //.bearing(0)             //0 - 360°
                        //.tilt(90)               //limit 90
                        //.build();
                        //gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        //gMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);


                        if (location != null) {
                            alta(contador, myLatLng.latitude, myLatLng.longitude, fecha);
                            Toast.makeText(getContext(), "Fecha: " + fecha + "Lat: " + myLatLng.latitude + " - " + "Lon: " + myLatLng.longitude + "Contador: " + contador, Toast.LENGTH_SHORT).show();
                        }
                        contador++;
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

            }
            /*Location myLocation = googleMap.getMyLocation();
            LatLng myLatLng = new LatLng(myLocation.getLatitude(),
                    myLocation.getLongitude());
            Toast.makeText(getContext(), "Lat: " + myLatLng.latitude + " - " + "Lon: " + myLatLng.longitude, Toast.LENGTH_SHORT).show();*/

        } else {
            buttonMyPosition();
        }

    }

    public List<Marcadores> listarMarkadores() {
        List lista = new ArrayList();
        lista.add(new Marcadores(1, "title_1", "detalle_1", -12.083780124135965, -77.03701986686451));
        lista.add(new Marcadores(2, "title_2", "detalle_2", -12.084905916031966, -77.03641636983616));
        lista.add(new Marcadores(3, "title_3", "detalle_3", -12.084785268720983, -77.03559829608662));
        return lista;
    }

    public void alta(int idPersona, double latitud, double longitud, Date fecha) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "administracion", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();
        int idP = idPersona;
        double lat = latitud;
        double lon = longitud;
        Date fec = fecha;
        int idGrupo = 1;

        ContentValues registro = new ContentValues();

        registro.put("idPersona", idP);
        registro.put("latitud", lat);
        registro.put("longitud", lon);
        registro.put("fecha", String.valueOf(fec));
        registro.put("idGrupo", idGrupo);

        // los inserto en la base de datos
        bd.insert("usuario", null, registro);

        bd.close();
        Toast.makeText(getContext(), "Datos del usuario cargados", Toast.LENGTH_SHORT).show();

    }

    // Hacemos búsqueda de todos los usuarios
    public List<String[]> consulta() {
        registros = new ArrayList();
        ArrayList<String[]> rows = new ArrayList<>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "administracion", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select * from usuario", null);
        if (fila.getCount() > 0) {
            Toast.makeText(getContext(), "exito", Toast.LENGTH_SHORT).show();
            while (fila.moveToNext()){
                rows.add(new String[]{fila.getString(0),fila.getString(1), fila.getString(2)});
                //registros.add(new Registro(fila.getInt(0), fila.getDouble(1), fila.getDouble(2)));
                //Toast.makeText(getContext(), "dato1"+fila.getString(0)+"dato2: "+fila.getString(1)+"dato3:" +fila.getString(2), Toast.LENGTH_SHORT).show();
            }
            //if (fila.moveToFirst()) {
                //registros.add(new Registro(fila.getInt(0), fila.getDouble(1), fila.getDouble(2)));

            //}
        } else
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        bd.close();
        return rows;
    }

    public Date calcularFecha(Location location) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        fecha = new Date(location.getTime());
        return fecha;
    }

    public void grabarPosiciones() {
        ActivityCompat.requestPermissions((Activity) getContext(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


        templatePDF = new TemplatePDF(getContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Supervisor", "Posiciones", "Armando Aguinaga");
        templatePDF.addTitles("MyBizControl", "Clientes", fecha);
        templatePDF.createTable(header, (ArrayList<String[]>) consulta());
        templatePDF.addParagraph(shortText);
        templatePDF.addParagraph(longText);
        templatePDF.closeDocument();
        //
        pdfView();
    }

    public void pdfView() {
        templatePDF.viewPDF();
    }

    private ArrayList<String[]> getClients() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"1", "Armando", "Aguinaga"});
        rows.add(new String[]{"2", "Sofia", "Hernandez"});
        rows.add(new String[]{"3", "Naomi", "Alfaro"});
        rows.add(new String[]{"4", "Lorena", "Canedo"});

        return rows;
    }

    private ArrayList<String[]> getPosiciones(Double d1, Double d2) {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"1", d1.toString(), d2.toString()});

        return rows;
    }

    public void buttonMyPosition() {
        gMap.setMyLocationEnabled(true);
        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            //Con false se genera el efecto de arrastrar la camara hasta la posicion actual
            @Override
            public boolean onMyLocationButtonClick() {
                //Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void showInfoAlert() {
        new AlertDialog.Builder(getContext())
                .setTitle("GPS Signal")
                .setMessage("You don't have GPS signal enabled. Would you like to enable the GPS signal now?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("CANCEL", null).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //gMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location uodates

        }
    }


    private Boolean isGPSEnabled() {
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (gpsSignal == 0) {
                //El GPS no está activado
                //showInfoAlert();
                return false;
            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    public void onClick(View v) {

        /*if (!this.isGPSEnabled()) {
            showInfoAlert();
        }else{

        }*/
        //consulta();
        grabarPosiciones();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    //permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    TextUtils.equals(permissions[0], android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //gMap.setMyLocationEnabled(true);

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setSmallestDisplacement(DISTANCE);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {


                @Override
                public void onMyLocationChange(Location location) {
                    // TODO Auto-generated method stub
                    CONTEO = UPDATE_INTERVAL + contador;
                    contador = UPDATE_INTERVAL;
                    gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
                    Toast.makeText(getContext(), "C: " + CONTEO + " - " + "Lat: " + location.getLatitude() + "- " + "Lon: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
