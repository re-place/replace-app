package replace.boot

import com.google.inject.Module

interface ModuleFactory {
    fun create(): Module
}
