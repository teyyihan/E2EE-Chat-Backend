package com.teyyihan.e2ee_chat.network

import com.teyyihan.e2ee_chat.model.Message
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmAPI {

    @POST("fcm/send")
    @Headers("Authorization: Bearer AAAA1HbkEbk:APA91bHwYmeUgCMxhVnrV1JRbid5OFUHZynZ936X51Wo55jbFuosZvsADfGs-a03oyFWoLDagzQvIHdn-LE1yV4LoCcMJlQgnwb-s-Lx976HxWXlgk30y2Gwk8DPNMLbYiod4lsb8WKC")
    suspend fun sendMessage(
            @Body message: Message
    ): MessageResponse

}