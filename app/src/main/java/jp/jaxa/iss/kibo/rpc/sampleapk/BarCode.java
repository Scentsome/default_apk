package jp.jaxa.iss.kibo.rpc.sampleapk;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

public class BarCode {
    public String Name;
    public double loc_x;
    public double loc_y;
    public double loc_z;
    public double qua_x;
    public double qua_y;
    public double qua_z;
    public double qua_w;
    public int QR;
    // suggestion watch pooint
    public double dx=0,dy=0,dz=0;
    public double wp_pos_x;
    public double wp_pos_y;
    public double wp_pos_z;
    public double wp_qua_x;
    public double wp_qua_y;
    public double wp_qua_z;
    public double wp_qua_w;


    public BarCode(String Name,double loc_x,double loc_y,double loc_z,double qua_x,double qua_y,double qua_z,double qua_w,int QR){
        this.Name=Name;
        this.loc_x=loc_x;
        this.loc_y=loc_y;
        this.loc_z=loc_z;
        this.qua_x=qua_x;
        this.qua_y=qua_y;
        this.qua_z=qua_z;
        this.qua_w=qua_w;
        this.QR=QR;
    }

    public Point getWPPoint(){
        return new Point(wp_pos_x,wp_pos_y,wp_pos_z);
    }

    public Point prePos(Point fromPoint){
        Point prePosPoint=new Point(wp_pos_x,wp_pos_y,wp_pos_z) ;
        if (dx!=(double)0) {
            prePosPoint = new Point(fromPoint.getX(), wp_pos_y, wp_pos_z);
        }
        if (dz!=(double)0) {
            prePosPoint = new Point(wp_pos_x, wp_pos_y, fromPoint.getX());
        }
        return prePosPoint;
    }

    public void compute(double wp_distance){
        if (compare(qua_x,qua_y, qua_z, qua_w,(double) 0,(double) 0, (double) 0, (double) 1)){
            dx=-1;
            dy=0;
            dz=0;
        }
        if (compare(qua_x,qua_y, qua_z, qua_w,(double) 0,(double) 0, (double) 1, (double) 0)){
            dx=1;
            dy=0;
            dz=0;
        }
        if (compare(qua_x,qua_y, qua_z, qua_w,(double) 0,(double) -0.7071068, (double) 0, (double) 0.7071068)){
            dx=0;
            dy=0;
            dz=-1;
        }
        if (compare(qua_x,qua_y, qua_z, qua_w,(double) 0,(double) 0.7071068, (double) 0, (double) 0.7071068)){
            dx=0;
            dy=0;
            dz=1;
        }
        wp_pos_x = loc_x + wp_distance * dx;
        wp_pos_y = loc_y + wp_distance * dy;
        wp_pos_z = loc_z + wp_distance * dz;
        wp_qua_x = qua_x;
        wp_qua_y = qua_y;
        wp_qua_z = qua_z;
        wp_qua_w = qua_w;
    }

    private boolean compare(double x1,double y1, double z1, double w1,double x2,double y2, double z2, double w2){
        double dx = Math.max(Math.max(x1 - x2, x2 - x1),0);
        double dy = Math.max(Math.max(y1 - y2, y2 - y1),0);
        double dz = Math.max(Math.max(z1 - z2, z2 - z1),0);
        double dw = Math.max(Math.max(w1 - w2, w2 - w1),0);
        double distance = Math.sqrt(dx*dx+dy*dy+dz*dz+dw*dw);
        return (distance < (double)0.001);
    }

    public Quaternion getWPQuaternion(){
        return new Quaternion((float) wp_qua_x, (float) wp_qua_y, (float) wp_qua_z, (float) wp_qua_w);
    }

    public int getQR(){
        return QR;
    }

    public String getName(){
        return Name;
    }
}
