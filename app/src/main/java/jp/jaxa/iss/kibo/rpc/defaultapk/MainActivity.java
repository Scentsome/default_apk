package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

//adrian
public class MainActivity extends AppCompatActivity {
    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {

        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()){
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this,loaderCallbackInterface);
        }
        else{
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        test();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                final Bitmap finalbitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalbitmap);
                    }
                });
                return bitmap;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeARCode(bitmap);
        }
    }

    void decodeQRCodoe(Bitmap bitmap){
        Mat mat= new Mat();
        Utils.bitmapToMat(bitmap, mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        Log.d("zencher",""+result);
        textView.setText(result);
    }
    void decodeARCode(Bitmap bitmap){
        Mat mat= new Mat();
        Mat id = new Mat();
        String s="";
        ArrayList<Mat> corners=new ArrayList<>();
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGBA2RGB);
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
        Aruco.detectMarkers(mat,dictionary,corners,id);
        Aruco.drawDetectedMarkers(mat,corners,id);
        for (int i=0; i<6;i++){
            s += (int) id.get(i,0)[0];
            s += ", ";
        }
        textView.setText(s);
        Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(),mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat,resultBitmap);
        imageView.setImageBitmap(resultBitmap);
    }
    void test(){
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute("https://docs.opencv.org/trunk/singlemarkersrejected.jpg");
    }
}
