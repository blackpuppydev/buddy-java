package th.co.ais.genesis.module.buddy.gms;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;


public class FaceDetect {

    private final static String TAG = "FaceDetect";

    private Context mContext;
    private FaceDetector detector;

    public FaceDetect(Context mContext) {
        this.mContext = mContext;


    }


    public void initFaceDetect() {

        Log.d(TAG, "initFaceDetect");

        this.detector = new FaceDetector.Builder(mContext)
                .setProminentFaceOnly(true)
                .build();


    }


    public void startDetectFace() {


        Frame frame = null;
        SparseArray<Face> faces = this.detector.detect(frame);

//        detector.setProcessor(
//                new LargestFaceFocusingProcessor(
//                        detector,
//                        new FaceTracker()));
//
//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        CameraSource cameraSource = new CameraSource.Builder(mContext, detector)
//                .setFacing(CameraSource.CAMERA_FACING_FRONT)
//                .setRequestedPreviewSize(320, 240)
//                .build()
//                .start();



    }

    public void stopDetect(){

    }


}
