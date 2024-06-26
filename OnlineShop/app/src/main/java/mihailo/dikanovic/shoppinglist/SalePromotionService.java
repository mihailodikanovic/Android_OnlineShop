package mihailo.dikanovic.shoppinglist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class SalePromotionService extends Service
{
    private static final String CHANNEL_ID = "sale_channel";
    private static final int SALE_NOTIFICATION_ID = 1;
    private static final int END_NOTIFICATION_ID = 2;

    private static final long SALE_DURATION = 20 * 1000; // 10 minutes
    private static final long SALE_PERIOD = 24 * 60 * 60 * 1000; // 24 hours

    private boolean saleStarted = false;

    private Timer dailyNotificationTimer;
    private CountDownTimer countdownTimer;


    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotificationChannel();
        scheduleDailySaleNotification();
    }

    private void scheduleDailySaleNotification()
    {
        dailyNotificationTimer = new Timer();
        dailyNotificationTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if (!saleStarted)
                {
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            startCountdown();  //SALE STARTED

                            adjustItemPrices(true);
                            saleStarted = true;
                        }
                    });
                }
            }
        }, 0, SALE_PERIOD);
    }

    private void startCountdown()
    {
        countdownTimer = new CountDownTimer(SALE_DURATION, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                saleCountdownNotification(millisUntilFinished);
            }

            @Override
            public void onFinish()
            {
                saleCountdownNotification(0);
                sendSaleEndNotification();  //SALE ENDED

                adjustItemPrices(false);
                saleStarted = false;

                countdownTimer.cancel();
            }
        };

        countdownTimer.start();
    }

    private void saleCountdownNotification(long millisUntilFinished)
    {
        long minutes = millisUntilFinished / (1000 * 60);
        long seconds = (millisUntilFinished / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sale_icon)
                .setContentTitle("SALE STARTED!!!")
                .setContentText("-20% ON ALL ITEMS FOR THE NEXT " + timeLeftFormatted)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(SALE_NOTIFICATION_ID, builder.build());
    }

    private void sendSaleEndNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sale_icon)
                .setContentTitle("SALE ENDED.")
                .setContentText("Next sale in 24h")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(END_NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel()
    {
        CharSequence name = "Sale Notification";
        String description = "Channel for sale notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void adjustItemPrices(boolean isSaleActive)
    {
        SharedPreferences prefs = getSharedPreferences("SalePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sale_active", isSaleActive);
        editor.apply();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (dailyNotificationTimer != null)
        {
            dailyNotificationTimer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
