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
import replace.model.TemporaryFiles
import replace.model.Users

val tables: Array<out Table> = arrayOf(
    BookedEntities,
    BookableEntities,
    BookableEntityTypes,
    Bookings,
    Floors,
    TemporaryFiles,
    Users,
)

inline fun useDatabase(block: () -> Unit) {
    PostgreSQLContainer("postgres").use { container ->
        container.start()
        println("Postgres container started @ ${container.jdbcUrl}")
        Database.connect(
            url = container.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = "test",
            password = "test",
        )
        println("Successfully connected to postgres")

        transaction {
            SchemaUtils.create(*tables)
        }
        println("Successfully created tables ${tables.joinToString { it.tableName }}")
        block()
    }
}
