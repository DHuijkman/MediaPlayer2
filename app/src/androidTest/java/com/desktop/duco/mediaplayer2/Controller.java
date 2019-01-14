package com.desktop.duco.mediaplayer2;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.desktop.duco.mediaplayer2.tests.MusicListSelectSongTest;
import com.desktop.duco.mediaplayer2.tests.NextSongUITest;
import com.desktop.duco.mediaplayer2.tests.NoSongs;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.desktop.duco.mediaplayer2.util.CustomSleep.customSleep;
import static com.desktop.duco.mediaplayer2.util.GetTestActivity.getActivityInstance;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class Controller {
    @BeforeClass
    public static void startUp() {

    }

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {

    }

    @Test
    public void testController() throws Throwable {
        int testCount;
        int failedTests;
        List<String> errorMessage = new ArrayList<>();
        List<String> usedTests = new ArrayList<>();
        List<String> displayMessage = new ArrayList<>();
        String command = BuildConfig.SELECTEDTEST;
        String oldTest = "";

        if (!command.isEmpty()) {
            command.replaceAll(" ", "");
            String[] comands = command.split(",");
            usedTests.addAll(Arrays.asList(comands));
        }
        //uncomment this line if you want to manually add which test should be run (if you dont want to use gradle)
        //usedTests.add("contactsearchtest");

        Map<String, AbstractTest> tests = new HashMap<>();

       // tests.put("musiclistselectsongtest", new MusicListSelectSongTest());
        tests.put("nextsonguitest", new NextSongUITest());
       // tests.put("nosongs", new NoSongs());


        int k = 0;
        while (k < BuildConfig.MULTIPLIER) {
            testCount = 0;
            failedTests = 0;
            if (usedTests.isEmpty()) {
                for (String test : tests.keySet()) {
                    testCount++;
                    try {
                        customSleep(100);
                        tests.get(test).start();
                        tests.get(test).reset();
                    } catch (Error | Exception a) {
                        failedTests++;
                        if (test.equalsIgnoreCase(BuildConfig.SHOWERRORFOR)) {
                            displayMessage.add(
                                    test + "_TEST has failed, \n" + a + "\n prev test was: " + oldTest + "\n\n");
                        } else {
                            displayMessage.add(test + "_TEST has failed, \n" +
                                    "for a detailed error message run the test on it\'s own" + "\n\n");
                        }
                        tests.get(test).reset();
                    }
                    oldTest = test;
                }
            } else {
                for (String sellectedTest : usedTests) {
                    testCount++;
                    String testname = sellectedTest.toLowerCase();
                    if (tests.containsKey(testname)) {
                        try {
                            customSleep(100);
                            tests.get(testname).start();
                            tests.get(testname).reset();
                        } catch (Error | Exception a) {
                            failedTests++;
                            if (usedTests.size() == 1) {
                                displayMessage.add(testname + "_TEST has failed, \n" + a + "\n\n");

                            } else {
                                if (testname.equalsIgnoreCase(BuildConfig.SHOWERRORFOR)) {
                                    displayMessage.add(
                                            testname + "_TEST has failed, \n" + a + "\n prev test was: " + oldTest +
                                                    "\n\n");
                                } else {
                                    displayMessage.add(testname + "_TEST has failed, \n" +
                                            "for a detailed error message run the test on it\'s own" +
                                            "\n\n");
                                }
                            }
                            tests.get(testname).reset();
                        }
                        oldTest = testname;
                    } else {
                        displayMessage.add(
                                sellectedTest + " does not exist check if your test is in the list of tests in " +
                                        "android-bouw7\\app\\src\\androidTest\\java\\com\\gobuildit\\bouw7\\Controller.java");
                    }
                }
            }
            if (BuildConfig.DIALOG) {
                final int setsRunAmount = BuildConfig.MULTIPLIER;
                final int testPassedAmount = testCount - failedTests;

                final StringBuilder m = new StringBuilder();
                for (String message : displayMessage) {
                    m.append(message);
                }

                TestResultScreen frag = new TestResultScreen(m.toString(), testCount, k, setsRunAmount, testPassedAmount);

                (getActivityInstance()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_screen, frag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();

                customSleep(7000);
                frag.countDown(3);
                customSleep(1000);
                frag.countDown(2);
                customSleep(1000);
                frag.countDown(1);
                customSleep(1000);

                getActivityInstance().getSupportFragmentManager().popBackStack();
            }

            errorMessage = removeDuplicates(errorMessage, displayMessage, k + 1);
            displayMessage.clear();
            k++;
        }


        if (!errorMessage.isEmpty()) {
            StringBuilder m = new StringBuilder();
            for (String message : errorMessage) {
                m.append(message);
            }
            throw new Error(m.toString());
        }
    }

    private List<String> removeDuplicates(List<String> searchList, List<String> newList, int setNumber) {
        List<String> cleanedList = new ArrayList<>();
        String[] splitResult;
        String setNumbers;
        String error;
        boolean add;

        if (searchList.isEmpty()) {
            for (String item : newList) {
                cleanedList.add("[" + setNumber + "]" + "=-=" + item);
            }
            return cleanedList;
        }
        for (String searchListItem : searchList) {
            add = true;
            splitResult = searchListItem.split("=-=");
            setNumbers = splitResult[0].replaceAll("[\\[\\]]", "");
            error = splitResult[1];

            for (Iterator<String> iterator = newList.iterator(); iterator.hasNext(); ) {
                String value = iterator.next();
                if (error.equals(value)) {
                    setNumbers = setNumbers + "-" + setNumber;
                    cleanedList.add("[" + setNumbers + "]" + "=-=" + error);
                    iterator.remove();
                    add = false;
                }
            }

            if (add) {
                cleanedList.add("[" + setNumber + "]" + "=-=" + error);
            }
        }
        for (String item : newList) {
            cleanedList.add("[" + setNumber + "]" + "=-=" + item);
        }

        return cleanedList;
    }

    @After
    public void tearDown() {

    }

}
