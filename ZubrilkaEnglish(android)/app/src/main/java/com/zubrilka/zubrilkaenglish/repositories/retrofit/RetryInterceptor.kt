package com.zubrilka.zubrilkaenglish.repositories.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.SocketTimeoutException

class RetryInterceptor(private val maxRetry: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        var response: Response? = null
        var tryCount = 0

        while (tryCount < maxRetry) {
            try {
                response = chain.proceed(request)
                break // если успешно, выходим из цикла
            } catch (e: SocketTimeoutException) {
                tryCount++
                if (tryCount >= maxRetry) {
                    throw e // если достигнут максимум попыток, выбрасываем исключение
                }
            } catch (e: Exception) {
                throw e // обработка других IOException
            }
        }

        return response!!
    }
}