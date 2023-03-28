package com.mukiva.rssreader.watchdetails.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.mukiva.rssreader.R

typealias WatchDetailsMenuAction = () -> Boolean

class WatchDetailsMenuProvider(
    private val _shareAction: WatchDetailsMenuAction
) : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.feed_item_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId)
        {
            R.id.share_option -> _shareAction()
        }
        return false
    }
}