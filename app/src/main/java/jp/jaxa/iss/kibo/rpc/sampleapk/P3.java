package jp.jaxa.iss.kibo.rpc.sampleapk;

public class P3 {
    public double pos_x;
    public double pos_y;
    public double pos_z;
    public double qua_x;
    public double qua_y;
    public double qua_z;
    private String back_list[] = new String[2];
    public P3(){
        this.pos_x=0.0;
        this.pos_y=0.0;
        this.pos_z=0.0;
        this.qua_x=0.0;
        this.qua_y=0.0;
        this.qua_z=0.0;
    }
    public void set(int QR,String back){
        back_list=back.split(", ");
        switch (back_list[0]) {
            case "pos_x":
                pos_x=Double.parseDouble(back_list[1]);
                break;
            case "pos_y":
                pos_y=Double.parseDouble(back_list[1]);
                break;
            case "pos_z":
                pos_z=Double.parseDouble(back_list[1]);
                break;
            case "qua_x":
                qua_x=Double.parseDouble(back_list[1]);
                break;
            case "qua_y":
                qua_y=Double.parseDouble(back_list[1]);
                break;
            case "qua_z":
                qua_z=Double.parseDouble(back_list[1]);
                break;
            default:
                switch (QR) {
                    case 0:
                        pos_x=Double.parseDouble(back_list[1]);
                        break;
                    case 1:
                        pos_y=Double.parseDouble(back_list[1]);
                        break;
                    case 2:
                        pos_z=Double.parseDouble(back_list[1]);
                        break;
                    case 3:
                        qua_x=Double.parseDouble(back_list[1]);
                        break;
                    case 4:
                        qua_y=Double.parseDouble(back_list[1]);
                        break;
                    case 5:
                        qua_z=Double.parseDouble(back_list[1]);
                        break;
                    default:
                        /// Something Error !!!
                }

        }
    }
}
