package com.example.developer2.appmap.templates;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.developer2.appmap.activities.ViewPDFActivity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private Paragraph paragraph;

    private PdfWriter pdfWriter;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.RED);


    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument() {
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = pdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        } catch (Exception e) {
            Log.e("openDocument", e.toString());
        }

    }

    private void createFile() {
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!folder.exists())
            folder.mkdirs();
        pdfFile = new File(folder, "TemplatePDF.pdf");

    }

    public void closeDocument() {
        document.close();

    }

    public void addMetaData(String title, String subject, String author) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, String date) {
        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fSubTitle));
            addChildP(new Paragraph("Generado: " + date, fHighText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addTitles", e.toString());
        }
    }
    private void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }
    public void addParagraph(String text) {
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);

            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void createTable(String[] header, ArrayList<String[]> clients) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfCell;
            int indexC = 0;
            while (indexC < header.length) {
                pdfCell = new PdfPCell(new Phrase(header[indexC++], fSubTitle));
                pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfCell.setBackgroundColor(BaseColor.GREEN);
                pdfPTable.addCell(pdfCell);

            }

            for (int indexR = 0; indexR < clients.size(); indexR++) {
                String[] row = clients.get(indexR);
                for (indexC = 0; indexC < header.length; indexC++) {
                    pdfCell = new PdfPCell(new Phrase(row[indexC]));
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfCell.setFixedHeight(40);
                    pdfPTable.addCell(pdfCell);

                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("createTable",e.toString());
        }

    }
    public void viewPDF(){
        Intent intent = new Intent(context, ViewPDFActivity.class);
        intent.putExtra("path", pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
