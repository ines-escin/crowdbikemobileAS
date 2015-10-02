import br.ufpe.cin.contexto.crowdbikemobile.MapDisplayActivity
import br.ufpe.cin.contexto.crowdbikemobile.RegisterActivity
import br.ufpe.cin.crowdbike.specs.AuxiliarTestClasses.ServerResponse
import com.squareup.okhttp.Response
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 18)
public class RegisterActivitySpec extends RoboSpecification
{
    def "System creates the activity"(){
        when:
        def activity = new RegisterActivity()

        then:
        noExceptionThrown()
    }

    def "Registration with valid data"()
    {
        given:

            String email = "ealp@gmail.com"
            String username = "edgar"
            String password = "allanpoe"
            String confirmPassword = "allanpoe"
            String url = "http://localhost:8080/project/rest/login?username=" + username + "&pass=" + password + "&email=" + email;

        expect:
            password == confirmPassword

        when:
            ServerResponse serverResponse = new ServerResponse();
            Response response = serverResponse.getResponse(url)

        then:
            response != null && response.code() != 408
    }

}