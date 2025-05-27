package ham.n0ai.logger.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ham.n0ai.logger.Contact
import ham.n0ai.logger.R
import java.text.SimpleDateFormat
import java.util.Locale

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val callsign: TextView = itemView.findViewById(R.id.textCallsign)
        private val mode: TextView = itemView.findViewById(R.id.textMode)
        private val frequency: TextView = itemView.findViewById(R.id.textFrequency)
        private val power: TextView = itemView.findViewById(R.id.textPower)
        private val dateTime: TextView = itemView.findViewById(R.id.textDateTime)

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val inputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        private val outputDateFormat = SimpleDateFormat("MMMM d yyyy", Locale.getDefault())
        private val outputTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        fun bind(contact: Contact) {
            callsign.text = contact.callsign
            mode.text = contact.mode
            frequency.text = "${contact.frequency} ${contact.frequencyUnit}"
            power.text = "${contact.power}W"

            // Format date
            val formattedDate = try {
                val date = inputDateFormat.parse(contact.date)
                if (date != null) outputDateFormat.format(date) else contact.date
            } catch (e: Exception) {
                contact.date
            }

            // Format time
            val formattedTime = try {
                val time = inputTimeFormat.parse(contact.time)
                if (time != null) outputTimeFormat.format(time) else contact.time
            } catch (e: Exception) {
                contact.time
            }

            dateTime.text = "$formattedDate $formattedTime"
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem === newItem
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }
}