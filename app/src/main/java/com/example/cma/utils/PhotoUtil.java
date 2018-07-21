package com.example.cma.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.cma.R;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by 王国新 on 2018/6/3.
 *
 * 拍照或从相册里获取图片
 */


public class PhotoUtil implements OnPopListener{
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;

    private AppCompatActivity mActivity;
    private Context mContext;
    private Uri imageUri;
    private File outputImage;
    private Bitmap bitmap;
    private ModelPopup pop;

    private static PhotoUtil photoUtil = null;

    private PhotoUtil(){}

    public static PhotoUtil getInstance(){
        if(null == photoUtil)
            photoUtil = new PhotoUtil();
        return photoUtil;
    }

    /*
     * 只能在布局为ConstraintLayout的Activity里使用，
     * 并为该布局加上命名 android:id="@+id/rl_boot"
     */
    public void showPopupWindow(Context context){
        this.mContext = context;
        this.mActivity = (AppCompatActivity)mContext;
        pop = new ModelPopup(context, PhotoUtil.this,true);

        ConstraintLayout constraintLayout = (ConstraintLayout) mActivity.findViewById(R.id.rl_boot);
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);
        pop.showAtLocation(constraintLayout, Gravity.BOTTOM, 0, 0);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
        outputImage = null;
    }

    public boolean selectPicture(Intent intent){
        String path=null;
        Uri uri=intent.getData();

        Cursor cursor= mContext.getContentResolver().query(uri,null,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        if(path!=null){
            bitmap= BitmapFactory.decodeFile(path);
            outputImage=new File(path);
            return true;
        }else{
            Toast.makeText(mContext,"failed to get image",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public File getOutputImage(){
        return outputImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onBtn1() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivity.startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onBtn2() {
        outputImage = new File(getExternalStorageDirectory(), "output_image.jpg");
        //启动相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(mContext, "com.example.cma.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mActivity.startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onBtn3() {
        Toast.makeText(mContext, "你取消了选择", Toast.LENGTH_SHORT).show();
    }

}
