package you.thiago.qualanota.ui.itemowner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import you.thiago.qualanota.R
import you.thiago.qualanota.data.model.ItemOwner

class ItemOwnerAdapter(
    private val list: List<ItemOwner>,
    private val listener: AdapterItemOwnerClickListener,
) : RecyclerView.Adapter<ItemOwnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_owner, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupItem(list[position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val location = view.findViewById<TextView>(R.id.location)
        private val description = view.findViewById<TextView>(R.id.description)

        fun setupItem(itemOwner: ItemOwner) {
            name.text = itemOwner.name
            location.text = itemOwner.location
            description.text = itemOwner.description

            view.setOnClickListener {
                listener.onAdapterClick(itemOwner)
            }
        }
    }

    fun interface AdapterItemOwnerClickListener {
        fun onAdapterClick(itemOwner: ItemOwner)
    }
}
