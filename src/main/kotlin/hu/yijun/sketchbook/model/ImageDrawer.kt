package hu.yijun.sketchbook.model

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.awt.Graphics2D
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

    private val executor = Executors.newSingleThreadExecutor()
    private val dispatcher = executor.asCoroutineDispatcher()
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
    private val channel = Channel<DrawAction>(Channel.UNLIMITED)
    private val job: Job = scope.launch {
        val batch = mutableListOf<DrawAction>()
        while (isActive) {
            if (imageModel == null) {
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

            imageModel?.draw(batch)
            batch.clear()
        }
    }

    override fun draw(drawAction: DrawAction) {
        channel.trySend(drawAction).getOrThrow()
    }

    override fun install(newModel: ImageModel) {
        imageModel = newModel
    }

    override fun uninstall() {
        imageModel = null
    }

    override fun close() {
        job.cancel()
        channel.close()
        dispatcher.close()
        executor.shutdown()
        graphics?.dispose()
    }
}