package com.example.baking_app;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.baking_app.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.anything;

/**
 * some instructions by https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/ PATRICK BACON
 */

public class RecyclerViewClickTest extends ActivityTestRule<ItemListActivity> {

    public RecyclerViewClickTest() {
        super(ItemListActivity.class);
    }

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Rule
    public ActivityTestRule<ItemListActivity> mActivityTestRule =
            new ActivityTestRule<>(ItemListActivity.class);

    @Test
    public void idlingResourceTest() {
        //check if there is a card with first recipe name
        onView(withRecyclerView(R.id.item_list).atPosition(0)).check(matches(hasDescendant(withText("Nutella Pie"))));

        //perform click in the first card
        onView(withRecyclerView(R.id.item_list).atPosition(0)).perform(click());

        //check if there is a recyclerview with ingredients is displayer
        onView(withId(R.id.ingredients_list)).check(matches(isDisplayed()));

        //check if there is a recyclerview with steps is displayer
        onView(withId(R.id.steps_list)).check(matches(isDisplayed()));

        //perform click in the first step
        onView(withRecyclerView(R.id.steps_list).atPosition(0)).perform(click());

        //check if there is a TextView with step's description
        onView(withId(R.id.fragment_step_description)).check(matches(isDisplayed()));
    }


    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
