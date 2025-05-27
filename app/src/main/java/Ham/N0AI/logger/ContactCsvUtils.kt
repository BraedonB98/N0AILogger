package ham.n0ai.logger

import android.content.Context

object ContactCsvUtils {
    private const val CSV_FILE_NAME = "contacts.csv"

    fun contactToCsv(contact: Contact): String =
        listOf(
            contact.callsign,
            contact.date,
            contact.time,
            contact.frequency,
            contact.frequencyUnit,
            contact.power,
            contact.mode,
            contact.notes.replace(",", ";")
        ).joinToString(",")

    fun csvToContact(line: String): Contact? {
        val parts = line.split(",")
        if (parts.size < 8) return null
        return Contact(
            callsign = parts[0],
            date = parts[1],
            time = parts[2],
            frequency = parts[3],
            frequencyUnit = parts[4],
            power = parts[5],
            mode = parts[6],
            notes = parts[7]
        )
    }

    fun saveContact(context: Context, contact: Contact) {
        context.openFileOutput(CSV_FILE_NAME, Context.MODE_APPEND).bufferedWriter().use { writer ->
            writer.write(contactToCsv(contact))
            writer.newLine()
        }
    }

    fun loadContacts(context: Context): List<Contact> {
        val file = context.getFileStreamPath(CSV_FILE_NAME)
        if (!file.exists()) return emptyList()
        return context.openFileInput(CSV_FILE_NAME).bufferedReader().readLines()
            .mapNotNull { csvToContact(it) }
    }

    fun saveAllContacts(context: Context, contacts: List<Contact>) {
        context.openFileOutput(CSV_FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            contacts.forEach {
                writer.write(contactToCsv(it))
                writer.newLine()
            }
        }
    }
}