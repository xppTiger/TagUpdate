import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

//        // Update tags for videos and users
//        long startTime = System.currentTimeMillis();    //Starting time
//        TagUpdate.load();
//        long endTime = System.currentTimeMillis();    //Ending time
//        System.out.println("Running time: " + (endTime - startTime) + "ms");

//        // Calculate similarities for video-pairs
//        Similarity similarity = new Similarity();
//        similarity.loadFile();
//        similarity.calculateSimilarity();
//        //similarity.similarityToString();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            System.out.println("test "+index);
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("job "+index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
