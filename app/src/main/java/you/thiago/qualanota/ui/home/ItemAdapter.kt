package you.thiago.qualanota.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import you.thiago.qualanota.R
import you.thiago.qualanota.data.model.Item

class ItemAdapter(private val list: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupItem(list[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.labelTitle)
        private val owner = view.findViewById<TextView>(R.id.labelOwner)
        private val rating = view.findViewById<TextView>(R.id.labelRating)
        private val review = view.findViewById<TextView>(R.id.labelReview)

        fun setupItem(item: Item) {
            title.text = item.title
            owner.text = item.owner
            rating.text = item.rating.toString()
            review.text = item.review
        }
    }
}
