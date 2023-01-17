package replace.model

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import org.jetbrains.exposed.dao.BackReference
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.InnerTableLink
import org.jetbrains.exposed.dao.OptionalReference
import org.jetbrains.exposed.dao.OptionalReferrers
import org.jetbrains.exposed.dao.Reference
import org.jetbrains.exposed.dao.Referrers
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.VarCharColumnType

open class Model: IdTable<String>() {
    final override val id: Column<EntityID<String>> = varchar("id", 36).defaultExpression(CustomFunction("gen_random_uuid()", VarCharColumnType())).entityId()
    override val primaryKey = PrimaryKey(id)
}

// this is a pretty bad hack to get around the fact that Exposed doesn't support this check
fun<T : Comparable<T>> Entity<T>.isLoaded(relation: Column<*>): Boolean {
    val cache = Entity::class.java.getDeclaredField("referenceCache")
    cache.isAccessible = true

    @Suppress("UNCHECKED_CAST")
    val cacheMap = cache.get(this) as HashMap<Column<*>, Any?>

    return cacheMap.contains(relation)
}

fun <SRC : Entity<*>> getReferenceObjectFromDelegatedProperty(entity: SRC, property: KProperty1<SRC, Any?>): Any? {
    property.isAccessible = true
    return property.getDelegate(entity)
}

@Suppress("UNCHECKED_CAST")
fun<T : Comparable<T>> Entity<T>.test(vararg relations: KProperty1<out Entity<*>, Any?>): Boolean {
    val cache = Entity::class.java.getDeclaredField("referenceCache")
    cache.isAccessible = true


    val validMembers = this::class.memberProperties.filter { it in relations } as Collection<KProperty1<Entity<T>, Any?>>

    val columns = validMembers.map { prop ->
        when (val refObject = getReferenceObjectFromDelegatedProperty(this, prop)) {
            is Reference<*, *, *> -> {
                return@map (refObject as Reference<Comparable<Comparable<*>>, *, Entity<*>>).reference
            }
            is OptionalReference<*, *, *> -> {
                return@map (refObject as OptionalReference<Comparable<Comparable<*>>, *, Entity<*>>).reference
            }
            is Referrers<*, *, *, *, *> -> {
                return@map (refObject as Referrers<*, Entity<*>, *, Entity<*>, Any>).reference
            }
            is OptionalReferrers<*, *, *, *, *> -> {
                return@map (refObject as OptionalReferrers<*, Entity<*>, *, Entity<*>, Any>).reference
            }
            is InnerTableLink<*, *, *, *> -> {
                return@map (refObject as InnerTableLink<*, Entity<*>, Comparable<Comparable<*>>, Entity<Comparable<Comparable<*>>>>).sourceColumn
            }
            is BackReference<*, *, *, *, *> -> {
                return@map (refObject.delegate as Referrers<ID, Entity<ID>, *, Entity<*>, Any>).reference
            }


            else -> error("Relation delegate has an unknown type")
        }
    }

    val cacheMap = cache.get(this) as HashMap<Column<*>, Any?>

    columns.forEach {
        if (!cacheMap.contains(it)) {
            return true
        }
    }

    return true
}


fun<T : Comparable<T>, V> Entity<T>.whenLoaded(relation: Column<*>, callback: () -> V): V? {
    if (isLoaded(relation)) {
        return callback()
    }

    return null
}
