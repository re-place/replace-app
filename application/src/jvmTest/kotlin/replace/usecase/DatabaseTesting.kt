package replace.usecase

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookableEntities
import replace.model.BookableEntityTypes
import replace.model.Floors

fun prepareDatabase() {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    val tables: Array<out Table> = arrayOf(BookableEntities, BookableEntityTypes, Floors)
    transaction {
        SchemaUtils.drop(*tables)
        SchemaUtils.create(*tables)
    }
}
