package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
//Ryder's Code
public class MainActivity extends AppCompatActivity {
    ImageView imageView1;
    TextView textView1;

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
        if(!OpenCVLoader.initDebug())
        {
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,loaderCallbackInterface);
        }
        else
        {
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        imageView1 = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView1);
        test();
    }

    private void test(){
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        downloadImageTask.execute("https://docs.opencv.org/trunk/singlemarkersrejected.jpg");
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                final Bitmap finalBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView1.setImageBitmap(finalBitmap);
                    }
                });
                return bitmap;
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeARTag(bitmap);
        }

        void decodeQRCode(Bitmap bitmap){
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap,mat);
            QRCodeDetector qrCodeDetector = new QRCodeDetector();
            String result = qrCodeDetector.detectAndDecode(mat);
            textView1.setText(result);
        }

        void decodeARTag(Bitmap bitmap){
            //AR Tag 圖
            Mat mat = new Mat();
            //解析出來的ID
            Mat id = new Mat();
            //bitmap to mat
            Utils.bitmapToMat(bitmap,mat);
            //Getting AR Tag's dictionary
            Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);
            //remove alpha channel
            Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGBA2BGR);
            //AR Tag's corner List
            ArrayList<Mat> corners = new ArrayList<>();
            Aruco.detectMarkers(mat,dictionary,corners,id);

            String result = "";
            for(int i=0;i<6;i++)
            {
                result += (int)(id.get(i,0)[0]);
                result += ", ";
            }

            textView1.setText(result);

            //Draw AR TAG
            Aruco.drawDetectedMarkers(mat,corners,id);
            Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(),mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat,resultBitmap);
            imageView1.setImageBitmap(resultBitmap);

        }
    }
}

