package hu.yijun.util

import hu.yijun.presenter.CanvasPresenter
import hu.yijun.presenter.ImageMetadataRepository
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
