import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.ufpe.cin.br.adapter.crowdbikemobile.AdapterOcurrence
import br.ufpe.cin.br.adapter.crowdbikemobile.Attributes
import br.ufpe.cin.br.adapter.crowdbikemobile.Entity
import br.ufpe.cin.br.adapter.crowdbikemobile.Metadata
import br.ufpe.cin.contexto.crowdbikemobile.MapDisplayActivity
import com.google.gson.Gson
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import pl.polidea.robospock.RoboSpecification

class PostLocationActivitySpec extends RoboSpecification {

    def "System creates the activity"(){
        when:
        def activity = new MapDisplayActivity()

        then:
        noExceptionThrown()
    }

    def "Successful post of the current location"(){


    }

    def "User submits an occurrence with a location defined manually and with valid coordinate values" (){

    }

}