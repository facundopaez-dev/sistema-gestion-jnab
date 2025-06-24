package com.ebcf.jnab.ui.drawer

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class DrawerManager(
    private val activity: AppCompatActivity,
    private val binding: ActivityMainBinding,
    private val navController: NavController
) {
    private lateinit var drawerToggle: ActionBarDrawerToggle

    fun setupDrawer(
        onLogout: () -> Unit
    ) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Drawer toggle (hamburguesa)
        drawerToggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Establece el color blanco para el icono en forma de hamburguesa de la toolbar del Drawer
        drawerToggle.drawerArrowDrawable.color =
            ContextCompat.getColor(binding.toolbar.context, android.R.color.white)

        // Listener del Drawer
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    drawerLayout.closeDrawers()
                    onLogout()
                    true
                }

                else -> false
            }
        }

        // Muestra u oculta la toolbar y el drawer segun el fragmento actual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    activity.supportActionBar?.hide()
                    setDrawerEnabled(drawerLayout, false)
                }

                else -> {
                    activity.supportActionBar?.show()
                    setDrawerEnabled(drawerLayout, true)
                }
            }
        }
    }

    private fun setDrawerEnabled(drawerLayout: DrawerLayout, enabled: Boolean) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            drawerToggle.isDrawerIndicatorEnabled = true
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            drawerToggle.isDrawerIndicatorEnabled = false
        }
        drawerToggle.syncState()
    }
}