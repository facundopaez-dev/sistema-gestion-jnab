package com.ebcf.jnab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ui.AppBarConfiguration
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ebcf.jnab.databinding.FragmentUserBinding
import com.ebcf.jnab.R


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = childFragmentManager.findFragmentById(R.id.home_nav_host)?.findNavController() ?: return
        binding.navView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_symposiums, R.id.navigation_talks, R.id.navigation_agenda, R.id.navigation_speakers)
        )



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
