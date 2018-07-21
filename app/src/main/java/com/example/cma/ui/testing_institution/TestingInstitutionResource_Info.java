package com.example.cma.ui.testing_institution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.testing_institution.TestingInstitutionResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestingInstitutionResource_Info extends AppCompatActivity {
    Toolbar toolbar;
    TestingInstitutionResource testingInstitutionResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_institution_resource__info);
        init();

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //对 编辑 按钮监听
        Button button2=(Button)findViewById(R.id.edit_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TestingInstitutionResource_Info.this,TestingInstitutionResource_Modify.class);
                intent.putExtra("test",testingInstitutionResource);
                startActivity(intent);
            }
        });

    }
    public void initView(){
        TextView textView1=(TextView) findViewById(R.id.text_view1_1);
        textView1.setText(String.valueOf(testingInstitutionResource.getTotalNumber()));

        TextView textView2=(TextView) findViewById(R.id.text_view2_1);
        textView2.setText(String.valueOf(testingInstitutionResource.getSeniorProfessionalTitle()));

        TextView textView3=(TextView) findViewById(R.id.text_view3_1);
        textView3.setText(String.valueOf(testingInstitutionResource.getIntermediateProfessionalTitle()));

        TextView textView4=(TextView) findViewById(R.id.text_view4_1);
        textView4.setText(String.valueOf(testingInstitutionResource.getPrimaryProfessionalTitle()));

        TextView textView5=(TextView) findViewById(R.id.text_view5_1);
        textView5.setText(String.valueOf(testingInstitutionResource.getFixedAssets()));

        TextView textView6=(TextView) findViewById(R.id.text_view6_1);
        textView6.setText(String.valueOf(testingInstitutionResource.getEquipmentNumber()));

        TextView textView7=(TextView) findViewById(R.id.text_view7_1);
        textView7.setText(String.valueOf(testingInstitutionResource.getFloorSpace()));

        TextView textView8=(TextView) findViewById(R.id.text_view8_1);
        textView8.setText(String.valueOf(testingInstitutionResource.getStableArea()));

        TextView textView9=(TextView) findViewById(R.id.text_view9_1);
        textView9.setText(String.valueOf(testingInstitutionResource.getOutdoorsArea()));

        TextView textView10=(TextView) findViewById(R.id.text_view10_1);
        textView10.setText(testingInstitutionResource.getNameAndAddress());

        TextView textView11=(TextView) findViewById(R.id.text_view11_1);
        textView11.setText(testingInstitutionResource.getNewPlace());
    }

    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/TestingInstitutionResource/get";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    //Log.d("请求回复：",responseData);
                    parseJSONWithGSON2(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里进行UI操作，将结果显示到界面上
                            initView();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void  parseJSONWithGSON2(String responseData){
        // JSONArray array=new JSONArray();
        try{
            //Log.d("responseData:",responseData);
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {
                Log.d("null","array null");

            }else
            {
                Gson gson=new Gson();
                testingInstitutionResource=gson.fromJson(array,new TypeToken<TestingInstitutionResource>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
