package replace.datastore

import replace.model.BookableEntity
import replace.model.Booking
import replace.model.Site

// repositories that don't have any special methods

typealias BookingRepository = Repository<Booking>
typealias SiteRepository = Repository<Site>
