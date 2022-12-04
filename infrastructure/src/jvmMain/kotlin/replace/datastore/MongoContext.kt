package replace.datastore

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import replace.model.Booking

class MongoContext {
    init {
        val client: CoroutineClient = KMongo.createClient().coroutine
        val db = client.getDatabase("test")
        val collection = db.getCollection<Booking>()
    }
}
