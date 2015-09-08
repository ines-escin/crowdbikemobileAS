package br.ufpe.cin.util.crowdbikemobile;

/**
 * Created by danielmaida on 03/09/15.
 */
import java.util.UUID;

public class IdGenerator {
    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
