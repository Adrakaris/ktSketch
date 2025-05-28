package hu.yijun.util

import hu.yijun.presenter.CanvasPresenter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

val mainModule = module {
    single { CanvasPresenter() }
}

inline fun <reified T : Any> koinInject(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
