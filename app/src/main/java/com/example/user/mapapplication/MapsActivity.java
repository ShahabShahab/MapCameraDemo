package com.example.user.mapapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.user.mapapplication.Model.ClinicResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener, View.OnClickListener {

    ProgressBar progressBar;
    Button button;
    ViewPager mViewPager;

    private static final String TAG = MapsActivity.class.getName();
    private static final LatLng NEAR_SYDNEY = new LatLng(-33.872313, 151.211270);

    /**
     *
     * The amount by which to scroll the Camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */

    String southWest, northEast;

    private static final int SCROLL_BY_PX = 100;

    public static final CameraPosition BONDI =
            new CameraPosition.Builder().target(new LatLng(-33.891614, 151.276417))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(50)
                    .build();

    public static final CameraPosition Enghelab =
            new CameraPosition.Builder().target(new LatLng(35.700987, 51.391232))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();

    private GoogleMap mMap;

    private CompoundButton mAnimateToggle;
    private CompoundButton mCustomDurationToggle;
    private SeekBar mCustomDurationBar;
    private PolylineOptions currPolylineOptions;
    private boolean isCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        System.out.println("onCreate is called!");
        mAnimateToggle = (CompoundButton) findViewById(R.id.animate);
        mCustomDurationToggle = (CompoundButton) findViewById(R.id.duration_toggle);
        mCustomDurationBar = (SeekBar) findViewById(R.id.duration_bar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        button = (Button) findViewById(R.id.btnSearch);
        mViewPager = ( ViewPager ) findViewById(R.id.view_pager_clinics);
        mViewPager.setOnClickListener(this);
        //updateEnabledState();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    protected void onResume() {
        super.onResume();
        updateEnabledState();
    }
//
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

       mMap.setOnCameraIdleListener(this);
       //mMap.setOnCameraMoveStartedListener(this);
       mMap.setOnCameraMoveListener(this);
       //mMap.setOnCameraMoveCanceledListener(this);

        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Show Sydney
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
       // mMap.setOnMapClickListener(this);

        int height = 120;
        int width = 85;
        BitmapDrawable bitmapdrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.m);
        Bitmap b=bitmapdrawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        mMap.addMarker(new MarkerOptions().position(NEAR_SYDNEY).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        .position(NEAR_SYDNEY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEAR_SYDNEY, 10));

    }
