package ham.n0ai.logger.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ham.n0ai.logger.Contact
import ham.n0ai.logger.ContactViewModel
import ham.n0ai.logger.R

class ContactDetailsFragment : DialogFragment() {

    private val contactViewModel: ContactViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contact = arguments?.getSerializable("contact") as? Contact
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_contact_details, null)

        view.findViewById<TextView>(R.id.detailsCallsign).text = contact?.callsign
        view.findViewById<TextView>(R.id.detailsMode).text = contact?.mode
        view.findViewById<TextView>(R.id.detailsFrequency).text = "${contact?.frequency} ${contact?.frequencyUnit}"
        view.findViewById<TextView>(R.id.detailsPower).text = "${contact?.power}W"
        view.findViewById<TextView>(R.id.detailsDateTime).text = "${contact?.date} ${contact?.time}"
        view.findViewById<TextView>(R.id.detailsNotes).text = contact?.notes

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Contact Details")
            .setView(view)
            .setPositiveButton("Close", null)
            .setNegativeButton("Delete") { _, _ ->
                contact?.let {
                    contactViewModel.deleteContact(it)
                }
            }
            .create()

        dialog.setOnShowListener {
            val deleteButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            deleteButton.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            // Alignment is handled by the system, negative is left, positive is right
        }

        return dialog
    }

    companion object {
        fun newInstance(contact: Contact): ContactDetailsFragment {
            val fragment = ContactDetailsFragment()
            val args = Bundle()
            args.putSerializable("contact", contact)
            fragment.arguments = args
            return fragment
        }
    }
}