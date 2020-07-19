package jp.jaxa.iss.kibo.rpc.sampleapk;
import gov.nasa.arc.astrobee.types.Point;
public class kizBlock {

    private double x_min;
    private double y_min;
    private double z_min;
    private double x_max;
    private double y_max;
    private double z_max;
    private double x_reset;
    private double y_reset;
    private double z_reset;
    final double errorRang = 0.3;
    final double resetDelta = 0.6;

    public kizBlock(double x_min,double y_min,double z_min,double x_max,double y_max,double z_max){
        this.x_min=x_min;
        this.y_min=y_min;
        this.z_min=z_min;
        this.x_max=x_max;
        this.y_max=y_max;
        this.z_max=z_max;
    }

    public Point getresetPoint(){
        Point resetPoint;
        resetPoint = new Point(x_reset,y_reset,z_reset);
        return resetPoint;
    }


    public Point checkBoundary(Point currentPoint){
    /// currentPoint must be in the range of kizBlock
    /// Tha is :
    ///   x_min < currentPoint.getX() < x_max
    ///   y_min < currentPoint.getY() < y_max
    ///   z_min < currentPoint.getZ() < z_max
        Boolean mightCollision;

        mightCollision = false;
        x_reset=currentPoint.getX();
        y_reset=currentPoint.getY();
        z_reset=currentPoint.getZ();
        if ((currentPoint.getX()-x_min)<errorRang){
            x_reset=x_min+resetDelta;
            mightCollision = true;
        }
        if ((x_max-currentPoint.getX())<errorRang){
            x_reset=x_max-resetDelta;
            mightCollision = true;
        }
        if ((currentPoint.getY()-y_min)<errorRang){
            y_reset=y_min+resetDelta;
            mightCollision = true;
        }
        if ((y_max-currentPoint.getY())<errorRang){
            y_reset=y_max-resetDelta;
            mightCollision = true;
        }
        if ((currentPoint.getZ()-z_min)<errorRang){
            z_reset=z_min+resetDelta;
            mightCollision = true;
        }
        if ((z_max-currentPoint.getZ())<errorRang){
            z_reset=z_max-resetDelta;
            mightCollision = true;
        }

        if (mightCollision) {
            return getresetPoint();
        }else{
            return null;
        }
    }


}


