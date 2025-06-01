package com.example.spaceexplorer.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.spaceexplorer.databinding.FragmentDetailBinding
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.presentation.model.LaunchDetailUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment showing detailed info about a single launch.
 * Gets launchId from SafeArgs and uses DetailViewModel.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pass launchId to ViewModel's savedStateHandle
        viewModel.fetchLaunchDetail(args.launchId)

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LaunchDetailUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                    }
                    is LaunchDetailUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.contentGroup.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        bindLaunchDetails(state.launch)
                    }
                    is LaunchDetailUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }

    private fun bindLaunchDetails(launch: Launch) {
        Log.i("DetailFragment", "bindLaunchDetails: $launch")
        binding.tvMissionName.text = launch.missionName
        binding.tvLaunchDate.text = launch.launchDateUtc.take(10) // format as yyyy-MM-dd
        binding.tvRocketName.text = launch.rocketName
        binding.tvSuccessStatus.text = when (launch.success) {
            true -> "Success"
            false -> "Failure"
            else -> "Unknown"
        }
        // Optionally load video or article links here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
