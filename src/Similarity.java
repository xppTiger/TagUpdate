import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Haiping on 2018/12/12
 */
public class Similarity {

    public ArrayList<CFItem> items;

    public Similarity(){

    }

    public void addItem(CFItem item){
        boolean found = false;
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getItemId().equals(item.getItemId())){
                System.out.println("duplicated items in Similarity");
                found = true;
                break;
            }
        }
        if(found == false){
            items.add(item);
        }
    }

    public void calculateSimilarity(){
        for(int i=0; i<items.size(); i++){
            CFItem itemLeft = items.get(i);
            for(int j=i+1; j<items.size(); j++){
                CFItem itemRight = items.get(j);
                int usersShared = 0;
                double avgLeft = 0;
                double avgRight = 0;
                for(Map.Entry<String, Double> entry: itemLeft.users.entrySet()){

                }

            }
        }
    }



}