//    /**
//     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
//     * all entry points that call methods on the Google Maps API.
//     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "Map not ready!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * Called when the Go To Bondi button is clicked.
     */
    public void onGoToBondi(View view) {
        if (!checkReady()) {
            return;
        }
        int height = 120;
        int width = 85;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.m2);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap smallerBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallerBitmap))
        .position(BONDI.target));
        changeCamera(CameraUpdateFactory.newCameraPosition(BONDI), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(MapsActivity.this, "Animation on Bondi is complete!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(MapsActivity.this, "Animation on Bondi is stopped!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called when the Animate To Sydney button is clicked.
     */
    public void onGoToEnghelab(View view) {
        if (!checkReady()) {
            return;
        }
        int height = 120;
        int width = 85;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.map);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap smallerBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallerBitmap))
        .position(Enghelab.target));
        changeCamera(CameraUpdateFactory.newCameraPosition(Enghelab), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(getBaseContext(), "Animation to Sydney complete", Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Animation to Sydney canceled", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * Called when the stop button is clicked.
     */
    public void onStopAnimation(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.stopAnimation();
    }

    /**
     * Called when the zoom in button (the one with the +) is clicked.
     */
    public void onZoomIn(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * Called when the zoom out button (the one with the -) is clicked.
     *
     */
    public void onZoomOut(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.zoomOut());
    }

    /**
     * Called when the tilt more button (the one with the /) is clicked.
     */
    public void onTiltMore(View view) {
        if (!checkReady()) {
            return;
        }
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        System.out.println("The camera position +" + mMap.getCameraPosition());
        float currentTilt = currentCameraPosition.tilt;
        float newTilt = currentTilt + 10;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Called when the tilt less button (the one with the \) is clicked.
     *
     */

    public void onTiltLess(View view) {
        if (!checkReady()) {
            return;
        }
        CameraPosition currentCameraPosition = mMap.getCameraPosition();

        float currentTilt = currentCameraPosition.tilt;

        float newTilt = currentTilt - 10;
        newTilt = (newTilt > 0) ? newTilt : 0;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Called when the left arrow button is clicked. This causes the camera to move to the left
     */

    public void onScrollLeft(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0));
    }

    /**
     * Called when the right arrow button is clicked. This causes the camera to move to the right.
     */
    public void onScrollRight(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0));
    }

    /**
     * Called when the up arrow button is clicked. The causes the camera to move up.
     */
    public void onScrollUp(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX));
    }

    /**
     * Called when the down arrow button is clicked. This causes the camera to move down.
     */
    public void onScrollDown(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX));
    }

    /**
     * Called when the animate button is toggled
     */
    public void onToggleAnimate(View view) {
        updateEnabledState();
    }

    /**
     * Called when the custom duration checkbox is toggled
     */
    public void onToggleCustomDuration(View view) {
        updateEnabledState();
    }

    /**
     * Update the enabled state of the custom duration controls.
     */
    private void updateEnabledState() {
        mCustomDurationToggle.setEnabled(mAnimateToggle.isChecked());
        mCustomDurationBar
                .setEnabled(mAnimateToggle.isChecked() && mCustomDurationToggle.isChecked());
    }

    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     *
     */
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        if (mAnimateToggle.isChecked()) {
            if (mCustomDurationToggle.isChecked()) {
                int duration = mCustomDurationBar.getProgress();
                // The duration must be strictly positive so we make it at least 1.
                mMap.animateCamera(update, Math.max(duration, 1), callback);
                System.out.println("update is called!");
            } else {
                mMap.animateCamera(update, callback);
            }
        } else {
            mMap.moveCamera(update);
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (!isCanceled) {
            mMap.clear();
        }
        String reasonText = "UNKNOWN_REASON";
        currPolylineOptions = new PolylineOptions().width(5);
        switch (reason) {
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                currPolylineOptions.color(Color.BLUE);
                reasonText = "GESTURE";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                currPolylineOptions.color(Color.RED);
                reasonText = "API_ANIMATION";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                currPolylineOptions.color(Color.GREEN);
                reasonText = "DEVELOPER_ANIMATION";
                break;
        }
        Log.i(TAG, "onCameraMoveStarted(" + reasonText + ")");
        addCameraTargetToPath();
    }

    @Override
    public void onCameraMove() {
        // When the camera is moving, add its target to the current path we'll draw on the map.
//        if (currPolylineOptions != null) {
//            addCameraTargetToPath();
//        }
        button.setVisibility(View.VISIBLE);
        button.setText("Search this area!");
        Log.i(TAG, "onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        // When the camera stops moving, add its target to the current path, and draw it on the map.
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            mMap.addPolyline(currPolylineOptions);
        }
        isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        Log.i(TAG, "onCameraMoveCancelled");
    }

    @Override
    public void onCameraIdle() {
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            mMap.addPolyline(currPolylineOptions);
        }
        button.setVisibility(View.VISIBLE);
        currPolylineOptions = null;
        isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        Log.i(TAG, "onCameraIdle");
        System.out.println("onCameraIdle");
        LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        System.out.println(latLngBounds);
        String temp =latLngBounds.southwest + "";
        southWest = temp.substring(10,temp.length() - 1);
        System.out.println(southWest);
        temp = latLngBounds.northeast + "";
        northEast = temp.substring(10,temp.length() - 1);
        System.out.println(northEast);
    }
    private void addCameraTargetToPath() {
        LatLng target = mMap.getCameraPosition().target;
        System.out.println("target on cameraIdle" + target);
        currPolylineOptions.add(target);
    }

    public void onSearchArea(View view){
        progressBar.setVisibility(View.VISIBLE);
        button.setText("");
        startGetClinicService();
    }

    private void startGetClinicService() {
        RestManager restManager = new RestManager();
        Call<ClinicResponse> call = restManager.getRetrofitService().getClinicInRegion(southWest, northEast);
        call.enqueue(new Callback<ClinicResponse>() {
            @Override
            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
                if ( response.isSuccessful() ){
                    progressBar.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    if ( response.body().getClinics().size() != 0 ){
                        for (int i = 0; i < response.body().getClinics().size(); i++) {

                            mMap.addMarker(new MarkerOptions().
                                    position(new LatLng(response.body().getClinics().get(i).getLatitude(),
                                            response.body().getClinics().get(i).getLongtitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(createBitMap(R.drawable.m2))));
                        }
                    }else{
                        Toast.makeText(MapsActivity.this, "No Clinic in this region!", Toast.LENGTH_SHORT).show();
                    }

                    if (response.body().getClinics().size() == 0) {
                        mViewPager.setVisibility(View.INVISIBLE);
                        mMap.clear();
                        Toast.makeText(MapsActivity.this,  "موردی یافت نشد!", Toast.LENGTH_SHORT).show();
                    } else {
                        mViewPager.setVisibility(View.VISIBLE);
                    }
                    MapsPagerAdapter mapsPagerAdapter = new MapsPagerAdapter(getSupportFragmentManager(), MapsActivity.this, response.body().getClinics());
                    mViewPager.setAdapter(mapsPagerAdapter);
                    mViewPager.setClipChildren(false);
                    mViewPager.setClipToPadding(false);
                    mViewPager.setOffscreenPageLimit(2);
                    mViewPager.setPageMargin(10);
                }
            }

            @Override
            public void onFailure(Call<ClinicResponse> call, Throwable t) {

            }
        });
    }
    private Bitmap createBitMap( int drawableRes ){
        int height = 120;
        int width = 85;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(drawableRes);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap smallerBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        return smallerBitmap;
    }

    @Override
    public void onClick(View v) {

    }
}
