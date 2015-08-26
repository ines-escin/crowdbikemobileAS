import br.ufpe.cin.contexto.crowdbikemobile.MapDisplayActivity
import pl.polidea.robospock.RoboSpecification

class PostLocationActivitySpec extends RoboSpecification {

    def "Create the activity"(){
        when:
        def activity = new MapDisplayActivity()

        then:
        noExceptionThrown()
    }

}