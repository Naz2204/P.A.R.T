package ua.ipze.kpi.part.providers.static

import android.content.Context

class GlobalApplicationContext {
    companion object {
        lateinit var context: Context
            private set

        fun init(context: Context) {
            if (::context.isInitialized) return
            this.context = context
        }
    }
}