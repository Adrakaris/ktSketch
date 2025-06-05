package hu.yijun.sketchbook.util

import hu.yijun.sketchbook.model.CoroutineImageDrawer
import hu.yijun.sketchbook.model.ImageDrawer
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.presenter.ImageMetadataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.dsl.onClose

val mainModule = module {
    singleOf(::CoroutineImageDrawer).closeable() bind ImageDrawer::class
    single { CanvasPresenter(get()) }.binds(arrayOf(ImageMetadataRepository::class))
}

inline fun <reified T : Any> koinInject(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

inline fun <reified T : AutoCloseable> KoinDefinition<T>.closeable(): KoinDefinition<T> =
    onClose { it?.close() }
