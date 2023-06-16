package pwr.project.interaktywna_szklarnia

import java.sql.Connection
import java.sql.DriverManager

class DatabaseManager {
    private var connection: Connection? = null

    fun connect(): Connection? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val url = "jdbc:mysql://192.168.20.68/Greenhouse"
            val username = "root"
            val password = "1234"

            connection = DriverManager.getConnection(url, username, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return connection
    }

    fun disconnect() {
        connection?.close()
        connection = null
    }
}