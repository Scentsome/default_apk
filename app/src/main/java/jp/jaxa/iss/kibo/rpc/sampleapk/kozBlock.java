package jp.jaxa.iss.kibo.rpc.sampleapk;
import gov.nasa.arc.astrobee.types.Point;
public class kozBlock {

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

    public kozBlock(double x_min,double y_min,double z_min,double x_max,double y_max,double z_max){
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


    public Boolean checkBoundary(Point currentPoint){
        Boolean mightCollision;
        double dx = Math.max(Math.max(x_min - currentPoint.getX(), currentPoint.getX() - x_max),0);
        double dy = Math.max(Math.max(y_min - currentPoint.getY(), currentPoint.getY() - y_max),0);
        double dz = Math.max(Math.max(z_min - currentPoint.getZ(), currentPoint.getZ() - z_max),0);
        double distance = Math.sqrt(dx*dx+dy*dy+dz*dz);
        mightCollision = false;
        x_reset=currentPoint.getX();
        y_reset=currentPoint.getY();
        z_reset=currentPoint.getZ();
        if (distance < errorRang) {
            if ((x_min-currentPoint.getX())<errorRang){
                x_reset=x_min-resetDelta;
                mightCollision = true;
            }
            if ((currentPoint.getX()-x_max)<errorRang){
                x_reset=x_max+resetDelta;
                mightCollision = true;
            }
            if ((y_min-currentPoint.getY())<errorRang){
                y_reset=y_min-resetDelta;
                mightCollision = true;
            }
            if ((currentPoint.getY()-y_max)<errorRang){
                y_reset=y_max+resetDelta;
                mightCollision = true;
            }
            if ((z_min-currentPoint.getZ())<errorRang){
                z_reset=z_min-resetDelta;
                mightCollision = true;
            }
            if ((currentPoint.getZ()-z_max)<errorRang){
                z_reset=z_max+resetDelta;
                mightCollision = true;
            }
        }
        return mightCollision;
    }


}


