package replace.boot

import com.google.inject.Guice
import com.google.inject.Injector
import replace.App

object AppFactory {
    private const val RESOURCE_NAME = "app-binding.txt"

    fun create(): App {
        val injector = createInjector()
        return object : App {
            override val injector: Injector = injector
        }
    }

    private fun createInjector(): Injector {
        return javaClass.getResourceAsStream(RESOURCE_NAME).use {
            checkNotNull(it) { "Could not find resource $RESOURCE_NAME" }
            val implName = it.bufferedReader().readLine()
            val moduleFactoryImpl = coerceClass<ModuleFactory>(implName).kotlin.run {
                checkNotNull(objectInstance) { "ModuleFactory $implName must be an object" }.create()
            }
            Guice.createInjector(moduleFactoryImpl)
        }
    }

    private inline fun <reified T : Any> coerceClass(implementation: String): Class<out T> {
        return Class.forName(implementation).asSubclass(T::class.java)
    }
}
