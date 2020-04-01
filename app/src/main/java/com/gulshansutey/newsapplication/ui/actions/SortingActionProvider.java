package com.gulshansutey.newsapplication.ui.actions;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.core.view.ActionProvider;

import com.gulshansutey.newsapplication.R;

public class SortingActionProvider extends ActionProvider {
    public static final String SORT_BY_LATEST = "Latest First";
    public static final String SORT_BY_OLDEST = "Older First";
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public SortingActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add(SORT_BY_LATEST);
        MenuItem ascending=subMenu.getItem(0);
        ascending.setIcon(R.drawable.ic_arrow_drop_up_black_18dp);
        subMenu.add(SORT_BY_OLDEST);
        MenuItem descending=subMenu.getItem(1);
        descending.setIcon(R.drawable.ic_arrow_drop_down_black_18dp);
        super.onPrepareSubMenu(subMenu);
    }

}
