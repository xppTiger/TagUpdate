import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haiping on 2018/12/12
 */
public class Similarity {

    public HashMap<String, CFItem> items;
    public HashMap<String[], Double> similarity;

    public Similarity(){
        items = new HashMap<>();
        similarity = new HashMap<String[], Double>();
    }

    public void addItem(CFItem item){
        if(item == null){
            return;
        }
        if(items.containsKey(item.getItemId())){
            System.out.println("existed CF item");
        }else{
            items.put(item.getItemId(), item);
        }

    }

    public void calculateSimilarity(){
        long startTime = System.currentTimeMillis();    //starting time of loading video-user watch file
        for(Map.Entry<String, CFItem> entryItemLeft: items.entrySet()){
            CFItem itemLeft = entryItemLeft.getValue();
            for(Map.Entry<String, CFItem> entryItemRight: items.entrySet()){
                CFItem itemRight = entryItemRight.getValue();
                if(itemLeft.getItemId().equals(itemRight.getItemId()) == false){
                    ArrayList<Double> arrayX = new ArrayList<>();
                    ArrayList<Double> arrayY = new ArrayList<>();
                    for(Map.Entry<String, Double> entryUserLeft: itemLeft.users.entrySet()){
                        for(Map.Entry<String, Double> entryUserRight: itemRight.users.entrySet()){
                            if(entryUserLeft.getKey().equals(entryUserRight.getKey())){
                                arrayX.add(entryUserLeft.getValue());
                                arrayY.add(entryUserRight.getValue());
                            }
                        }
                    }
                    if(arrayX.size() > 0){
                        if(arrayX.size() != arrayY.size()){
                            System.out.println("different user size");
                        }
                        String[] itemKey = {itemLeft.getItemId(), itemRight.getItemId()};
                        double[] array1 = new double[arrayX.size()];
                        double[] array2 = new double[arrayY.size()];
                        for(int k=0; k<arrayX.size(); k++){
                            array1[k] = arrayX.get(k);
                            array2[k] = arrayY.get(k);
                        }
                        double sim = Utilities.calculatePcc(array1, array2)*array1.length/(array1.length + Properties.similarityWeight);
                        similarity.put(itemKey, sim);
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();    //starting time of loading video-user watch file
        System.out.println("Similarity calculation time: "+(endTime - startTime)+"ms");
    }

    public void loadFile(){
        File videoUserScoreFile = new File(Properties.videoUserScoreFile);
        BufferedReader reader = null;

        long startTime = System.currentTimeMillis();    //starting time of loading video-user watch file
        try {
            String tempString;
            String[] tempList;
            CFItem item;
            String itemId;
            String userId;
            double linkValue;
            int line=1;

            reader = new BufferedReader(new FileReader(videoUserScoreFile));
            while ((tempString = reader.readLine()) != null) {
                if(line>1){
                    tempList = tempString.split(",");
                    itemId = tempList[1];
                    userId = tempList[0];
                    linkValue = Double.valueOf(tempList[2]);
                    // no need to add this line if the videoId is not in videos
                    if(Math.abs(linkValue) > Properties.EPSILON){
                        if(items.containsKey(itemId)){
                            item = items.get(itemId);
                        }else{
                            item = new CFItem(itemId);
                            items.put(item.getItemId(), item);
                        }
                        item.addUser(userId, linkValue);
                    }
                }
                line++;
            }
            reader.close();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();    //ending time of loading video-user watch file
        System.out.println("loading time of video-user watch: "+(endTime - startTime)+"ms");
    }

    public void similarityToString(){
        for(Map.Entry<String[], Double>entry: similarity.entrySet()){
            String[] key = entry.getKey();
            double sim = entry.getValue();
            System.out.println(""+key[0]+" | "+key[1]+" | "+sim);
        }
    }

}
