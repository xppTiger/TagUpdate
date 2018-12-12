import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Haiping on 2018/12/12
 */
public class CFItem {

    private String itemId;
    public HashMap<String, Double> users;

    public CFItem(String itemId){
        this.itemId = itemId;
        users = new LinkedHashMap<>();
    }

    public void addUser(String userId, double score){
        users.put(userId, score);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


}
