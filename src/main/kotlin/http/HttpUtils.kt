package http

import okhttp3.*
import java.io.IOException

object HttpUtils {

    @JvmStatic
    fun get(url: String, success: (String?) -> Unit, failure: (IOException) -> Unit) {

        OkHttpClient().newCall(Request.Builder().url(url).get().build())
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    failure(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    success(response.body?.string())
                }
            })
    }

}