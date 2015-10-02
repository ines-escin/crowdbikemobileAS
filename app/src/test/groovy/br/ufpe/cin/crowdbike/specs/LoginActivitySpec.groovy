import android.content.Intent
import android.widget.Button
import android.widget.EditText
import br.ufpe.cin.contexto.crowdbikemobile.LoginActivity
import br.ufpe.cin.crowdbike.specs.AuxiliarTestClasses.ServerResponse
import com.example.crowdbikemobile.R
import com.squareup.okhttp.Response
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config


import org.robolectric.shadows.ShadowActivity
import pl.polidea.robospock.RoboSpecification

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 18)
public class LoginActivitySpec extends RoboSpecification
{


    def "System creates the activity"()
    {
        when:
        def activity = new LoginActivity();

        then:
        noExceptionThrown()
    }

    def "Login made with valid credentials"()
    {
        given:

            ServerResponse serverResponse = new ServerResponse();
            def activity = Robolectric.buildActivity(LoginActivity.class).create().start().resume().get()

            String username = "admin"
            String password = "password"

            String url = "http://localhost:8080/project/rest/login?username=" + username + "&pass=" + password;

        when:
            Response response = serverResponse.getResponse(url);

        then:
            response != null && response.code() != 408

    }

    def "Login made with invalid credentials"(){

        given:

        ServerResponse serverResponse = new ServerResponse();

        String username = "admin"
        String password = "password"

        String url = "http://localhost:8080/project/rest/login?username=" + username + "&pass=" + password;

        when:
        Response response = serverResponse.getResponse(url);

        then:
        response == null || response.code() == 408
    }

}