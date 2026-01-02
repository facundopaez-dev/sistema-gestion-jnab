package com.ebcf.jnab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.databinding.ActivityMainBinding
import com.ebcf.jnab.domain.model.UserRole
import com.ebcf.jnab.ui.drawer.DrawerManager
import com.ebcf.jnab.ui.login.LoginViewModel
import com.ebcf.jnab.ui.session.SessionViewModel
import com.ebcf.jnab.ui.session.SessionViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerManager: DrawerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        drawerManager = DrawerManager(this, binding, navController)

        val factory = SessionViewModelFactory(FirebaseAuthRemoteDataSource.getInstance())
        val sessionViewModel = ViewModelProvider(this, factory)[SessionViewModel::class.java]
        val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Operacion a realizar cuando se presiona el boton "Cerrar sesion" en el Drawer
        drawerManager.setupDrawer {
            sessionViewModel.logout()
        }

        loginViewModel.loginSuccess.observe(this) { user ->
            navigateBasedOnRole(user.role)
        }

        sessionViewModel.logoutEvent.observe(this) {
            // setPopUpTo(R.id.nav_graph, true) significa:
            // - Volver hasta el inicio del grafo de navegacion (nav_graph)
            // - true indica eliminar tambien el destino inicial del grafo (startDestination),
            //   es decir, limpiar completamente el backstack antes de navegar a loginFragment.
            // Esto evita que el usuario pueda presionar "Atras" y regresar a pantallas anteriores
            // despues de cerrar sesion.
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()

            navController.navigate(R.id.loginFragment, null, navOptions)
        }
    }

    private fun navigateBasedOnRole(roleFromFirebase: String) {
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