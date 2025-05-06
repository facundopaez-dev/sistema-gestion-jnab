package com.ebcf.jnab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ebcf.jnab.data.model.UserRole
import com.ebcf.jnab.databinding.ActivityMainBinding
import com.ebcf.jnab.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el layout principal usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Observa el resultado del login desde el ViewModel
        viewModel.loginResult.observe(this, Observer { result ->
            if (result.success) {
                // Si el inicio de sesion es exitoso, se navega segun el rol del usuario
                navigateBasedOnRole(result.role)
            }
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
                Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_LONG).show()
            }
        }
    }

}
