import android.support.v7.appcompat.R
import android.widget.Spinner
import br.ufpe.cin.contexto.crowdbikemobile.MapDisplayActivity
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 18)
public class PostLocationActivitySpec extends RoboSpecification {

    def "System creates the activity"(){
        when:
        def activity = new MapDisplayActivity()

        then:
        noExceptionThrown()
    }


    def "Location post request"(){

        given:

        def activity = Robolectric.buildActivity(MapDisplayActivity.class).create().get()
        Spinner occurrenceSpinner = (Spinner) activity.findViewById(com.example.crowdbikemobile.R.id.menu_spinner)
        occurrenceSpinner.setSelection(1)
        activity.latitude = "-8.032943"
        activity.longitude = "-34.901065"

        when:
        activity.postInfo();

        then:
        noExceptionThrown();
    }

}