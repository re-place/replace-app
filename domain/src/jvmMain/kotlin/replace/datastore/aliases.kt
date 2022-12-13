package replace.datastore

import replace.model.BookableEntity
import replace.model.Booking
import replace.model.Location

// repositories that don't have any special methods

typealias BookableEntityRepository = Repository<BookableEntity>
typealias BookingRepository = Repository<Booking>
typealias LocationRepository = Repository<Location>
