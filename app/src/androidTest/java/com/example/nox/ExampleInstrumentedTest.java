package com.example.nox;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void homeDisplaysRequiredActions() {
        try (ActivityScenario<MainActivity> ignored =
                     ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.btnMiNoche)).check(matches(isDisplayed()));
            onView(withId(R.id.btnAdaptacion)).check(matches(isDisplayed()));
            onView(withId(R.id.btnAcerca)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void adaptationReceivesDurationAndReturnsCancellation() {
        try (ActivityScenario<MainActivity> ignored =
                     ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.btnAdaptacion)).perform(click());
            onView(withId(R.id.countdownText)).check(matches(withText("20:00")));

            Espresso.pressBack();

            onView(withId(R.id.resultMessage))
                    .check(matches(withText(R.string.adaptation_cancelled)));
        }
    }

    @Test
    public void runningAdaptationTimerSurvivesActivityRecreation() throws InterruptedException {
        Intent intent = new Intent(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AdaptationActivity.class);
        intent.putExtra(AdaptationActivity.EXTRA_DURATION_MS, 20 * 60_000L);

        try (ActivityScenario<AdaptationActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnStartTimer)).perform(click());
            Thread.sleep(1_200);

            int[] secondsBeforeRecreation = new int[1];
            scenario.onActivity(activity -> secondsBeforeRecreation[0] =
                    countdownSeconds(activity.findViewById(R.id.countdownText)));

            scenario.recreate();

            int[] secondsAfterRecreation = new int[1];
            scenario.onActivity(activity -> secondsAfterRecreation[0] =
                    countdownSeconds(activity.findViewById(R.id.countdownText)));

            assertTrue(secondsAfterRecreation[0] <= secondsBeforeRecreation[0]);
            assertTrue(secondsBeforeRecreation[0] - secondsAfterRecreation[0] <= 3);
            onView(withId(R.id.btnPauseTimer)).check(matches(isEnabled()));
        }
    }

    private static int countdownSeconds(TextView countdown) {
        String[] parts = countdown.getText().toString().split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}