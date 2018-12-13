/**
 * Created by Haiping on 2018/12/12
 */
public class Utilities {

    public static double calculatePcc(double[] arrayX, double[] arrayY){

        if(arrayX.length != arrayY.length){
            System.out.println("unmatched array length");
            return 0;
        }
        if(arrayX.length == 0){
            return 0;
        }
        double avgX = 0;
        double avgY= 0;
        for(int i=0; i<arrayX.length; i++){
            avgX += arrayX[i];
            avgY += arrayY[i];
        }
        avgX /= arrayX.length;
        avgY /= arrayY.length;

        double numerator = 0;
        double denominatorX = 0;
        double denominatorY = 0;
        for(int i=0; i<arrayX.length; i++){
            numerator += (arrayX[i] - avgX)*(arrayY[i] - avgY);
            denominatorX += Math.pow( arrayX[i] - avgX, 2);
            denominatorY += Math.pow( arrayY[i] - avgY, 2);
        }
        double result = 0;
        if((denominatorX * denominatorY) != 0){
            result = numerator/Math.sqrt(denominatorX * denominatorY);
        }else{
            //cos
            numerator = 0;
            denominatorX = 0;
            denominatorY = 0;
            for(int i=0; i<arrayX.length; i++){
                numerator += arrayX[i] * arrayY[i];
                denominatorX += Math.pow(arrayX[i], 2);
                denominatorY += Math.pow(arrayY[i], 2);
                result = numerator/Math.sqrt(denominatorX * denominatorY);
            }
            result = 2*result - 1;
        }

        return result;
    }

}
