package replace.datastore

import replace.model.Booking
import replace.model.File
import replace.model.Site

// repositories that don't have any special methods

typealias BookingRepository = Repository<Booking>
typealias SiteRepository = Repository<Site>
typealias FileRepository = Repository<File>
