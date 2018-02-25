package com.gh0u1l5.wechatmagician.spellbook.hookers

import com.gh0u1l5.wechatmagician.spellbook.Global.STATUS_FLAG_DATABASE
import com.gh0u1l5.wechatmagician.spellbook.WechatPackage.SQLiteCancellationSignal
import com.gh0u1l5.wechatmagician.spellbook.WechatPackage.SQLiteCursorFactory
import com.gh0u1l5.wechatmagician.spellbook.WechatPackage.SQLiteDatabase
import com.gh0u1l5.wechatmagician.spellbook.WechatPackage.SQLiteErrorHandler
import com.gh0u1l5.wechatmagician.spellbook.WechatStatus
import com.gh0u1l5.wechatmagician.spellbook.annotations.WechatHookMethod
import com.gh0u1l5.wechatmagician.spellbook.hookers.base.EventCenter
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IDatabaseHook
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IDatabaseHookRaw
import com.gh0u1l5.wechatmagician.spellbook.util.C
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object Database : EventCenter() {

    override val interfaces: List<Class<*>>
        get() = listOf(IDatabaseHook::class.java, IDatabaseHookRaw::class.java)

    @WechatHookMethod @JvmStatic fun hookEvents() {
        findAndHookMethod(
                SQLiteDatabase, "openDatabase",
                C.String, SQLiteCursorFactory, C.Int, SQLiteErrorHandler, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseOpen") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseOpen(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseOpen") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseOpen(param)
                    }
                }
                notify("onDatabaseOpen") { plugin ->
                    if (plugin is IDatabaseHook) {
                        val path     = param.args[0] as String
                        val database = param.result
                        plugin.onDatabaseOpen(path, database)
                    }
                }
            }
        })

        findAndHookMethod(
                SQLiteDatabase, "rawQueryWithFactory",
                SQLiteCursorFactory, C.String, C.StringArray, C.String, SQLiteCancellationSignal, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseQuery") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseQuery(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseQuery") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseQuery(param)
                    }
                }
            }
        })

        findAndHookMethod(
                SQLiteDatabase, "insertWithOnConflict",
                C.String, C.String, C.ContentValues, C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseInsert") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseInsert(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseInsert") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseInsert(param)
                    }
                }
            }
        })

        findAndHookMethod(
                SQLiteDatabase, "updateWithOnConflict",
                C.String, C.ContentValues, C.String, C.StringArray, C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseUpdate") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseUpdate(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseUpdate") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseUpdate(param)
                    }
                }
            }
        })

        findAndHookMethod(
                SQLiteDatabase, "delete",
                C.String, C.String, C.StringArray, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseDelete") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseDelete(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseDelete") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseDelete(param)
                    }
                }
            }
        })

        findAndHookMethod(
                SQLiteDatabase, "executeSql",
                C.String, C.ObjectArray, SQLiteCancellationSignal, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                notify("beforeDatabaseExecute") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.beforeDatabaseExecute(param)
                    }
                }
            }
            override fun afterHookedMethod(param: MethodHookParam) {
                notify("afterDatabaseExecute") { plugin ->
                    if (plugin is IDatabaseHookRaw) {
                        plugin.afterDatabaseExecute(param)
                    }
                }
            }
        })

        WechatStatus.toggle(STATUS_FLAG_DATABASE, true)
    }
}