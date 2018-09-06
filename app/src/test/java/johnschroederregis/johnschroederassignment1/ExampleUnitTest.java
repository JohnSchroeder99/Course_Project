package johnschroederregis.johnschroederassignment1;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 *
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



public class ExampleUnitTest {



    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getfbData(){

        assertNotNull("not null", "" );

    }


    @Test
    public void fbLink(){

        assertEquals("https://contacts-course-project.firebaseio.com", "https://contacts-course-project.firebaseio.com" );

    }
}