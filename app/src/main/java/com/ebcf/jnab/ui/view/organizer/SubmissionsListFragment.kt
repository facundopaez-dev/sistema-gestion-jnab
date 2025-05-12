package com.ebcf.jnab.ui.view.organizer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentSubmissionsListBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.adapter.organizer.SubmissionAdapter
import com.ebcf.jnab.ui.viewmodel.organizer.SubmissionsListViewModel

class SubmissionsListFragment : Fragment() {

    private var _binding: FragmentSubmissionsListBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionsListBinding.inflate(inflater, container, false)

        // Crea el ViewModel sin Factory
        val submissionListViewModel = ViewModelProvider(this)[SubmissionsListViewModel::class.java]

        // Crea una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()

        // Configura RecyclerView
        val recyclerView = binding.recyclerViewSubmissions
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observa ViewModel para la lista de simposios
        submissionListViewModel.submissions.observe(viewLifecycleOwner) { submissions ->
            recyclerView.adapter = SubmissionAdapter(submissions, formatDateUseCase)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}