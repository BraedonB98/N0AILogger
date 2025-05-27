package ham.n0ai.logger.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ham.n0ai.logger.Contact
import ham.n0ai.logger.ContactViewModel
import ham.n0ai.logger.R
import ham.n0ai.logger.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val contactViewModel: ContactViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        adapter = ContactAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        contactViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            adapter.submitList(contacts.toList()) // To create a new list for DiffUtil
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}