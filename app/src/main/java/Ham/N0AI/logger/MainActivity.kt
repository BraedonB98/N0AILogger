package ham.n0ai.logger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import ham.n0ai.logger.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import ham.n0ai.logger.ContactViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val contactViewModel: ContactViewModel by viewModels()

    private val addContactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val contact = result.data?.getSerializableExtra("contact") as? Contact
            contact?.let {
                contactViewModel.addContact(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        // Load contacts from CSV and set to ViewModel
        val loadedContacts = ContactCsvUtils.loadContacts(this)
        loadedContacts.forEach { contactViewModel.addContact(it) }

        // Save to CSV whenever contacts change
        contactViewModel.contacts.observe(this) { contacts ->
            ContactCsvUtils.saveAllContacts(this, contacts)
        }

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this, AddContact::class.java)
            addContactLauncher.launch(intent)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener { menuItem ->
            // Only handle existing drawer items here
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigateUp(appBarConfiguration)
            true
        }
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_send_contacts -> {
                shareContactsCsv()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Optionally, also save onPause as a backup
    override fun onPause() {
        super.onPause()
        ContactCsvUtils.saveAllContacts(this, contactViewModel.contacts.value ?: emptyList())
    }

    // Add this function to MainActivity
    private fun shareContactsCsv() {
        val csvFile = getFileStreamPath("contacts.csv")
        if (!csvFile.exists()) {
            Snackbar.make(binding.root, "No contacts file to share.", Snackbar.LENGTH_SHORT).show()
            return
        }
        val uri = androidx.core.content.FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            csvFile
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share Contacts CSV"))
    }
}