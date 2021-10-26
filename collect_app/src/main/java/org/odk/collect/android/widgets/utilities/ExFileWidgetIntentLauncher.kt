package org.odk.collect.android.widgets.utilities

import android.app.Activity
import org.javarosa.form.api.FormEntryPrompt
import org.odk.collect.android.R
import org.odk.collect.android.utilities.ExternalAppIntentProvider
import org.odk.collect.androidshared.ui.ToastUtils.showLongToast
import org.odk.collect.androidshared.utils.IntentLauncher
import java.lang.Error
import java.lang.Exception

object ExFileWidgetIntentLauncherImpl : ExFileWidgetIntentLauncher {
    override fun launch(
        intentLauncher: IntentLauncher,
        activity: Activity,
        requestCode: Int,
        externalAppIntentProvider: ExternalAppIntentProvider,
        formEntryPrompt: FormEntryPrompt
    ) {
        try {
            val intent = externalAppIntentProvider.getIntentToRunExternalApp(formEntryPrompt)
            val intentWithoutDefaultCategory =
                externalAppIntentProvider.getIntentToRunExternalAppWithoutDefaultCategory(
                    formEntryPrompt,
                    activity.packageManager
                )

            intentLauncher.launchForResult(
                activity, intent, requestCode
            ) {
                intentLauncher.launchForResult(
                    activity, intentWithoutDefaultCategory, requestCode
                ) {
                    showLongToast(activity, getErrorMessage(formEntryPrompt, activity))
                }
            }
        } catch (e: Exception) {
            showLongToast(activity, e.message!!)
        } catch (e: Error) {
            showLongToast(activity, e.message!!)
        }
    }

    private fun getErrorMessage(formEntryPrompt: FormEntryPrompt, activity: Activity): String {
        val customErrorMessage = formEntryPrompt.getSpecialFormQuestionText("noAppErrorString")
        return customErrorMessage ?: activity.getString(R.string.no_app)
    }
}

interface ExFileWidgetIntentLauncher {
    fun launch(
        intentLauncher: IntentLauncher,
        activity: Activity,
        requestCode: Int,
        externalAppIntentProvider: ExternalAppIntentProvider,
        formEntryPrompt: FormEntryPrompt
    )
}
