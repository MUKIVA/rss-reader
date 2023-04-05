package com.mukiva.rssreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.mukiva.rssreader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _appBarConfiguration: AppBarConfiguration
    private lateinit var _binding: ActivityMainBinding
    private lateinit var _navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupActionBar()

    }

    override fun onSupportNavigateUp(): Boolean {
        _navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    private fun setupActionBar() {
        setSupportActionBar(_binding.toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        _navController = navHostFragment.navController
        _appBarConfiguration = AppBarConfiguration(_navController.graph)
        setupActionBarWithNavController(_navController, _appBarConfiguration)
    }
}