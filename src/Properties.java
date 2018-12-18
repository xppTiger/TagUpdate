/**
 * Created by Haiping on 2018/11/30
 */
public class Properties {

    public static String videoSimilarityFile = "/home/haiping/TagUpdate/tmp_video_video_sim_20181210_2.csv";
    public static String videoTagFile = "/home/haiping/TagUpdate/tmp_video_tag_20181210.csv";
    public static String videoUserScoreFile = "/home/haiping/TagUpdate/tmp_video_device_score_20181210.csv";

//    public static String videoSimilarityFile = "/home/haiping/TagUpdate/testSimilarity.txt";
//    public static String videoTagFile = "/home/haiping/TagUpdate/testTag.txt";
//    public static String videoUserScoreFile = "/home/haiping/TagUpdate/testScore.txt";

//    public static String videoSimilarityFile = "d:\\media_tag\\testSimilarity.txt";
//    public static String videoTagFile = "d:\\media_tag\\testTag.txt";
//    public static String videoUserScoreFile = "d:\\media_tag\\testScore.txt";

    public static int videoTagUpdateIterationTimes = 3;
    public static int userTagUpdateIterationTimes = 1;
    public static double categoryInitialValue = 1;
    public static double keywordInitialValue = 1;
    public static boolean lpa = true;
    public static double EPSILON = 0.000001;
    public static double similarityWeight = 100;
    public static int threadPoolSize = 12;

}
