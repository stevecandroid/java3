package com.xt.java3.util.pic

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.xt.java3.base.BaseActivity
import java.io.File

/**
 * Created by steve on 17-9-16.
 */
class PhotoTaker private constructor(private var act: BaseActivity) {

    lateinit private var imageUri: Uri
    private val TAKE_PHOTO = 998
    lateinit private var handler: (String?) -> Unit

    private lateinit var outputFolder : File
    private lateinit var outputImage : File

    companion object {
        fun with(act: BaseActivity): PhotoTaker {
            val obj = PhotoTaker(act)
            act.photoTaker = obj
            return obj
        }
    }

    fun takePicAndHandler(handler: (String?) -> Unit) {

        this.handler = handler

        outputFolder = File(act.externalCacheDir.path + "//images")
        outputImage = File(act.externalCacheDir.path + "//images//tmp.jpg")


        PermissionsWrapper.PermissionMgr.with(act).request {

            permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

            onAllSuccess {

                if (!outputFolder.exists()) {
                    outputFolder.mkdir()
                }

                if (outputImage.exists()) {
                    outputImage.delete()
                    outputImage.createNewFile()
                }

                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(outputImage)
                } else {
                    imageUri = FileProvider.getUriForFile(act, "com.xt.daka.fileprovider", outputImage)
                }
                // 启动相机程序
                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                act.startActivityForResult(intent, TAKE_PHOTO)

            }

        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO) {
            handler(outputImage.absolutePath)
        }
    }


}