package com.example.spaceexplorer.presentation.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceexplorer.R
import com.example.spaceexplorer.databinding.ItemLaunchBinding
import com.example.spaceexplorer.domain.model.Launch

/**
 * Adapter for displaying SpaceX launches in RecyclerView.
 *
 * Binds [Launch] data to the item view using ViewBinding.
 */
class LaunchAdapter(
    private val launches: List<Launch>,
    private val onItemClick: (Launch) -> Unit
) : RecyclerView.Adapter<LaunchAdapter.LaunchViewHolder>() {

    inner class LaunchViewHolder(private val binding: ItemLaunchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(launch: Launch) {

            binding.tvMissionName.text = launch.missionName

            // Format launch date to user-friendly format if needed
            binding.tvLaunchDate.text = launch.launchDateUtc.take(10) // ISO date yyyy-MM-dd

            binding.tvRocketName.text = launch.rocketName


            // Success/Failure text + color
            when (launch.success) {
                true -> {
                    binding.tvSuccessStatus.text = "Success"
                    binding.tvSuccessStatus.setTextColor(
                        binding.root.context.getColor(R.color.spacex_green)
                    )
                }
                false -> {
                    binding.tvSuccessStatus.text = "Failure"
                    binding.tvSuccessStatus.setTextColor(
                        binding.root.context.getColor(R.color.spacex_red)
                    )
                }
                null -> {
                    binding.tvSuccessStatus.text = "Unknown"
                    binding.tvSuccessStatus.setTextColor(
                        binding.root.context.getColor(R.color.spacex_light_gray)
                    )
                }
            }

            binding.root.setOnClickListener {
                onItemClick(launch)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val binding = ItemLaunchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaunchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bind(launches[position])
    }

    override fun getItemCount(): Int = launches.size
}
