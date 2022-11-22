package replace

import com.google.inject.Injector
import replace.boot.AppFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

interface App {
    val injector: Injector

    companion object Default : App by AppFactory.create()
}

val App.backend: Backend
    get() = injector.getInstance(Backend::class.java)

operator fun <T : Any> App.get(type: KClass<T>): T = injector.getInstance(type.java)
inline fun <reified T : Any> App.get(): T = get(T::class)
inline fun <reified T : Any> injector(): ReadOnlyProperty<App, T> = ReadOnlyProperty { e, _ -> e.get() }
