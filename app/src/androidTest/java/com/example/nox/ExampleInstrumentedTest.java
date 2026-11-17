package com.example.nox;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
}