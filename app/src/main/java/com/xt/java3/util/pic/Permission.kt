package com.xt.java3.util.pic


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.blankj.utilcode.util.ToastUtils
import com.xt.java3.base.BaseActivity


/**
 * Created by steve on 17-8-30.
 */


/**
 * 包装权限
 * onSuccess onDenied 为根据对应位置的权限申请结果调用的方法组
 * 若 All 存在 优先All
 * 若 Some 存在 优先Some
 */
class PermissionsWrapper {


    private var onSuccess: Array<() -> Unit> = arrayOf()

    private var onDenied: Array<() -> Unit> = arrayOf()

    private var onAllSuccess: (() -> Unit)? = null

    private var onSomeDenied: (() -> Unit)? = null

    private lateinit var permissions: Array<out String>

    fun onSuccess(s: Array<() -> Unit>) {
        onSuccess = s
    }

    fun onDenied(d: Array<() -> Unit>) {
        onDenied = d
    }

    fun onAllSuccess(als: () -> Unit) {
        onAllSuccess = als
    }

    fun onSomeDenied(sd: () -> Unit) {
        onSomeDenied = sd
    }

    fun permissions(vararg ps: String) {
        permissions = ps
    }


    /**
     * 权限申请工具类
     * unRegisterPos 仍为申请权限的位置,获取对应位置的数组
     */
    class PermissionMgr private constructor(private val mActiviy: BaseActivity) {

        private  var wrapper: PermissionsWrapper = PermissionsWrapper()
        private val unRegisterPos = mutableListOf<Int>()
        private var defaultConfig: Boolean = false
        private var requestCode = 1

        companion object {
            fun with(mActiviy: BaseActivity): PermissionMgr {
                mActiviy.permissionMgr = PermissionMgr(mActiviy)
                return mActiviy.permissionMgr
            }
        }

        fun setupDefaultConfig(): PermissionMgr {
            defaultConfig = true
            return this
        }

        fun setRequestCode(code: Int): PermissionMgr {
            requestCode = code
            return this
        }

        fun onRequestPermissionsResult(requestCode: Int, p: Array<out String>, grantResults: IntArray) {

            var t = 0   //用于检测权限通过的数量与申请数量是否一致 否则权限未完全申请成功

            if (requestCode == this.requestCode) {
                grantResults.forEachIndexed { index, results ->
                    if (results == PackageManager.PERMISSION_GRANTED) {
                        t++


                        if (wrapper.onAllSuccess != null) {
                            if (t == grantResults.size) wrapper.onAllSuccess!!.invoke()
                        } else {
                            wrapper.onSuccess[unRegisterPos[index]].invoke()
                        }

                    } else {
                        if (wrapper.onSomeDenied == null) {
                            wrapper.onDenied[unRegisterPos[index]].invoke()
                        }
                    }
                }
            }

            if (t != grantResults.size) {
                if (wrapper.onSomeDenied != null) {
                    wrapper.onSomeDenied!!.invoke()

                    if (defaultConfig) {
                        ToastUtils.showShort("请设置对应权限")
                        mActiviy.startApplicationPage()
                    }
                }
            }

        }

        /**
         * 申请权限
         */
        fun request(init: PermissionsWrapper.() -> Unit) {
            val unRegisterPermissions = mutableListOf<String>()
            wrapper.init()


            //一旦　onSuccess 和　onDenied 而其他未设置,而其他未设置,则要求 他们的要处理完所有情况 否则抛出异常
            if ((wrapper.onSuccess.size != wrapper.permissions.size ||
                    wrapper.onDenied.size != wrapper.permissions.size) && wrapper.onSuccess.size != wrapper.onDenied.size) {
                if ((wrapper.onSomeDenied == null && wrapper.onAllSuccess == null))
                    throw Exception(" onSuccess's Size and onDenied's Size must as same as permission you ask !!")
            }


            //　未设置任何处理方法的的情况
            if (wrapper.onAllSuccess == null && wrapper.onSuccess.isEmpty()) {
                wrapper.onAllSuccess = {}
            }
            //　未设置任何处理方法的的情况
            if (wrapper.onSomeDenied == null && wrapper.onDenied.isEmpty()) {
                wrapper.onSomeDenied = {}
            }

            wrapper.permissions.forEachIndexed { index, permission ->
                if (ActivityCompat.checkSelfPermission(mActiviy, permission) == PackageManager.PERMISSION_GRANTED) {
                    if (wrapper.onAllSuccess == null) {
                        wrapper.onSuccess[index].invoke()
                    }
                } else {
                    unRegisterPermissions.add(permission)
                    unRegisterPos.add(index)
                }
            }

            if (unRegisterPermissions.size != 0) {
                ActivityCompat.requestPermissions(mActiviy, unRegisterPermissions.toTypedArray(), requestCode)
            } else {

                if (wrapper.onAllSuccess != null) {
                    wrapper.onAllSuccess!!.invoke()
                }

            }
        }

    }

}

/**
 * 开启权限页面的方法
 */
fun Context.startApplicationPage() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    this.startActivity(intent)
}



