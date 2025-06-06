package com.example.spaceexplorer.presentation.ui.detail

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.spaceexplorer.R
import com.example.spaceexplorer.databinding.FragmentDetailBinding
import com.example.spaceexplorer.domain.model.Launch
import com.example.spaceexplorer.domain.model.Rocket
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

    /**
     * Observes the UI state from ViewModel and updates the UI accordingly.
     */
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
                        bindLaunchDetails(state.launch, state.rocket)
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

    /**
     * Binds launch details to the UI views.
     */
    private fun bindLaunchDetails(launch: com.example.spaceexplorer.domain.model.Launch, rocket: com.example.spaceexplorer.domain.model.Rocket) {
        binding.valueMissionName.text = launch.missionName
        binding.valueLaunchDate.text = launch.launchDateUtc.replace('T', ' ').take(19) // Format e.g. 2006-03-24 22:30:00
        binding.valueRocketName.text = rocket.name
        binding.valueRocketDesc.text = rocket.description

        binding.valueSuccess.text = when (launch.success) {
            true -> "Success"
            false -> "Failure"
            null -> "Unknown"
        }
        // Set clickable links, show "N/A" if null or empty
        toggleLinkGroup(binding.layoutWebcastGroup, binding.valueWebcast, launch.webcastUrl, "Link to Webcast")
        toggleLinkGroup(binding.layoutArticleGroup, binding.valueArticle, launch.articleUrl, "Link to Article")
        toggleLinkGroup(binding.layoutWikiGroup, binding.valueWiki, launch.wikipediaUrl, "Link to Wikipedia")

    }


    /**
     * Shows or hides the entire link group container (label + value + divider).
     * Sets up clickable link if URL is valid.
     */
    private fun toggleLinkGroup(
        container: ViewGroup,
        textView: TextView,
        url: String?,
        label: String
    ) {
        if (url.isNullOrBlank()) {
            container.visibility = View.GONE
        } else {
            container.visibility = View.VISIBLE
            textView.text = label
            textView.isClickable = true
            textView.setTextColor(resources.getColor(R.color.linkColor))
            textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            textView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
