import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.ufpe.cin.contexto.crowdbikemobile.MapDisplayActivity
import pl.polidea.robospock.RoboSpecification

class PostLocationActivitySpec extends RoboSpecification {

    def "Create the activity"(){
        when:
        def activity = new MapDisplayActivity()

        then:
        noExceptionThrown()
    }

    def "User submits an occurrence with his current location"(){
        given:
            //The user is connected to internet
        when:
            //The user submits the current location
        then:
            //The occurrence must be successfully posted to the server
    }

    def "User submits an occurrence with a location defined manually and with valid coordinate values" (){
        given:
            //the user is connected to internet
        when:
            //the user submits the current location with valid coordinate values
        then:
            //The occurrence must be successfully posted to the server
    }

}