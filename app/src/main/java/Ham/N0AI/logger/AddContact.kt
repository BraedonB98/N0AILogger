package ham.n0ai.logger

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class AddContact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val editTextTime = findViewById<EditText>(R.id.editTextTime)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Set current date and time
        editTextDate.setText(dateFormat.format(calendar.time))
        editTextTime.setText(timeFormat.format(calendar.time))

        // Show DatePickerDialog on date field click
        editTextDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(y, m, d)
                editTextDate.setText(dateFormat.format(calendar.time))
            }, year, month, day).show()
        }

        // Show TimePickerDialog on time field click
        editTextTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(this, { _, h, m ->
                calendar.set(Calendar.HOUR_OF_DAY, h)
                calendar.set(Calendar.MINUTE, m)
                editTextTime.setText(timeFormat.format(calendar.time))
            }, hour, minute, true).show()
        }

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val contact = Contact(
                callsign = findViewById<EditText>(R.id.editTextCallsign).text.toString(),
                date = findViewById<EditText>(R.id.editTextDate).text.toString(),
                time = findViewById<EditText>(R.id.editTextTime).text.toString(),
                frequency = findViewById<EditText>(R.id.editTextFrequency).text.toString(),
                frequencyUnit = findViewById<Spinner>(R.id.spinnerFrequencyUnit).selectedItem.toString(),
                power = findViewById<EditText>(R.id.editTextPower).text.toString(),
                mode = findViewById<EditText>(R.id.editTextMode).text.toString(),
                notes = findViewById<EditText>(R.id.editTextNotes).text.toString()
            )
            ContactCsvUtils.saveContact(this, contact) // <-- Save to CSV

            val resultIntent = Intent()
            resultIntent.putExtra("contact", contact)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}