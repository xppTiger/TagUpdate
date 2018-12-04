import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haiping on 2018/11/28
 */
public class Connection {

    private int numUser;
    private int numVideo;
    public HashMap<String[], Double> connection;

    public Connection(){
        numUser = 0;
        numVideo = 0;
        connection = new HashMap<>();
    }

    public void addConnection(String userId, String videoId, double score){
        String[] key = new String[2];
        key[0] = userId;
        key[1] = videoId;
        Double value = new Double(score);
        this.connection.put(key, value);
    }

    public double getScore(String userId, String videoId){
        String[] key = new String[2];
        key[0] = userId;
        key[1] = videoId;

        for (Map.Entry<String[], Double> entry : connection.entrySet()) {
            String[] connectionKey = (String[]) entry.getKey();
            if(connectionKey[0].equals(key[0])&&(connectionKey[1].equals(key[1]))){
                return (Double) entry.getValue();
            }
        }
        return 0;
    }

}
