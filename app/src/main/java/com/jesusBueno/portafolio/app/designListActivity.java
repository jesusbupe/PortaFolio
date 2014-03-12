package com.jesusBueno.portafolio.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class designListActivity extends FragmentActivity
        implements designListFragment.Callbacks {


    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_list);

        if (findViewById(R.id.design_detail_container) != null) {

            mTwoPane = true;

            ((designListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.design_list))
                    .setActivateOnItemClick(true);
        }

    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {

            Bundle arguments = new Bundle();
            arguments.putString(designDetailFragment.ARG_ITEM_ID, id);
            designDetailFragment fragment = new designDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.design_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.f
            Intent detailIntent = new Intent(this, designDetailActivity.class);
            detailIntent.putExtra(designDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
