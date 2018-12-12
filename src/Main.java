import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();    //获取开始时间
        Loader.load();
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("Running time: " + (endTime - startTime) + "ms");    //输出程序运行时间


        /*
        double[] tagValue = {1};
        HashMap<String, Double> example = new LinkedHashMap<>();
        String str1 = "device1";
        String str2 = "device2";
        String str3 = "device3";
        example.put(str1, 5.6);
        example.put(str2, 3.0);
        example.put(str3, 89.0);
        example.put("device1", 100.0+5.6);
        System.out.println("device1: "+example.get("device1"));
        HashMap<String, Double> tag = new LinkedHashMap<>();
        tag = example;
//        for (Iterator<Map.Entry<String, Double>> it = example.entrySet().iterator(); it.hasNext();){
//            Map.Entry<String, Double> item = it.next();
//            //... todo with item
//            it.remove();
//        }
        example = new LinkedHashMap<>();
        System.out.println("example.size = "+example.size()+", tag.size = "+tag.size());
        for(Map.Entry<String, Double> entry: tag.entrySet()){
            System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
        }
        */
    }

}
