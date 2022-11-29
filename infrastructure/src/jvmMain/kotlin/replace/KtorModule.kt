package replace

import com.google.inject.AbstractModule
import com.google.inject.Module
import com.google.inject.binder.AnnotatedBindingBuilder
import replace.boot.ModuleFactory

class KtorModule : AbstractModule() {
    object Factory : ModuleFactory {
        override fun create(): Module = KtorModule()
    }

    override fun configure() {
        bind<Backend>().toInstance(KtorBackend)
    }

    private inline fun <reified T : Any> bind(): AnnotatedBindingBuilder<T> = bind(T::class.java)
}
