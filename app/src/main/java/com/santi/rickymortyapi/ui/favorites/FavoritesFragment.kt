package com.santi.rickymortyapi.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.santi.rickymortyapi.R
import com.santi.rickymortyapi.databinding.FavoritesFragmentBinding
import com.santi.rickymortyapi.model.local.LocationAndCharacter
import com.santi.rickymortyapi.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Job


class FavoritesFragment: Fragment(), FavoritesAdapter.CharacterDbItemListener {
    private lateinit var binding: FavoritesFragmentBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        viewModel.favorites.observe(viewLifecycleOwner, {
            if(it!=null){
                binding.recyclerview.adapter = FavoritesAdapter(it, this)
            }
        })
        getFavorites()
    }
    private fun getFavorites(){
        viewModel.getFavorites()
    }

    override fun onClickedDbCharacter(locationAndCharacter: LocationAndCharacter) {
        viewModel.fromDBToSelected(locationAndCharacter)
        findNavController().navigate(
            R.id.action_favoritesFragment_to_characterDetailFragment
        )
    }
}