package hu.yijun.sketchbook.util

import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.presenter.ImageMetadataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.binds
import org.koin.dsl.module

val mainModule = module {
    single { CanvasPresenter() }.binds(arrayOf(ImageMetadataRepository::class))
}

inline fun <reified T : Any> koinInject(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
