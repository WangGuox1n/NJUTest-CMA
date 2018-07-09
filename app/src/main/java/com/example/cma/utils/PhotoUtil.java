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
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.cma.R;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by 王国新 on 2018/6/3.
 */


public class PhotoUtil implements OnPopListener{
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;

    private AppCompatActivity mActivty;
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

    /* 只能在布局为ConstraintLayout的Activity里使用，
     * 并加上android:id="@+id/rl_boot"
     */
    public void showPopupWindow(Context context){
        this.mContext = context;
        this.mActivty = (AppCompatActivity)mContext;
        pop = new ModelPopup(context, PhotoUtil.this,true);

        ConstraintLayout constraintLayout = (ConstraintLayout) mActivty.findViewById(R.id.rl_boot);
        WindowManager.LayoutParams lp = mActivty.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivty.getWindow().setAttributes(lp);
        pop.showAtLocation(constraintLayout, Gravity.BOTTOM, 0, 0);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivty.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivty.getWindow().setAttributes(lp);
            }
        });
        outputImage = null;
    }

    public boolean selectPicture(Intent intent){
        String path=null;
        Uri uri=intent.getData();
        Log.d("Photo",uri.toString());
        //通过uri和selection(倒数第三个
        Cursor cursor= mContext.getContentResolver().query(uri,null,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        if(path!=null){
            Log.d("Photo",path);
            bitmap= BitmapFactory.decodeFile(path);
            outputImage=new File(path);//add new
            return true;
        }else{
            Toast.makeText(mContext,"failed to get image",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public File getOutputImage(){
        return outputImage;
    }

    public File getInvalidPhoto(Context context){
        Log.d("PhotoUtil","未选择图片");
        Uri uri = ViewUtil.getInstance().getUriFromDrawableRes(context,R.mipmap.image_invalid);
        Log.d("Uri",uri.toString());
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
        return file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onBtn1() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mActivty.startActivityForResult(intent, CHOOSE_PHOTO);
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
        mActivty.startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onBtn3() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        mActivty.startActivityForResult(intent,3);
        Toast.makeText(mContext, "你取消了选择", Toast.LENGTH_SHORT).show();
    }

}
