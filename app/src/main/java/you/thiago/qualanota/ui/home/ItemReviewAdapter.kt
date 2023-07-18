package you.thiago.qualanota.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import you.thiago.qualanota.R
import you.thiago.qualanota.data.model.ItemReview

class ItemReviewAdapter(
    private val list: List<ItemReview>,
    private val listener: AdapterItemReviewClickListener,
) : RecyclerView.Adapter<ItemReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupItem(list[position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val owner = view.findViewById<TextView>(R.id.owner)
        private val rating = view.findViewById<TextView>(R.id.rating)
        private val review = view.findViewById<TextView>(R.id.review)

        fun setupItem(itemReview: ItemReview) {
            title.text = itemReview.title
            owner.text = itemReview.owner
            rating.text = itemReview.rating.toString()
            review.text = itemReview.review

            view.setOnClickListener {
                listener.onAdapterClick(itemReview)
            }
        }
    }

    fun interface AdapterItemReviewClickListener {
        fun onAdapterClick(itemReview: ItemReview)
    }
}
