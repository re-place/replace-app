package replace.usecase

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.PostgreSQLContainer
import replace.model.BookableEntities
import replace.model.BookableEntityTypes
import replace.model.BookedEntities
import replace.model.Bookings
import replace.model.Floors
import replace.model.Users

inline fun useDatabase(block: () -> Unit) {
    PostgreSQLContainer("postgres").apply {
        withExposedPorts(5432)
        withEnv("POSTGRES_PASSWORD", "password")
    }.use {
        it.start()
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/test;DB_CLOSE_DELAY=-1",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "password",
        )
        val tables: Array<out Table> = arrayOf(
            BookedEntities,
            BookableEntities,
            BookableEntityTypes,
            Bookings,
            Floors,
            Users,
        )
        transaction {
            SchemaUtils.create(*tables)
        }
        block()
    }
}
