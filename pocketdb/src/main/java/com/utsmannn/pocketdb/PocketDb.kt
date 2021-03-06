package com.utsmannn.pocketdb

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.utsmannn.pocketdb.extensions.encrypt
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class PocketDb(private val context: Context, private var key: String) {
    companion object {
        fun install(context: Context, key: String) {
            startKoin {
                androidContext(context)
                val module = module {
                    single { PocketDb(get(), key) }
                    single { Gson() }

                }
                modules(module)
            }
        }

        fun installModule(koinApplication: KoinApplication, key: String) {
            koinApplication.run {
                val module = module {
                    single { PocketDb(get(), key) }
                    single { Gson() }
                }

                modules(module)
            }
        }
    }

    private fun String.fixIllegalChar() = replace("/", "")
        .replace("\\", "")

    fun pref(identifier: String = "default"): SharedPreferences =
        context.getSharedPreferences(identifier.encrypt(key).fixIllegalChar(), Context.MODE_PRIVATE)

    fun getSecretKey() = key
}