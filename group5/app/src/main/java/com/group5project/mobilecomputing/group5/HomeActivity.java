package com.group5project.mobilecomputing.group5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.group5project.mobilecomputing.group5.svm_train.GetAccuracy;

public class HomeActivity extends AppCompatActivity implements GetAccuracy {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static final String TAG = Tab1.class.getCanonicalName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AppCompatActivity currentActivity;
    private String accuracy = "";
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 8503;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public void setAccuracyString(String result){
        this.accuracy = result;
    }
    public String getAccuracyString(){
        return accuracy;
    }

    //permissions code from stackoverflow
    private void storagePermissionCheck() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else
                    HomeActivity.this.finish();

                return;
            }
        }

    }


    @Override
    public void getAccuracy(final String result) {
        final String accuracy = result;
        setAccuracyString(result);
        Log.d(TAG, "Accuracy of five-fold crossvalidation is: "+result);
        currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(currentActivity, "Accuracy is "+accuracy, Toast.LENGTH_LONG).show();            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storagePermissionCheck();
        setContentView(R.layout.activity_home);
        currentActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        int id = android.os.Process.myPid();
        Log.d(TAG, "in onResume method " + Integer.toString(id));

    }
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */

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
            switch(position){
                case 0:
                    Tab1 tab1obj = new Tab1();
                    return tab1obj;
                case 1:
                    Tab2 tab2obj = new Tab2();
                    return tab2obj;
                case 2:
                    Tab3 tab3obj = new Tab3();
                    return tab3obj;
                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
