package ia.ankherth.veniceai

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de integración para la actividad MainActivity.
 * Verifica que el WebView se carga correctamente.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun webViewIsDisplayed() {
        // Verificar que el WebView está visible
        onView(withId(R.id.webView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun activityTitle() {
        // Verificar que la actividad tiene el título correcto
        val activity = activityRule.activity
        assert(activity.title.isNotEmpty())
    }
}

