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

    //ethan
    ImageView imgView;
    TextView txtView;
    LoaderCallbackInterface loaderCallbackInterface = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
        }
        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imageView2);
        txtView = findViewById(R.id.textView2);
        //Make sure OpenCV is loaded
        if (!OpenCVLoader.initDebug()) {
            boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, loaderCallbackInterface);
        } else {
            loaderCallbackInterface.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        test();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
                        imgView.setImageBitmap(finalBitmap);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            decodeARTag(bitmap);
        }

        void decodeQRcode(Bitmap bitmap) {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            QRCodeDetector qrCodeDetector = new QRCodeDetector();
            String result = qrCodeDetector.detectAndDecode(mat);
            txtView.setText(result);
        }
    }

    void decodeARTag(Bitmap bitmap) {
        Mat mat = new Mat();                                           //AR TAg 圖
        Mat ids = new Mat();                                           //解析出來的ID
        ArrayList<Mat> corner = new ArrayList<>();                     //解析出來的corner list
        Utils.bitmapToMat(bitmap, mat);                                 //bitmap to mat
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);              //去掉alpha通道
        Dictionary dictionary = Dictionary.get(Aruco.DICT_6X6_250);    //取得 Aruco 的字典
        Aruco.detectMarkers(mat, dictionary, corner, ids);


        String idlist = "";
        for (int i = 0; i <= 5; i++) {
            int id = (int) (ids.get(i, 0)[0]);
            idlist += id+" ";
        }
        txtView.setText(idlist);


        Aruco.drawDetectedMarkers(mat, corner, ids);
        Bitmap resultbitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, resultbitmap);
        imgView.setImageBitmap(resultbitmap);
    }

    void test() {
        DownloadImageTask task = new DownloadImageTask();
        task.execute("https://docs.opencv.org/trunk/singlemarkersoriginal.jpg");
    }

}

