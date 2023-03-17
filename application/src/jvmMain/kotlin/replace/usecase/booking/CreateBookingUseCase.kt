package replace.usecase.booking

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Booking
import replace.model.Users

object CreateBookingUseCase {

    suspend fun execute(
        createBookingDto: CreateBookingDto,
        userId: String,
    ): BookingDto {
        return newSuspendedTransaction {
            if (createBookingDto.bookedEntityIds.isEmpty()) {
                throw IllegalArgumentException("BookedEntities must not be empty")
            }

            val start = Instant.parse(createBookingDto.start)
            val end = Instant.parse(createBookingDto.end)

            val duration = end.epochSeconds - start.epochSeconds

            if (duration <= 0) {
                throw IllegalArgumentException("End must be after start")
            }

            val now = Clock.System.now()

            if (end.epochSeconds < now.epochSeconds) {
                throw IllegalArgumentException("Start must be in the future")
            }

            val bookedAncestorCount = getBookedEntitiesAncestorCount(
                createBookingDto.bookedEntityIds,
                start,
                end,
            )

            if (bookedAncestorCount > 0) {
                throw IllegalArgumentException("One of the booked entities is already booked")
            }

            val bookedDescendantCount = getBookedEntitiesDescendantCount(
                createBookingDto.bookedEntityIds,
                start,
                end,
            )

            if (bookedDescendantCount > 0) {
                throw IllegalArgumentException("One of the booked entities has a descendant that is already booked")
            }

            val newBookedEntities = BookableEntity.forEntityIds(createBookingDto.bookedEntityIds.map { EntityID(it, BookableEntities) })

            val booking = Booking.new {
                this.start = start
                this.end = end
                this.userId = EntityID(userId, Users)
                this.bookedEntities = newBookedEntities
            }

            booking.toDto()
        }
    }

    private suspend fun getBookedEntitiesAncestorCount(
        entityIds: List<String>,
        start: Instant,
        end: Instant,
    ): Int {
        // We use a recursive query to retrieve all ascendants (including itself), join them with bookings in the given time range and count the result
        // if the result is > 0 then one of the ascendants (or itself) is already booked
        return newSuspendedTransaction {
            val query = TransactionManager.current().connection.prepareStatement(
                """
                |WITH RECURSIVE withAncestors AS (
                |    SELECT id, parent_id
                |    FROM bookable_entities
                |    WHERE id IN (${entityIds.joinToString(",") { "?" }})
                |    UNION
                |    SELECT bookable_entities.id, bookable_entities.parent_id
                |        FROM bookable_entities
                |        JOIN withAncestors ON withAncestors.parent_id = bookable_entities.id
                |),
                |withBookings as (
                |   SELECT
                |    booked_entities.id,
                |    booked_entities.bookable_entity_id,
                |    booked_entities.booking_id
                |    FROM booked_entities
                |    JOIN withAncestors ON withAncestors.id = booked_entities.bookable_entity_id
                |    JOIN bookings ON bookings.id = booked_entities.booking_id
                |    WHERE bookings.start <= ?::timestamp AND bookings.end > ?::timestamp
                |)
                |SELECT
                |    COUNT(withBookings.id) as count
                |    FROM withBookings
                |
                """.trimMargin(),
                false,
            )

            val queryParams: MutableList<Pair<IColumnType, *>> = entityIds.map { Pair(VarCharColumnType(), it) }.toMutableList()

            queryParams.add(Pair(VarCharColumnType(), end.toString()))
            queryParams.add(Pair(VarCharColumnType(), start.toString()))

            query.fillParameters(queryParams)

            val resultSet = query.executeQuery()
            resultSet.next()
            resultSet.getInt("count")
        }
    }

    private suspend fun getBookedEntitiesDescendantCount(
        entityIds: List<String>,
        start: Instant,
        end: Instant,
    ): Int {
        // We use a recursive query to retrieve all descendants (including itself), join them with bookings in the given time range and count the result
        // if the result is > 0 then one of the descendants (or itself) is already booked
        return newSuspendedTransaction {
            val query = TransactionManager.current().connection.prepareStatement(
                """
                |WITH RECURSIVE withAncestors AS (
                |    SELECT id, parent_id
                |    FROM bookable_entities
                |    WHERE id IN (${entityIds.joinToString(",") { "?" }})
                |    UNION
                |    SELECT bookable_entities.id, bookable_entities.parent_id
                |        FROM bookable_entities
                |        JOIN withAncestors ON withAncestors.id = bookable_entities.parent_id
                |),
                |withBookings as (
                |   SELECT
                |    booked_entities.id,
                |    booked_entities.bookable_entity_id,
                |    booked_entities.booking_id
                |    FROM booked_entities
                |    JOIN withAncestors ON withAncestors.id = booked_entities.bookable_entity_id
                |    JOIN bookings ON bookings.id = booked_entities.booking_id
                |    WHERE bookings.start <= ?::timestamp AND bookings.end > ?::timestamp
                |)
                |SELECT
                |    COUNT(withBookings.id) as count
                |    FROM withBookings
                |
                """.trimMargin(),
                false,
            )

            val queryParams: MutableList<Pair<IColumnType, *>> = entityIds.map { Pair(VarCharColumnType(), it) }.toMutableList()

            queryParams.add(Pair(VarCharColumnType(), end.toString()))
            queryParams.add(Pair(VarCharColumnType(), start.toString()))

            query.fillParameters(queryParams)

            val resultSet = query.executeQuery()
            resultSet.next()
            resultSet.getInt("count")
        }
    }
}
