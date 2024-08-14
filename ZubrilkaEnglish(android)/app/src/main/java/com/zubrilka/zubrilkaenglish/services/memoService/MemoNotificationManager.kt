package com.zubrilka.zubrilkaenglish.services.memoService

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zubrilka.zubrilkaenglish.R
import com.zubrilka.zubrilkaenglish.models.Memo
import com.zubrilka.zubrilkaenglish.screens.MainActivity

class MemoNotificationManager private constructor(){
    companion object{
        val instance: MemoNotificationManager by lazy { MemoNotificationManager() }
    }

    private val CHANNEL_ID = "ZubrilkaEnglish-channel-notification"

    /**
     * установит будильник для показа уведомления
     */
    fun setupAlarm(memo: Memo, context: Context){
        val calendar = Calendar.getInstance()

        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent: PendingIntent = Intent(context, MemoReceiver::class.java).let { intent ->
            intent.putExtra("memo_id",memo.id)
            PendingIntent.getBroadcast(
                context,
                memo.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        calendar.set(Calendar.HOUR_OF_DAY,memo.hour)
        calendar.set(Calendar.MINUTE,memo.minutes)
        calendar.set(Calendar.SECOND,0)
        // Вычитаем один день
        calendar.add(Calendar.DAY_OF_MONTH, 0)

        // Установка alarm на каждый день в выбранное время
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }


    /**
     * покажет уведомление
     */
    fun showMemoNotification(memo: Memo, context: Context){
        createNotificationChanel(context)

        val notificationId = 101

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context,0,intent,
            PendingIntent.FLAG_IMMUTABLE)

        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.book_icon)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(memo.note) // Устанавливаем раскрывающийся текст

        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.book_svg)
            .setContentText(memo.note)
            .setLargeIcon(bitmap)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(bigTextStyle)

        with(NotificationManagerCompat.from(context)){
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(notificationId,builder.build())
        }
    }

    /**
     * создаст канал уведомлений
     */
    private fun createNotificationChanel(context: Context) {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    /**
     * отменит alarm
     */
    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MemoReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

}