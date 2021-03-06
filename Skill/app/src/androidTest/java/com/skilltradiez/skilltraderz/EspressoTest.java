package com.skilltradiez.skilltraderz;
/*
 *    Team15Alpha
 *    AppName: SkillTradiez (Subject to change)
 *    Copyright (C) 2015  Stephen Andersen, Falon Scheers, Elyse Hill, Noah Weninger, Cole Evans
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.test.suitebuilder.annotation.LargeTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onData;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static android.support.test.runner.lifecycle.Stage.RESUMED;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    public static final String STRING_TO_BE_TYPED = "Espresso";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    @Before
    public void deleteLocal() throws IOException {
        MasterController.getUserDB().getLocal().deleteFile();
    }

    @After
    public void deleteLocalAgain() throws IOException {
        MasterController.getUserDB().getLocal().deleteFile();
    }


    // Login.CreateAccount

    @Test
    public void testCreateUser() {
        String username = TestUtilities.getRandomString();
        String email = TestUtilities.getRandomString();

        onView(withId(R.id.usernameField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.emailField)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.beginApp)).perform(click());
        onView(withId(R.id.Go_Profile_Menu)).perform(click());
        onView(withId(R.id.user_name)).check(matches(withText(username)));
    }
    @Test
    public void testModifySkillDetails(){
        //create user and add skill
        testAddSkillz();

        // now go to inventory and edit that skill
        onView(withId(R.id.Go_Profile_Menu)).perform(click());
        onView(withId(R.id.inventory)).perform(click());
        // click on the skill click to edit
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());
        onView(withId(R.id.edit_skill)).perform(click());

        //edit skill description
        onView(withId(R.id.new_skill_description)).perform(typeText("Edited Skill description"), closeSoftKeyboard());
        onView(withId(R.id.add_skill_to_database)).perform(click());

        // go back to inventory and assert that the stuff is set current
        onView(withId(R.id.skill_description)).check(matches(withText("Edited Skill description")));
    }

    // Inventory.AddSkillz + ExamineSkillz (examines after adding)
    public String username;
    public String email;
    @Test
    public void testAddSkillz() {
        username = "Drew" + TestUtilities.getRandomString();
        email = "E" + TestUtilities.getRandomString();
        //login
        onView(withId(R.id.usernameField)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.emailField)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.beginApp)).perform(click());

        // click add skill
        onView(withId(R.id.Go_Make_Skill)).perform(click());

        // set skill properties
        onView(withId(R.id.new_skill_name)).perform(typeText("Sname"), closeSoftKeyboard());
        onView(withId(R.id.new_skill_description)).perform(typeText("Sdesc"), closeSoftKeyboard());
        onView(withId(R.id.radioButton)).perform(click());
        // Set visibility (default is visible
        //onView(withId(R.id.is_visible)).perform(click());

        //Not sure how to set category spinner with espresso..... but it works in the app if you try it yourself
        //onView(withId(R.id.category_spinner)).perform(click());
        //set selection of category spinner somehow


        // add skill to db
        onView(withId(R.id.add_skill_to_database)).perform(click());

        // go back to home screen and then to profile and inventory
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.Go_Profile_Menu)).perform(click());
        onView(withId(R.id.inventory)).perform(click());

        // click on the skill
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        // assert that the stuff is set current
        onView(withId(R.id.skillTitle)).check(matches(withText("Sname")));
        onView(withId(R.id.skill_description)).check(matches(withText("Sdesc")));
    }

    @Test
    // Inventory.RemoveSkillz
    public void testRemoveSkillz() {
        // start by adding a skill
        testAddSkillz();
        onView(withId(R.id.add_remove_skill)).perform(click());
        pressBack();
        // Not sure how espresso really works, this seems like a hack but it works.
        // if an exception is not thrown getting the first item, then there was an item and
        // therefore it wasn't removed successfully.
        try {
            onData(anything()).inAdapterView(allOf(withId(R.id.results_list), isDisplayed())).atPosition(0).check(matches(isDisplayed()));
        } catch (Exception e) {
            return;
        }
        assertTrue(false);
    }

    public String usernameAddfriend;
    public String friendAddfriend;
    public String emailAddfriend;

    @Test
    // Friends.AddFriend
    public void testAddFriend() throws UserAlreadyExistsException {
        friendAddfriend = "temp_f" + TestUtilities.getRandomString();
        usernameAddfriend = "Sam" + TestUtilities.getRandomString();
        emailAddfriend = "E" + TestUtilities.getRandomString();
        //create friend
        DatabaseController.createUser(friendAddfriend);

        //login
        onView(withId(R.id.usernameField)).perform(typeText(usernameAddfriend), closeSoftKeyboard());
        onView(withId(R.id.emailField)).perform(typeText(emailAddfriend), closeSoftKeyboard());
        onView(withId(R.id.beginApp)).perform(click());

        //return home to browse users find friend
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Users)).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText(friendAddfriend), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        //add friend
        onView(withId(R.id.right_button)).perform(click());

        //check that friend was added
        MasterController mc = new MasterController();
        assertTrue(mc.getUserByName(usernameAddfriend).getFriendsList().hasFriend(mc.getUserByName(friendAddfriend)));
    }

    @Test
    // Friends.RemoveFriend
    public void testRemoveFriend() throws UserAlreadyExistsException {
        //add friend first
        testAddFriend();

        //remove friend
        onView(withId(R.id.right_button)).perform(click());

        //check that friend was removed
        MasterController mc = new MasterController();
        assertFalse(mc.getUserByName(usernameAddfriend).getFriendsList().hasFriend(mc.getUserByName(friendAddfriend)));
    }

    @Test
    public void testBrowseOwnInventory() throws UserAlreadyExistsException {
        //create user and add skill
        testAddSkillz();

        // go back to home screen and then to profile and inventory
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.Go_Profile_Menu)).perform(click());
        onView(withId(R.id.inventory)).perform(click());

        // now browsing inventory... can click on a skill and read full details
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        // assert that the stuff is set current
        onView(withId(R.id.skillTitle)).check(matches(withText("Sname")));
        onView(withId(R.id.skill_description)).check(matches(withText("Sdesc")));
    }

    public void editSkillVisibility(){
        //create user and add skill
        testAddSkillz();

        // go back to home screen and then to profile and inventory
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.Go_Profile_Menu)).perform(click());
        onView(withId(R.id.inventory)).perform(click());

        // now browsing inventory... can click on a skill and read full details
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        // edit visibility
        onView(withId(R.id.edit_skill)).perform(click());
        onView(withId(R.id.is_visible)).perform(click());
        onView(withId(R.id.add_skill_to_database)).perform(click());

        // go back to browse all skills and assert that it is not there
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Skillz)).perform(click());

        //search for the skill that shouldn't be there
        onView(withId(R.id.search_bar)).perform(typeText("Sname"), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());

        // Not sure how espresso really works, this seems like a hack but it works.
        // if an exception is not thrown getting the first item, then there was an item and
        // therefore it wasn't removed from visibility successfully.
        try {
            onData(anything()).inAdapterView(allOf(withId(R.id.results_list), isDisplayed())).atPosition(0).check(matches(isDisplayed()));
        } catch (Exception e) {
            return;
        }
        assertTrue(false);
    }

    @Test
    public void testBrowseFriendInventory() throws UserAlreadyExistsException {
        //add friend first
        testAddFriend();

        // Check profile details and click inventory
        onView(withId(R.id.user_name)).check(matches(withText(friendAddfriend)));
        onView(withId(R.id.inventory)).perform(click());
        // now browsing inventory
    }

    @Test
    public void testBrowseFriendProfile() throws UserAlreadyExistsException {
        // create account login and add a friend
        testAddFriend();

        // now browse users and can search and select friend's profile
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Users)).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText(friendAddfriend), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        //check we are now browsing our friend's profile
        onView(withId(R.id.user_name)).check(matches(withText(friendAddfriend)));
    }

    @Test
    public void sortSkillsByCategory(){
        // This works you can see by trying it.... but how to navigate a category spinner UI test with
        // Espresso is not clear and Google has failed me....
    }
    @Test
    public void sortSkillsByText(){
        // create a user and login and create a skill
        testAddSkillz();

        // go back to browse all skills and assert that it is not there
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Skillz)).perform(click());

        //search for the skill that should be there
        onView(withId(R.id.search_bar)).perform(typeText("Sname"), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());

        //ensure that our skill was found after sorting by text
        onData(anything()).inAdapterView(allOf(withId(R.id.results_list), isDisplayed())).atPosition(0).check(matches(isDisplayed()));
    }

    @Test
    public void sortFriendInventoryByText() throws UserAlreadyExistsException {
        // create a user and login and add friend
        String tradeFriend = "Al" + TestUtilities.getRandomString();
        String tradeUsername = "Bo" + TestUtilities.getRandomString();
        String tradeEmail = "Em" + TestUtilities.getRandomString();
        //create friend, make sure they have a skill, add them
        DatabaseController.createUser(tradeFriend);
        GeneralMenuActivity g_activity = (GeneralMenuActivity) getActivityInstance();
        g_activity.getMasterController().makeNewSkill("miscellaneous", "stupid sort test", "High", true, new ArrayList<Image>());

        //login and add friend
        onView(withId(R.id.usernameField)).perform(typeText(tradeUsername), closeSoftKeyboard());
        onView(withId(R.id.emailField)).perform(typeText(tradeEmail), closeSoftKeyboard());
        onView(withId(R.id.beginApp)).perform(click());

        //return home to browse users find friend
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Users)).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText(tradeFriend), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        //add friend
        onView(withId(R.id.right_button)).perform(click());
        onView(withId(R.id.inventory)).perform(click());
        // sort their inventory by text for the skill we know they have
        onView(withId(R.id.search_bar)).perform(typeText("stupid sort test"), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());
        //ensure that the skill was found after sorting by text
        onData(anything()).inAdapterView(allOf(withId(R.id.results_list), isDisplayed())).atPosition(0).check(matches(isDisplayed()));
    }
    public String tradeFriend;
    public String tradeUsername;
    public String tradeEmail;
    /* this test ALSO is a test for Notified of Trade Request since we check the trade updates page to see
    if there is any new trade activity after trade started
    */
    @Test
    public void testStartTrade() throws UserAlreadyExistsException {
        tradeFriend = "Al" + TestUtilities.getRandomString();
        tradeUsername = "Bo" + TestUtilities.getRandomString();
        tradeEmail = "Em" + TestUtilities.getRandomString();
        //create friend, make sure they have a skill, add them
        DatabaseController.createUser(tradeFriend);
        GeneralMenuActivity g_activity = (GeneralMenuActivity) getActivityInstance();
        g_activity.getMasterController().makeNewSkill("miscellaneous", "stupid trade test", "High", true, new ArrayList<Image>());

        //login and add friend
        onView(withId(R.id.usernameField)).perform(typeText(tradeUsername), closeSoftKeyboard());
        onView(withId(R.id.emailField)).perform(typeText(tradeEmail), closeSoftKeyboard());
        onView(withId(R.id.beginApp)).perform(click());

        //return home to browse users find friend
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.All_Users)).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText(tradeFriend), closeSoftKeyboard());
        onView(withId(R.id.search_bar_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        //add friend
        onView(withId(R.id.right_button)).perform(click());

        // start trade
        onView(withId(R.id.left_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.other_invList)).atPosition(0).perform(click());
        onView(withId(R.id.sendTrade)).perform(click());
        // toast made that trade request was sent (to be pushed when online if not)

        //return home and view trade history
        onView(withId(R.id.Go_Home_Menu)).perform(click());
        onView(withId(R.id.Trade_History)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.results_list)).atPosition(0).perform(click());

        // check that a trade was added in trade activity notifications page
        onData(anything()).inAdapterView(allOf(withId(R.id.requestList), isDisplayed())).atPosition(0).check(matches(isDisplayed()));

    }
    Activity currentActivity;
    // helper function for testStartTrade
    public Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity;
    }

}