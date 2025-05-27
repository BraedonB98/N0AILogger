package ham.n0ai.logger

import java.io.Serializable

data class Contact(
    val callsign: String,
    val date: String,
    val time: String,
    val frequency: String,
    val frequencyUnit: String,
    val power: String,
    val mode: String,
    val notes: String
) : Serializable