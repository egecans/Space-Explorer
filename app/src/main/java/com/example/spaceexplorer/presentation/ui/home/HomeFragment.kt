package com.example.spaceexplorer.presentation.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spaceexplorer.databinding.FragmentHomeBinding
import com.example.spaceexplorer.presentation.model.LaunchesUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment displaying list of SpaceX launches.
 *
 * Observes [HomeViewModel] to update UI reactively.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: LaunchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = LaunchAdapter(emptyList()) { _ ->
            // TODO: Handle item click, navigate to detail screen with launch.id
        }
        binding.rvLaunches.apply {
            adapter = this@HomeFragment.adapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LaunchesUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvLaunches.visibility = View.GONE
                        binding.tvErrorMessage.visibility = View.GONE
                    }
                    is LaunchesUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvLaunches.visibility = View.VISIBLE
                        binding.tvErrorMessage.visibility = View.GONE
                        adapter = LaunchAdapter(state.launches) { launch ->
                            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(launch.id)
                            Log.i("HomeFragment", "Navigating to detail with launch ID: ${launch.id}")
                            findNavController().navigate(action)
                        }
                        binding.rvLaunches.adapter = adapter
                    }
                    is LaunchesUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvLaunches.visibility = View.GONE
                        binding.tvErrorMessage.visibility = View.VISIBLE
                        binding.tvErrorMessage.text = state.message
                    }
                    is LaunchesUiState.NoInternetConnection -> {
                        showNoInternetDialog(state.message)
                    }
                }
            }
        }
    }

    private fun showNoInternetDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Connection Error")
            .setMessage(message)
            .setNegativeButton("Cancel") { dialog,  _ ->
               dialog.dismiss()
               activity?.finish()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
