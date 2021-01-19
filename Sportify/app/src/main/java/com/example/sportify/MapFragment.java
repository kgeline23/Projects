package com.example.sportify;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    FirebaseDatabase database;
    DatabaseReference mDbRef;
    List<Event> sportLocations;
    int LOCATION_REQUEST_CODE = 10001;
    Hashtable<String, Marker> markers = new Hashtable<String, Marker>(); // <EventId, Marker>
    Spinner filter;
    static String filterId;
    private static Query queryMarker;
    private ChildEventListener cel;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        database = FirebaseDatabase.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        /*

//        MainActivity.sportLocations = new ArrayList<Event>();

        //@Abraham, below you can find the lat long list that you'll have to change
        //
        sportLocations = new ArrayList<Event>();

        sportLocations.add(new Event(51.443365, 5.478255, "Location 1", "Basketball","Batman"));
        sportLocations.add(new Event(51.443258, 5.480636, "Location 2", "Soccer","Batman"));
        sportLocations.add(new Event(51.442214, 5.479649, "Location 3", "Basketball","Batman"));

        //---------------


        mDbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Event curevent = ds.getValue(Event.class);
                        curevent.id = ds.getKey();
                        sportLocations.add(curevent);
                        //Toast.makeText(getContext(),Double.toString(curevent.getLat()),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        //--------------

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        //Getting vales for filter
        filter = v.findViewById(R.id.spinnerFilter);
        database.getReference("categories").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> cat = new ArrayList<Category>();
                cat.add(new Category("All events", ""));
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds != null)
                    {
                        Category currentCat = ds.getValue(Category.class);
                        currentCat.setId(ds.getKey());
                        cat.add(currentCat);
                    }
                }
                ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getContext(), R.layout.support_simple_spinner_dropdown_item, cat);
                filter.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                filterId = ((Category)filter.getSelectedItem()).getId();
                loadMarkers(filterId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

/*
        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                loadMarkers(MainActivity.filterId);
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

 */

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));*/
/*
        for (int i = 0; i < sportLocations.size(); i++) {

            LatLng location = new LatLng(sportLocations.get(i).getLat(), sportLocations.get(i).getLon());
            BitmapDescriptor d = null;

            if (sportLocations.get(i).getEventCategory() == "Basketball") {
                d = BitmapDescriptorFactory.fromResource(R.drawable.basketball);
            } else if (sportLocations.get(i).getEventCategory() == "Soccer") {
                d = BitmapDescriptorFactory.fromResource(R.drawable.soccer);
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.anchor((float) 0.5, (float) 0.5);
            markerOptions.title(sportLocations.get(i).getEventName());

            if (d != null) {
                markerOptions.icon(d);
            }

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
        }*/

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
            zoomToUserLocation();
        } else {
            getPermission();
        }

        loadMarkers();
    }

    // Return the distance between two points in meters.
    private double distance(LatLng a, LatLng b) {
        final double R = 6378.137;
        double dLat = b.latitude * Math.PI / 180 - a.latitude * Math.PI / 180;
        double dLon = b.longitude * Math.PI / 180 - a.longitude * Math.PI / 180;
        double q = Math.pow(Math.sin(dLat/2), 2) + Math.cos(a.latitude * Math.PI / 180) * Math.cos(b.latitude * Math.PI / 180) * Math.pow(Math.sin(dLon/2), 2);
        double w = R * 2 * Math.atan2(Math.sqrt(q), Math.sqrt(1-q));
        return w * 1000;
    }

    public void loadMarkers() {
        loadMarkers("");
    }

    public void loadMarkers(String category) {
        database = FirebaseDatabase.getInstance();
        final Marker marker;

        if(queryMarker != null && cel != null) {
            queryMarker.removeEventListener(cel);
        }
        mMap.clear();
        markers.clear();

        if(category == null || category.equals(""))
            queryMarker = database.getReference("events");
        else
            queryMarker = database.getReference("events").orderByChild("eventCategory").equalTo(category);
        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot != null)
                {
                    //Toast.makeText(getContext(), "over here", Toast.LENGTH_SHORT).show();
                    Event curevent = snapshot.getValue(Event.class);
                    curevent.id = snapshot.getKey();
                    /*if(distance(new LatLng(curevent.getLat(), curevent.getLon()), ) < 40000)*/
                        notification(curevent);
                    LatLng location = new LatLng(curevent.getLat(), curevent.getLon());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.anchor((float) 0.5, (float) 0.5);
                    markerOptions.title(curevent.getEventName());

                    Marker m = mMap.addMarker(markerOptions);
                    markers.put(curevent.id, m);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DataSnapshot ds = snapshot;
                if(ds != null)
                {
                    Event curmarker = ds.getValue(Event.class);
                    curmarker.id = ds.getKey();
                    markers.get(curmarker.id).remove();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryMarker.addChildEventListener(cel);

    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
            }
        });
    }

    private void getPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Location");
                alert.setMessage("Please provide location access");
                alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }
                });
                alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                alert.create().show();
            }else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    public class LocationValues {

        private double lat;
        private double lon;
        private String eventName;
        private String eventCategory;

        public LocationValues(double lat, double lon, String eventName, String eventCategory) {
            this.lat = lat;
            this.lon = lon;
            this.eventName = eventName;
            this.eventCategory = eventCategory;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventCategory() {
            return eventCategory;
        }
    }

    public void notification(Event e){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel =
                    new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = (NotificationManager) getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"n").
                setContentTitle("Sportify - Event found").setSmallIcon(R.drawable.soccer).
                setAutoCancel(true).setContentText(e.getEventName()+" has been added");

        Intent i = new Intent(getContext(), SingleEventActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("data", e);
        i.putExtras(b);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(contentIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(999,builder.build());
    }
}