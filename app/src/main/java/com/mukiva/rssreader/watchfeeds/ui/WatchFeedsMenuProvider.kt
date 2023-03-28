package com.mukiva.rssreader.watchfeeds.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.mukiva.rssreader.R

typealias ProviderAction = () -> Boolean

class WatchFeedsMenuProvider(
    private var _aboutFeed : ProviderAction,
    private var _addFeed : ProviderAction,
    private var _deleteFeed : ProviderAction
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.feed_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId)
        {
            R.id.about_feed -> return _aboutFeed()
            R.id.add_feed -> return _addFeed()
            R.id.delete_feed -> return _deleteFeed()
        }
        return false
    }
}