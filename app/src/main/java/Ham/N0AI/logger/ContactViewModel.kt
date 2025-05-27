package ham.n0ai.logger

import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {
    private val _contacts = MutableLiveData<MutableList<Contact>>(mutableListOf())
    val contacts: LiveData<MutableList<Contact>> = _contacts

    fun addContact(contact: Contact) {
        _contacts.value?.add(0, contact) // Add to top
        _contacts.value = _contacts.value // Trigger observers
    }

    fun deleteContact(contact: Contact) {
        _contacts.value?.remove(contact)
        _contacts.value = _contacts.value // Trigger observers
    }
}