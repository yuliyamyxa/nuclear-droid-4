import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hw4.NotificationService
const val TAG = "MyWorker"

class MyWorker (
    val context: Context,
    params: WorkerParameters,
): Worker(context, params) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        //TODO("Not yet implemented")
        Log.d(TAG, "Starting work//.")
        val msg = inputData.getString("notification_text")
        Log.d("TAG", "site, vot notification: $msg")


        val intent = Intent(context, NotificationService::class.java).apply {
            putExtra("NOTIFICATION", msg)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
        /*val CHANNEL_ID : String = "123"
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("move bitches")
            .setContentText(msg)
            .setSmallIcon(com.example.hw4.R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()


        NotificationManagerCompat.from(context).notify(1, notification)
        //Thread.sleep(1000)*/
        Log.d(TAG, "work finished")
        return Result.success()
    }
}