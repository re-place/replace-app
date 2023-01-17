package replace.datastore

import replace.model.Bookings
import replace.model.Files
import replace.model.Sites

// repositories that don't have any special methods

typealias BookingRepository = Repository<Bookings>
typealias SiteRepository = Repository<Sites>
typealias FileRepository = Repository<Files>
