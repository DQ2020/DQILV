package com.sft.broker.dqui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.open.dqilv.ImpressionsLabelView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImpressionsLabelView labels = findViewById(R.id.labels);
        labels.addLabel("阳光");
        labels.addLabel("帅气");
        labels.addLabel("风流倜傥");
        labels.addLabel("英俊潇洒");
        labels.addLabel("DQ2020");
        labels.addLabel("阳光");
        labels.addLabel("帅气");
        labels.addLabel("风流倜傥");
        labels.addLabel("英俊潇洒");
        labels.addLabel("阳光");
        labels.addLabel("帅气");
        labels.addLabel("风流倜傥");
        labels.addLabel("英俊潇洒");
        labels.addLabel("阳光");
        labels.addLabel("帅气");
        labels.addLabel("风流倜傥");
        labels.addLabel("英俊潇洒");
    }
}
