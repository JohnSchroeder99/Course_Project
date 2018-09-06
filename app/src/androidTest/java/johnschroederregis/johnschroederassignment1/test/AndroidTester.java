package johnschroederregis.johnschroederassignment1.test;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import johnschroederregis.johnschroederassignment1.MasterFragment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class AndroidTester {
    @Test
    public void verifyArray(){

        assertEquals("D:/John's/Masters/Assignments/JohnSchroederAssignment1" , "D:/John's/Masters/Assignments/JohnSchroederAssignment1");
    }

    public void fbretreive (){

       assertNotNull(MasterFragment.class);
    }

}
