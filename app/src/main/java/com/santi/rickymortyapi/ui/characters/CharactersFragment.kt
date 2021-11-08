package com.santi.rickymortyapi.ui.characters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.santi.rickymortyapi.R
import com.santi.rickymortyapi.databinding.CharactersFragmentBinding
import com.santi.rickymortyapi.model.FullCharacter
import com.santi.rickymortyapi.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment(), RecyclerAdapter.CharacterItemListener,
    View.OnClickListener {
    private lateinit var binding: CharactersFragmentBinding
    private lateinit var recyclerAdapter: RecyclerAdapter
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private var searchJob: Job? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        recyclerAdapter = RecyclerAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        startSearchJob()
        binding.fab.setOnClickListener(this)
    }

    private fun startSearchJob() {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.getListData()
                .collectLatest {
                    recyclerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
        }
    }


    private fun setUpAdapter() {
        binding.recyclerview.apply {
            adapter = recyclerAdapter
        }
        recyclerAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                if (recyclerAdapter.snapshot().isEmpty()) {
                    binding.progressIndicator.isVisible = true
                }
                binding.errorTextView.isVisible = false

            } else {
                binding.progressIndicator.isVisible = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (recyclerAdapter.snapshot().isEmpty()) {
                        binding.errorTextView.isVisible = true
                        binding.errorTextView.text = it.error.localizedMessage
                    }
                }

            }
        }
    }

    override fun onClickedCharacter(fullCharacter: FullCharacter) {
        mainViewModel.canLike = true
        mainViewModel.selectedFullCharacter.value = fullCharacter
        findNavController().navigate(
            R.id.action_charactersFragment_to_characterDetailFragment
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fab.id -> {
                findNavController().navigate(R.id.action_charactersFragment_to_favoritesFragment)
            }
        }
    }
}