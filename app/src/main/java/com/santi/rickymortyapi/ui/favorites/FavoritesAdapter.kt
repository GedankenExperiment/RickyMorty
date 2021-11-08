package com.santi.rickymortyapi.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santi.rickymortyapi.R
import com.santi.rickymortyapi.model.local.LocationAndCharacter

class FavoritesAdapter(private val dataSet: List<LocationAndCharacter>, private val listener: FavoritesAdapter.CharacterDbItemListener):RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    interface CharacterDbItemListener {
        fun onClickedDbCharacter(locationAndCharacter: LocationAndCharacter)
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView
        val imageView: ImageView
        val textView: TextView
        init {
            // Define click listener for the ViewHolder's View.
            cardView = view.findViewById(R.id.favorite_card_view)
            imageView = view.findViewById(R.id.favoriteImage)
            textView = view.findViewById(R.id.favoriteName)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.favorite_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        Glide.with(viewHolder.imageView)
            .load(dataSet[position].characterDb.url)
            .circleCrop()
            .into(viewHolder.imageView)
        viewHolder.textView.text = dataSet[position].characterDb.name
        viewHolder.cardView.setOnClickListener(View.OnClickListener {
            listener.onClickedDbCharacter(dataSet[position])
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}