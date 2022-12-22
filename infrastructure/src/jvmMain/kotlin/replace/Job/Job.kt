package replace.Job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

abstract class Job(
    private val interval: Long,
    private val initialDelay: Long? = null,
) : CoroutineScope {
    private val job: Job = Job()

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override val coroutineContext: CoroutineContext
        get() = job + singleThreadExecutor.asCoroutineDispatcher()

    fun cancel() {
        job.cancel()
        singleThreadExecutor.shutdown()
    }

    fun dispatch() = launch {
        initialDelay?.let {
            delay(it)
        }

        while (true) {
            run()
            delay(interval)
        }
    }

    protected abstract suspend fun run()
}
