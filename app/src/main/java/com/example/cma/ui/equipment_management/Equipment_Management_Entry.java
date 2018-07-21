package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.cma.R;
import com.example.cma.utils.ViewUtil;

public class Equipment_Management_Entry extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_entry);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(Equipment_Management_Entry.this, Equipment_Main.class));
                break;
            case R.id.button2:
                startActivity(new Intent(Equipment_Management_Entry.this, EquipmentReceive_Main.class));
                break;
            case R.id.button3:
                startActivity(new Intent(Equipment_Management_Entry.this, EquipmentApplication_Main.class));
                break;
            case R.id.button4:
                startActivity(new Intent(Equipment_Management_Entry.this, EquipmentUse_Main.class));
                break;
            case R.id.button5:
                startActivity(new Intent(Equipment_Management_Entry.this, EquipmentMaintenance_Main.class));
                break;
        }
    }

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
