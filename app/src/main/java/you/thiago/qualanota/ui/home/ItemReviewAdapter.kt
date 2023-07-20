package you.thiago.qualanota.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        private val review = view.findViewById<TextView>(R.id.review)

        private val ratingStar1 = view.findViewById<ImageView>(R.id.star_enabled_1)
        private val ratingStar2 = view.findViewById<ImageView>(R.id.star_enabled_2)
        private val ratingStar3 = view.findViewById<ImageView>(R.id.star_enabled_3)
        private val ratingStar4 = view.findViewById<ImageView>(R.id.star_enabled_4)
        private val ratingStar5 = view.findViewById<ImageView>(R.id.star_enabled_5)

        fun setupItem(itemReview: ItemReview) {
            title.text = itemReview.title
            owner.text = itemReview.owner

            if (!itemReview.review.isNullOrBlank()) {
                review.text = itemReview.review
            } else {
                review.text = view.context.getString(R.string.empty_review)
            }

            toggleRating(itemReview.rating)

            view.setOnClickListener {
                listener.onAdapterClick(itemReview)
            }
        }

        private fun toggleRating(rating: Int?) {
            ratingStar1.visibility = View.VISIBLE
            ratingStar2.visibility = View.VISIBLE
            ratingStar3.visibility = View.VISIBLE
            ratingStar4.visibility = View.VISIBLE
            ratingStar5.visibility = View.VISIBLE

            when (rating) {
                1 -> {
                    ratingStar2.visibility = View.INVISIBLE
                    ratingStar3.visibility = View.INVISIBLE
                    ratingStar4.visibility = View.INVISIBLE
                    ratingStar5.visibility = View.INVISIBLE
                }
                2 -> {
                    ratingStar3.visibility = View.INVISIBLE
                    ratingStar4.visibility = View.INVISIBLE
                    ratingStar5.visibility = View.INVISIBLE
                }
                3 -> {
                    ratingStar4.visibility = View.INVISIBLE
                    ratingStar5.visibility = View.INVISIBLE
                }
                4 -> {
                    ratingStar5.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun interface AdapterItemReviewClickListener {
        fun onAdapterClick(itemReview: ItemReview)
    }
}
