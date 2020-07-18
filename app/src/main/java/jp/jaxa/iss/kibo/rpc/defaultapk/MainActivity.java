package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    //William
    ImageView imageview;
    TextView textview;

    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {

        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        imageview = findViewById(R.id.qr);
        textview = findViewById(R.id.textView);
        if (!OpenCVLoader.initDebug()) {
            boolean success =
                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0
                            , this, loaderCallbackInterface);
        }else{
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        test();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls){
            String url = urls[0];
            Bitmap bitmap = null;
            try{
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                final Bitmap finalBitmap = bitmap;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageview.setImageBitmap(finalBitmap);
                    }
                });
            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected  void  onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeARcode(bitmap);

        }
    }
    void decodeQRcode (Bitmap bitmap){
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result =  qrCodeDetector.detectAndDecode(mat);
        textview.setText(result);

    }

    void decodeARcode (Bitmap bitmap){
        Mat mat = new Mat();
        Mat ids = new Mat();
        ArrayList<Mat> corners = new ArrayList<>();
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat, Imgproc. COLOR_RGBA2RGB);
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat, dictionary, corners, ids);
        textview.setText((ids.get(0,0)[0]) + "");
        String result = "";
        for (int iloop = 0 ; iloop < 6; iloop ++){

            int ID = (int)(ids.get(iloop,0)[0]);
            result = result + "," + ID;
        }



        textview.setText(result);
        Aruco.drawDetectedMarkers(mat, corners, ids);

        Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, resultBitmap);
        imageview.setImageBitmap(resultBitmap);
    }
    void test(){
        DownloadImageTask task = new DownloadImageTask();
        task.execute("https://docs.opencv.org/trunk/singlemarkersoriginal.jpg");
    }
}

