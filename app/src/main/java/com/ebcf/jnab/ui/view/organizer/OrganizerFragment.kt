package com.ebcf.jnab.ui.view.organizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentOrganizerBinding

class OrganizerFragment : Fragment() {

    private var _binding: FragmentOrganizerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrganizerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = childFragmentManager.findFragmentById(R.id.nav_host_fragment_organizer)
            ?.findNavController() ?: return
        binding.bottomNavOrganizer.setupWithNavController(navController)

        AppBarConfiguration(
            setOf(R.id.submissionsListFragment, R.id.orgSymposiumsListFragment)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}