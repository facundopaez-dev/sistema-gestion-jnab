package com.ebcf.jnab

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.ebcf.jnab.data.model.UserRole
import com.ebcf.jnab.databinding.ActivityMainBinding
import com.ebcf.jnab.ui.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el layout principal usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Observa el resultado del login desde el ViewModel
        viewModel.loginSuccess.observe(this, Observer { role ->
            navigateBasedOnRole(role)
        })

    }

    private fun navigateBasedOnRole(roleFromFirebase: String) {
        val navController = findNavController(R.id.nav_host_fragment)
        val role = UserRole.fromValue(roleFromFirebase)

        when (role) {
            UserRole.ASSISTANT, UserRole.SPEAKER -> {
                navController.navigate(R.id.action_loginFragment_to_userFragment)
            }

            UserRole.ORGANIZER -> {
                navController.navigate(R.id.action_loginFragment_to_organizerFragment)
            }

            else -> {
                Snackbar.make(binding.root, "Rol no reconocido", Snackbar.LENGTH_LONG).show()
            }
        }
    }



}
