package com.example.peterzhao.dysreader.pdf_utils;

import android.content.Context;
import android.widget.Toast;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFReader{

    public static String ReadPDF(Context context, String filePath) {
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;
        File file = new File(filePath);
        try {
            PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));

            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);

            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);

            return pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            Toast.makeText(context, "readPDF IOException: check if the filePath is valid",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }
}