package com.santi.rickymortyapi.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.santi.rickymortyapi.R
import com.santi.rickymortyapi.databinding.CharacterDetailFragmentBinding
import com.santi.rickymortyapi.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: CharacterDetailFragmentBinding
    private val detailViewModel: MainActivityViewModel by activityViewModels()
    private var searchJob: Job? = null
    private var insertJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.setToFavorite.setOnClickListener(this)
    }

    private fun setupObservers() {
        detailViewModel.selectedFullCharacter.observe(viewLifecycleOwner, {
            if(it != null){
                Glide.with(binding.characterImage)
                    .load(it.image)
                    .circleCrop()
                    .into(binding.characterImage)

                binding.name.text = it.name

                binding.locationName.text = it.location?.name
                binding.species.text = it.species
                binding.status.text = it.status
                it.location?.url?.let { it1 -> startSearchJob(it1) }
            }

        })
        detailViewModel.location.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.numberOfResidents.text =
                    getString(R.string.number_of_residents, it.dimension)
            }
        })
    }

    private fun startSearchJob(locationUrl: String) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.getLocation(locationUrl)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.setToFavorite.id -> {
                Toast.makeText(requireActivity(), "setToFavorite", Toast.LENGTH_LONG).show()
                insertJob?.cancel()
                insertJob = viewLifecycleOwner.lifecycleScope.launch {
                    detailViewModel.insertFullCharacter()
                }
            }
        }
    }
}

