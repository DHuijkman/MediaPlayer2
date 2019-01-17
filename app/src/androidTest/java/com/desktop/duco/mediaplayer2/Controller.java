package com.desktop.duco.mediaplayer2;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.desktop.duco.mediaplayer2.demo.EmptyList;
import com.desktop.duco.mediaplayer2.demo.NextPlay;
import com.desktop.duco.mediaplayer2.demo.SelectSong;

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
        //this runs before everything else (even before the start of the activity thats being tested)
    }


    //The GrantPermissionRule Rule allows granting of runtime permissions on Android M (API 23) and above.
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");


    //This rule provides functional testing of a single Activity.
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setup() {
        //@Before runs before every single test thats annotated with @Test(or @SmallTest, @MediumTest or @LargeTest)
    }


    //@Test defines the actuall test
    @Test
    public void testController() {
        int testCount; //amount of tests being run in one set
        int failedTests; //amount of failed tests per set
        List<String> errorMessage = new ArrayList<>(); //list of collected error messages
        List<String> usedTests = new ArrayList<>(); // list of tests that are being used
        List<String> displayMessage = new ArrayList<>(); // list of collected error messages per set
        String command = BuildConfig.SELECTEDTEST; //tests that were selected to run in gradle command. empty by default
        String oldTest = ""; //debug value to keep track of previous ran test

        if (!command.isEmpty()) {// if command doesn't returns an empty string
            command.replaceAll(" ", ""); //remove any white spaces
            String[] comands = command.split(","); //split command on every ',' and put them in a string array called commands
            usedTests.addAll(Arrays.asList(comands)); //add the commands (selected tests) to usedTests
        }
        //uncomment this line if you want to manually add which test should be run (if you don't want to use gradle)
        //usedTests.add("contactsearchtest");

        Map<String, AbstractTest> tests = new HashMap<>(); //make a hashmap with a string as key and an abstract class called AbstractTest
                                                           //all test classes will extend to this abstract class

        //tests.put("selectsong", new SelectSong());  //add your tests here
        //tests.put("nextplay", new NextPlay());
        tests.put("nosongs", new EmptyList());


        int k = 0; // simple counter
        while (k < BuildConfig.MULTIPLIER) { // while counter is smaller than the multiplier specified by either your terminal input or
                                             //the default value of 1. keep running the following code
            testCount = 0;  //set amount of test run this set to 0
            failedTests = 0;    //set amount of failed tests this set to 0
            if (usedTests.isEmpty()) { //if there are no tests specified to run, run all tests available according to the following procedure.
                for (String test : tests.keySet()) { //for each test available do:
                    testCount++; // add 1 to the test ran this set counter
                    try {        // try to after 1/10 of a sec to execute the test and after that reset
                        customSleep(100);
                        tests.get(test).start();
                        tests.get(test).reset();
                    } catch (Error | Exception a) { //if the test fails somewhere add 1 to the failed test counter
                        failedTests++;
                        if (test.equalsIgnoreCase(BuildConfig.SHOWERRORFOR)) {//if in the gradle command it was specified to show the full error
                                                                              //for one test then add full error message. if not then just add the
                                                                              //notify error. this has to be done because a full error can contain
                                                                              // an entire view hierarchy and there is a max size to one error message
                            displayMessage.add(
                                    test + "_TEST has failed, \n" + a + "\n prev test was: " + oldTest + "\n\n");
                        } else {
                            displayMessage.add(test + "_TEST has failed, \n" +
                                    "for a detailed error message run the test on it\'s own" + "\n\n");
                        }
                        tests.get(test).reset(); //even if the test failed reset the state back to origin
                    }
                    oldTest = test; //set current test as previous test
                }
            } else { //if there were specified tests
                for (String sellectedTest : usedTests) { //for each test that was selected
                    testCount++; // add 1 to the test ran this set counter
                    String testname = sellectedTest.toLowerCase(); // get the name of the current test and make it all lower case
                    if (tests.containsKey(testname)) { //if your selected test is contained in the list of available tests try to run
                        try {                          // the test and afterwards reset it
                            customSleep(100);
                            tests.get(testname).start();
                            tests.get(testname).reset();
                        } catch (Error | Exception a) { //if the test fails somewhere add 1 to the failed test counter
                            failedTests++;
                            if (usedTests.size() == 1) { //if the amount of selected tests is 1 display full error message anyway
                                displayMessage.add(testname + "_TEST has failed, \n" + a + "\n\n");

                            } else {
                                if (testname.equalsIgnoreCase(BuildConfig.SHOWERRORFOR)) {//if in the gradle command it was specified to show the full error
                                                                                          //for one test then add full error message. if not then just add the
                                                                                          //notify error. this has to be done because a full error can contain
                                                                                          // an entire view hierarchy and there is a max size to one error message
                                    displayMessage.add(
                                            testname + "_TEST has failed, \n" + a + "\n prev test was: " + oldTest +
                                                    "\n\n");
                                } else {
                                    displayMessage.add(testname + "_TEST has failed, \n" +
                                            "for a detailed error message run the test on it\'s own" +
                                            "\n\n");
                                }
                            }
                            tests.get(testname).reset(); //even if the test failed reset the state back to origin
                        }
                        oldTest = testname; //set current test as previous test
                    } else {  //if selectedtest cant be found in the list of available tests show an error message saying where the list of tests is available
                        displayMessage.add(
                                sellectedTest + " does not exist check if your test is in the list of tests in " +
                                        "android-bouw7\\app\\src\\androidTest\\java\\com\\gobuildit\\bouw7\\Controller.java");
                    }
                }
            }
            if (BuildConfig.DIALOG) { //if in the gradle command dialog was set to true show a screen in between sets with the result and error messages
                final int setsRunAmount = BuildConfig.MULTIPLIER; //amount of sets of tests specified
                final int testPassedAmount = testCount - failedTests; // amount of tests that succeeded this set

                final StringBuilder m = new StringBuilder(); //string builder that builds a string of all the error messages this set
                for (String message : displayMessage) {
                    m.append(message);
                }

                TestResultScreen frag = new TestResultScreen(m.toString(), testCount, k, setsRunAmount, testPassedAmount);
                // create the result screen and
                //launch it for 7 sec. after that it will show a countdown for 3 sec to notify that the next set of test will be run
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

        //after all the sets have been run collect all the errors and make it into one big error message
        if (!errorMessage.isEmpty()) {
            StringBuilder m = new StringBuilder();
            for (String message : errorMessage) {
                m.append(message);
            }
            throw new Error(m.toString());
        }
    }

    //gets the new list of errors and check if they are already contained in the overall list of errors if so dont add them to the list
    // just add an index number to the front of the error. example: during first set you get this error "[1]=-=nosongs_TEST has failed"
                                                          //and during the second set it happens again dont add the same error again just make it
                                                          //[1-2]=-=nosongs_TEST has failed
                                                          //the numbers in between the bracket define in which set they failed
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
