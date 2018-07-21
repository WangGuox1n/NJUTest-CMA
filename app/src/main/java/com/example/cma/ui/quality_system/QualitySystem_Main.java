package com.example.cma.ui.quality_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.utils.FileUtil;

public class QualitySystem_Main extends AppCompatActivity implements View.OnClickListener {
   Button b1;
    Button b2;
    Button b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_system__main);
        b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(this);
        b3=(Button)findViewById(R.id.button3);
        b3.setOnClickListener(this);
        b2=(Button)findViewById(R.id.button2);
        b2.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button1:
                Intent intent1=new Intent(QualitySystem_Main.this,QualityManual_Main.class);
                intent1.putExtra("function","ProgramFile");
                startActivity(intent1);
                break;

            case R.id.button2:
                Intent intent2=new Intent(QualitySystem_Main.this,QualityManual_Main.class);
                intent2.putExtra("function","QualityManual");
                startActivity(intent2);
                break;

            case R.id.button3:
                Intent intent3=new Intent(QualitySystem_Main.this,QualityManual_Main.class);
                intent3.putExtra("function","OperatingInstruction");
                startActivity(intent3);
                break;

            default:
                break;
        }
    }
}
