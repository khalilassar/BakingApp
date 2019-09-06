package com.example.bakingapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void defaultBehaviourMobileVerticalOrientaion() throws InterruptedException {
        onView(withText("BakingApp")).check(matches(isDisplayed()));
        Thread.sleep(1000);
        onView(withId(R.id.fragment_recipe_list_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_recipe_list_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_recipe_detail_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_recipe_detail_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.player_view)).check(matches(isDisplayed()));
        onView(withText("INGREDIENTS")).check(matches(isDisplayed()));
        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }


}
