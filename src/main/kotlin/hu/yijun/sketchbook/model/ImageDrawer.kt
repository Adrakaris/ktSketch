package hu.yijun.sketchbook.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.awt.Graphics2D
import java.lang.AutoCloseable
import java.util.concurrent.Executors

interface ImageDrawer {
    fun install(newModel: ImageModel)
    fun uninstall()
    fun draw(drawAction: DrawAction)
}

class CoroutineImageDrawer : AutoCloseable, ImageDrawer {
    private var imageModel: ImageModel? = null
        set(model) {
            field = model
            graphics = model?.image?.createGraphics()
        }

    private var graphics: Graphics2D? = null

    private val lock = Any()
    private val executor = Executors.newSingleThreadExecutor()
    private val dispatcher = executor.asCoroutineDispatcher()
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private val channel = Channel<DrawAction>(Channel.UNLIMITED)
    private val job: Job = scope.launch {
        val batch = mutableListOf<DrawAction>()
        while (isActive) {
            if (graphics == null) {
                delay(32)
                continue
            }

            val first = channel.receive()
            batch.add(first)

            withTimeoutOrNull(16) {
                while (true) {
                    batch.add(channel.receive())
                }
            }

            synchronized(lock) {
                for (command in batch) {
                    command.draw(graphics!!)
                }
            }
            batch.clear()
        }
    }

    override fun draw(drawAction: DrawAction) {
        channel.trySend(drawAction).getOrThrow()
    }

    override fun install(newModel: ImageModel) {
        println("Install model (size ${newModel.imageSize})")
        synchronized(lock) {
            graphics?.dispose()
            imageModel = newModel
        }
    }

    override fun uninstall() {
        println("Uninstall model")
        synchronized(lock) {
            graphics?.dispose()
            imageModel = null
        }
    }

    override fun close() {
        job.cancel()
        channel.close()
        dispatcher.close()
        executor.shutdown()
        graphics?.dispose()
    }
}