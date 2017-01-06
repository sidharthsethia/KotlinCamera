package com.medcords.mhcpanel.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends ActionBarActivity {

    private ImageButton addUserButton, searchUserButton;
    private Drawer result;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MedCords");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        setUpNavigationDrawer();

        addUserButton = (ImageButton) findViewById(R.id.add_user_button);
        searchUserButton = (ImageButton) findViewById(R.id.search_user_button);

        addUserButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addUserButton.setImageResource(R.mipmap.add_user_green);
                searchUserButton.setImageResource(R.mipmap.search_user_grey);
                return false;
            }
        });

        searchUserButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addUserButton.setImageResource(R.mipmap.add_user_grey);
                searchUserButton.setImageResource(R.mipmap.search_user_green);
                return false;
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserSignupActivity.class);
                startActivity(i);
            }
        });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchUserButton.setImageResource(R.mipmap.search_user_grey);

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_search_user);

                final RelativeLayout mOTPLayout = (RelativeLayout) dialog.findViewById(R.id.otp_layout);
                final RelativeLayout mSearchLayout = (RelativeLayout) dialog.findViewById(R.id.search_layout);
                final Button mSearchButton = (Button) dialog.findViewById(R.id.search_button);
                final Button mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);
                final Button mOTPSubmitButton = (Button) dialog.findViewById(R.id.submit_button);
                final EditText mOTPEditText = (EditText) dialog.findViewById(R.id.input_otp);
                final EditText mPhoneEditText = (EditText) dialog.findViewById(R.id.input_mobile);
                final ProgressBar mProgressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
                final TextView mOTPTitleTextView = (TextView) dialog.findViewById(R.id.otp_text_view);
                final TextView mResendOTPTextView = (TextView) dialog.findViewById(R.id.resend_otp_text_view);
                final TextView mCancelTextView = (TextView) dialog.findViewById(R.id.cancel_textview);
                final TextInputLayout inputLayoutPhone = (TextInputLayout) dialog.findViewById(R.id.input_layout_mobile);
                final TextInputLayout inputLayoutOTP = (TextInputLayout) dialog.findViewById(R.id.input_layout_otp);

                mOTPLayout.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);



                mResendOTPTextView.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                mOTPTitleTextView.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                mOTPEditText.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                mOTPEditText.setHintTextColor(getResources().getColor(R.color.colorTextSecondary));
                mPhoneEditText.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                mCancelTextView.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                mPhoneEditText.setHintTextColor(getResources().getColor(R.color.colorTextSecondary));

                mCancelTextView.setVisibility(View.GONE);

                mCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                mSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!Utility.validatePhone(mPhoneEditText,inputLayoutPhone,MainActivity.this,MainActivity.this)) {
                            return;
                        }

                        String otp_title = String.format(getResources().getString(R.string.otp_title), mPhoneEditText.getText().toString().trim());
                        mOTPTitleTextView.setText(Html.fromHtml(otp_title));

                        mSearchLayout.setVisibility(View.GONE);
                        mOTPLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mCancelTextView.setVisibility(View.VISIBLE);

                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                    }
                });

                mCancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Utility.validateOTP(mOTPEditText,inputLayoutOTP,MainActivity.this,MainActivity.this)) {
                            return;
                        }

                        dialog.dismiss();
                        //Intent i = new Intent(MainActivity.this, MainActivity.class);
                        //startActivity(i);
                        //finish();

                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( result.isDrawerOpen() )
            result.closeDrawer();

        addUserButton.setImageResource(R.mipmap.add_user_grey);
        searchUserButton.setImageResource(R.mipmap.search_user_grey);
    }

    private void setUpNavigationDrawer(){

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        new ProfileDrawerItem().withName("Nikhil Baheti").withEmail("9231214141")
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home").withTag("Home"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings").withTag("Settings"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Log Out").withTag("Log Out")
                )
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                switch (position){
                    case 0:
                        break;
                    case 2:
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }

                result.closeDrawer();

                return false;
            }
        });
        result.getHeader().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Profile clicked","true");
            }
        });
        result.setSelectionAtPosition(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
