package eu.robomaniac.heurigenkalender;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.robomaniac.heurigenkalender.models.Heuriger;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.


            switch (position) {
                case 0:
                    return new MapFragment();
                case 1:
                    return new ListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.section_title_map);
                case 1:
                    return getResources().getString(R.string.section_title_list);
            }
            return null;
        }
    }

    /**
     * A list fragment containing a simple view.
     */
    public static class ListFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 final Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.listview);

            final ArrayList<Heuriger> list = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Location loc = new Location("");
                loc.setLongitude(Math.random());
                loc.setLongitude(Math.random());
                list.add(new Heuriger("Weinviertler Heuriger", "Mitterweg 9, 2020 Hollabrunn", Math.random() * 5, loc));
            }

            final StableArrayAdapter adapter = new StableArrayAdapter(MainActivity.context,
                    R.layout.list_entry, list);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(MainActivity.context,
                            "Click ListItem Number " + position, Toast.LENGTH_LONG)
                            .show();
                }
            });

            return rootView;
        }

        private class StableArrayAdapter extends ArrayAdapter<Heuriger> {

            HashMap<Heuriger, Integer> mIdMap = new HashMap<>();
            Location loc;

            public StableArrayAdapter(Context context, int textViewResourceId,
                                      List<Heuriger> objects) {
                super(context, textViewResourceId, objects);
                for (int i = 0; i < objects.size(); ++i) {
                    mIdMap.put(objects.get(i), i);
                }

                loc = new Location("");
                loc.setLongitude(Math.random());
                loc.setLongitude(Math.random());
            }

            @Override
            public long getItemId(int position) {
                Heuriger item = getItem(position);
                return mIdMap.get(item);
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.list_entry, parent, false);
                TextView nameView = (TextView) rowView.findViewById(R.id.firstLine);
                TextView addressView = (TextView) rowView.findViewById(R.id.secondLine);
                TextView ratingsView = (TextView) rowView.findViewById(R.id.stars);
                TextView distanceView = (TextView) rowView.findViewById(R.id.distance_te);

                Heuriger heuriger = getItem(position);
                double distance = heuriger.getLocation().distanceTo(loc);
                String distance_s = String.format("%.2f km", distance);

                if (distance > 100) {
                    distance_s = "> 100 km";
                }

                nameView.setText(heuriger.getName());
                addressView.setText(heuriger.getAddress());
                ratingsView.setText(String.format("%.1f", heuriger.getRating()));
                distanceView.setText(distance_s);

                return rowView;
            }

        }
    }

    public static class MapFragment extends Fragment implements OnMapReadyCallback {

        MapView mapView;
        GoogleMap map;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_map, container, false);

            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) v.findViewById(R.id.mapview);
            mapView.onCreate(savedInstanceState);

            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(this.getActivity());

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
            map.animateCamera(cameraUpdate);

            return v;
        }

        @Override
        public void onMapReady(GoogleMap map) {
            // Add a marker in Sydney, Australia, and move the camera.
            LatLng sydney = new LatLng(-34, 151);
            map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        @Override
        public void onResume() {
            mapView.onResume();
            super.onResume();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }

    }
}
