package com.example.cma.ui.quality_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.quality_system.QualityManual;
import com.example.cma.utils.DownloadUtil;

public class QualityManual_See extends AppCompatActivity implements View.OnClickListener{

    private QualityManual temp;
    Toolbar toolbar;
    String function;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_manual__see);
        Intent intent=getIntent();
        temp=(QualityManual)intent.getSerializableExtra("temp");
        function=intent.getStringExtra("function");
        init();
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
         TextView attachment=(TextView)findViewById(R.id.file_text);
        attachment.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.file_text:
                String filename=temp.getFile();
                String path="http://119.23.38.100:8080/cma/"+function+"/getCurrentFile";
                String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/质量体系";
                final ProgressDialog progressDialog=new ProgressDialog(QualityManual_See.this);
                progressDialog.setTitle("正在下载中");
                progressDialog.setMessage("请稍等....");
                progressDialog.setCancelable(true);
                progressDialog.show();
                DownloadUtil.getInstance().download(path,fileUrl,filename,new DownloadUtil.OnDownloadListener() {

                    @Override
                    public void onDownloadSuccess(String path) {
                        progressDialog.dismiss();
                        Toast.makeText(QualityManual_See.this, "下载成功！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {
                        Toast.makeText(QualityManual_See.this, "下载失败！", Toast.LENGTH_SHORT).show();

                    }
                });
                break;


            default:
                break;
        }

    }
    void init()
    {

            if(temp==null)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManual_See.this, "没有线性！", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("为空！！！！","!!!!!");
                return ;
            }

            TextView t1=(TextView)findViewById(R.id.id_text);
            TextView t2=(TextView)findViewById(R.id.name_text);
            TextView t4=(TextView)findViewById(R.id.time_text);
            TextView t5=(TextView)findViewById(R.id.person_text);
            TextView t6=(TextView)findViewById(R.id.content_text);
            t1.setText(temp.getFileId());
            t2.setText(temp.getFileName());
            t4.setText(temp.getModifyTime());
            t5.setText(temp.getModifier());
            t6.setText(temp.getModifyContent());
    }

}
