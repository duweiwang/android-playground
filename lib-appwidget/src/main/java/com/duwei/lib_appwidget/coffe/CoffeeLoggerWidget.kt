package com.duwei.lib_appwidget.coffe

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.duwei.lib_appwidget.R

/**
 * Implementation of App Widget functionality.
 */
class CoffeeLoggerWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val intent = Intent(context.applicationContext, CoffeeQuotesService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        context.startService(intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val coffeeLoggerPersistence = CoffeeLoggerPersistence(context)

            val widgetText = coffeeLoggerPersistence.loadTitlePref().toString()

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.demo_coffee_logger_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            views.setOnClickPendingIntent(
                R.id.ristretto_button,
                getPendingIntent(context, CoffeeTypes.RISTRETTO.grams)
            )
            views.setOnClickPendingIntent(
                R.id.espresso_button,
                getPendingIntent(context, CoffeeTypes.ESPRESSO.grams)
            )
            views.setOnClickPendingIntent(
                R.id.long_button,
                getPendingIntent(context, CoffeeTypes.LONG.grams)
            )
            views.setTextViewText(R.id.coffee_quote, getRandomQuote(context))

            val limit = coffeeLoggerPersistence.getLimitPref(appWidgetId)

            val background = if (limit <= widgetText.toInt())
                R.drawable.background_overlimit
            else
                R.drawable.background

            views.setInt(R.id.widget_layout, "setBackgroundResource", background)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, value: Int): PendingIntent {
            val intent = Intent(context, WidgetMainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            intent.action = Constants.ADD_COFFEE_INTENT
            intent.putExtra(Constants.GRAMS_EXTRA, value)
            return PendingIntent.getActivity(context, value, intent, 0)
        }

        private fun getRandomQuote(context: Context): String {
            val quotes = context.resources.getStringArray(R.array.coffee_texts)
            val rand = Math.random() * quotes.size
            return quotes[rand.toInt()].toString()
        }

    }
}

