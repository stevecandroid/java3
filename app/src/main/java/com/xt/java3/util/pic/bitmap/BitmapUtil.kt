package com.xt.java3.util.pic.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlin.properties.Delegates




/**
 * Created by steve on 17-9-16.
 */
/**
 * bitmap转为base64
 * @param bitmap
 * @return
 */
class BitmapUtil {

    companion object {


        fun bitmapToBase64(bitmap: Bitmap?): String  {

            var result: String by Delegates.notNull()
            var baos: ByteArrayOutputStream? = null
            try {
                if (bitmap != null) {
                    baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                    baos.flush()
                    baos.close()

                    val bitmapBytes = baos.toByteArray()
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                throw e
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    throw e
                }

            }
            return result
        }

        /**
         * base64转为bitmap
         * @param base64Data
         * @return
         */
        fun base64ToBitmap(base64Data: String): Bitmap? {
            if(base64Data == null) return null
            val bytes = Base64.decode(base64Data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun base64toBytes(base64Data: String): ByteArray? {
            return  Base64.decode(base64Data, Base64.DEFAULT)
        }

        fun Bitmap2Bytes(bm: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 80, baos)
            return baos.toByteArray()
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqSize:Long): Int {
                                                                        //kB

            var height = options.outHeight
            var width = options.outWidth

            var inSampleSize = 1

            while(height*width*4/1024 > reqSize){
                inSampleSize *= 2
                height = height/2
                width = width/2
            }

            return inSampleSize
        }

        fun getOptimalBitmap(path : String) : Bitmap{

            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path,o)
            val inSampleSize = calculateInSampleSize(o,1536)
            o.inJustDecodeBounds = false
            o.inSampleSize = inSampleSize
            return BitmapFactory.decodeFile(path,o)
        }

        fun compressByQuality(bitmap: Bitmap,maxByteSize: Long): Bitmap? {
            if (maxByteSize <= 0) return null
            val baos = ByteArrayOutputStream()
            var quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            while (bitmap.size() > maxByteSize && quality > 10) {
                baos.reset()
                Log.e("Companion",quality.toString())
                bitmap.compress(Bitmap.CompressFormat.JPEG, { quality -= 10;quality }(), baos)
            }
            if (quality < 0) return null
            val bytes = baos.toByteArray()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

    }



}

fun Bitmap.size() = rowBytes * byteCount



