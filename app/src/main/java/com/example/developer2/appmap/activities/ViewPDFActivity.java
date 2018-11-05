package com.example.developer2.appmap.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.developer2.appmap.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ViewPDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = (PDFView) findViewById(R.id.pdfView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            file = new File(bundle.getString("path",""));

        }
        pdfView.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).load();
    }



}
