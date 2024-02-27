//TODO(Migrate to compose)
//package com.mukiva.rssreader.watchfeeds.ui
//
//import android.view.Menu
//import android.view.MenuInflater
//import android.view.MenuItem
//import androidx.core.view.MenuProvider
//import com.mukiva.rssreader.R
//
//class WatchFeedsMenuProvider(
//    private val _actions: WatchFeedsMenuProviderActions
//) : MenuProvider {
//
//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.feed_menu, menu)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        when (menuItem.itemId)
//        {
//            R.id.about_feed -> _actions.aboutFeed()
//            R.id.add_feed -> _actions.addFeed()
//            R.id.delete_feed -> _actions.deleteFeed()
//            else -> return false
//        }
//        return true
//    }
//}