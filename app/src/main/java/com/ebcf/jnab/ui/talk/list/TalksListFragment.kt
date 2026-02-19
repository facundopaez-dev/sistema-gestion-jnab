package com.ebcf.jnab.ui.talk.list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.ui.talk.TalksFilterBottomSheet
import com.ebcf.jnab.ui.theme.SistemaGestionJnabTheme

class TalksListFragment : Fragment() {

    private lateinit var talksListViewModel: TalksListViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        talksListViewModel = ViewModelProvider(
            requireActivity(),
            TalksListViewModel.TalksListViewModelFactory(requireContext())
        )[TalksListViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                SistemaGestionJnabTheme {
                    TalksListScreen(
                        viewModel = talksListViewModel,
                        onFilterClick = {
                            val filterBottomSheet = TalksFilterBottomSheet()
                            filterBottomSheet.show(parentFragmentManager, TalksFilterBottomSheet.TAG)
                        }
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        if (isRemoving) {
            talksListViewModel.clearFilters()
        }
    }
}