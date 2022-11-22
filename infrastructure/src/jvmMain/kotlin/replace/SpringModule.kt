package replace

import com.google.inject.AbstractModule
import com.google.inject.binder.AnnotatedBindingBuilder
import replace.boot.ModuleFactory

class SpringModule : AbstractModule() {
    object Factory : ModuleFactory {
        override fun create() = SpringModule()
    }

    override fun configure() {
        bind<Backend>().toInstance(SpringApp.SpringBackend)
    }

    private inline fun <reified T : Any> bind(): AnnotatedBindingBuilder<T> = bind(T::class.java)
}
