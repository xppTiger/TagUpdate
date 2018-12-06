import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Haiping on 2018/11/30
 */
public class Loader {


    public static  void load(){
        HashMap<String, PageRankNode> nodes = new HashMap<>();

        File tagFile = new File(Properties.tagFile);
        File connectionFile = new File(Properties.connectionFile);
        BufferedReader reader = null;

        long startLoadTime = System.currentTimeMillis();    //获取Load开始时间
        // Load Id and tag information
        try {
            String tempString;
            String[] tempList;
            PageRankNode node;
            String nodeId;
            int line = 1;

            reader = new BufferedReader(new FileReader(tagFile));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                node = new PageRankNode(tempList[0], null, null, null);
               for(int i=1; i<tempList.length; i++){
                   node.addToOldTag(tempList[i], Properties.tagInitialValue);
                   node.addToNewTag(tempList[i], Properties.tagInitialValue);
               }
               nodes.put(node.getNodeId(), node);
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

        //Load connection information
        try {
            String tempString;
            String[] tempList;
            String nodeIdLeft;
            String nodeIdRight;
            double linkValue;

            int line = 1;

            reader = new BufferedReader(new FileReader(connectionFile));
            while ((tempString = reader.readLine()) != null) {
                tempList = tempString.split(",");
                nodeIdLeft = tempList[0];
                nodeIdRight = tempList[1];
                linkValue = Double.valueOf(tempList[2]);
                PageRankNode nodeLeft;
                PageRankNode nodeRight;

               // load node information
                if(nodes.containsKey(nodeIdLeft)){
                    nodeLeft = nodes.get(nodeIdLeft);
                }else{
                    nodeLeft = new PageRankNode(nodeIdLeft, null, null, null);
                    nodes.put(nodeIdLeft, nodeLeft);
                }
                if(nodes.containsKey(nodeIdRight)){
                    nodeRight = nodes.get(nodeIdRight);
                }else{
                    nodeRight = new PageRankNode(nodeIdRight, null, null, null);
                    nodes.put(nodeIdRight, nodeRight);
                }
                nodeLeft.addNeighbor(nodeRight, linkValue);
                nodeRight.addNeighbor(nodeLeft, linkValue);

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

        long endLoadTime = System.currentTimeMillis();    //获取Load结束时间
        System.out.println("程序Load时间：" + (endLoadTime - startLoadTime) + "ms");    //输出程序Load时间

        long startIterationTime = System.currentTimeMillis();    //获取iteration开始时间
        TagUpdateByPageRank pr = new TagUpdateByPageRank(nodes);
        pr.iterate();
        long endIterationTime = System.currentTimeMillis();    //获取iteration结束时间
        System.out.println("程序Iteration时间：" + (endIterationTime - startIterationTime) + "ms");    //输出程序Iteration时间

    }


}
