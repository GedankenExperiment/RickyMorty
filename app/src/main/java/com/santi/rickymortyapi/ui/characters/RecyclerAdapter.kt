package com.santi.rickymortyapi.ui.characters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santi.rickymortyapi.databinding.RecyclerRowBinding
import com.santi.rickymortyapi.model.FullCharacter


class RecyclerAdapter(private val listener: CharacterItemListener) :
    PagingDataAdapter<FullCharacter, RecyclerAdapter.MyViewHolder>(
        DiffUtilCallBack()
    ) {
    interface CharacterItemListener {
        fun onClickedCharacter(fullCharacter: FullCharacter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecyclerRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class MyViewHolder(
        private val binding: RecyclerRowBinding,
        private val listener: CharacterItemListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(characterData: FullCharacter) {
            binding.characterName.text = characterData.name
            binding.characterSpecies.text = characterData.species
            binding.cardView.setOnLongClickListener(View.OnLongClickListener {
                Log.d("COSAS ID", characterData.id.toString())
                return@OnLongClickListener false
            })
            binding.cardView.setOnClickListener {
                if ((characterData.id != null && characterData.image != null && characterData.name != null
                            && characterData.location?.url != null) && characterData.species != null && characterData.status != null
                ) {
                    listener.onClickedCharacter(characterData)
                }
            }
            Glide.with(binding.characterImage)
                .load(characterData.image)
                .circleCrop()
                .into(binding.characterImage)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<FullCharacter>() {
        override fun areItemsTheSame(oldItem: FullCharacter, newItem: FullCharacter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FullCharacter, newItem: FullCharacter): Boolean {
            return oldItem.id == newItem.id
        }

    }
}