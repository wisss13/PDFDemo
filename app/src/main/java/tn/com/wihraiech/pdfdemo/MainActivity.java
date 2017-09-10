package tn.com.wihraiech.pdfdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;


public class MainActivity extends Activity {
    private Button b;
    private PdfPCell cell;
    private String textAnswer;
    private Image bgImage;
    ListView list;
    private String path;
    private File dir;
    private File file;

    //use to set background color
    /*BaseColor myColor = WebColors.getRGBColor("#2c19ff");
    BaseColor myColor1 = WebColors.getRGBColor("#FBD2B2");
    BaseColor myColor2 = WebColors.getRGBColor("#757575");*/
    BaseColor myColor = WebColors.getRGBColor("#384dee");
    BaseColor myColor1 = WebColors.getRGBColor("#FBD2B2");
    BaseColor myColor2 = WebColors.getRGBColor("#757575");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.button1);
        list = (ListView) findViewById(R.id.list);

        //creating new file path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/PDF Scores Files";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    //getting files from directory and display in listview
        try {

            final ArrayList<String> FilesInFolder = GetFiles("/sdcard/Download/PDF Scores Files");
            if (FilesInFolder.size() != 0)
                list.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, FilesInFolder));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    // Clicking on items
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i = 0; i < files.length; i++)
                MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }


    public void createPDF() throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            file = new File(dir, "Scores PDF" + ".pdf");
            //file = new File(dir, "Scores PDF" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(2);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 80};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = MainActivity.this.getResources().getDrawable(R.drawable.app_invite);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Immigration Québec APP"));

                cell.addElement(new Paragraph("Fiche d'évaluation pour le programme des Travailleurs Qualifiés sélectionnés par le Québec"));
                //cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                /*cell = new PdfPCell(new Paragraph("aaaaa"));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);*/

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(3);

                float[] columnWidth = new float[]{30, 70, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor2);

                cell = new PdfPCell(new Phrase("Seuil éliminatoire"));
                cell.setBackgroundColor(myColor2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Facteurs ET Critères"));
                cell.setBackgroundColor(myColor2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("    Points / Max"));
                cell.setBackgroundColor(myColor2);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(6);

                table.addCell(" 2 points"); table.addCell(" Niveau de scolarité"); table.addCell(getCell("10 / 14"));
                table.addCell(" "); table.addCell(" Domaine de formation"); table.addCell(getCell("8 / 12"));
                table.addCell(" "); table.addCell(" Expérience professionnelle"); table.addCell(getCell(" 0 / 8 "));
                table.addCell(" "); table.addCell(" Âge"); table.addCell(getCell(" 0 / 16 "));
                table.addCell(" "); table.addCell(" FRANÇAIS: Compréhension orale"); table.addCell(getCell(" 0 / 7 "));
                table.addCell(" "); table.addCell("                     Production orale"); table.addCell(getCell(" 0 / 7 "));
                table.addCell(" "); table.addCell("                     Compréhension écrite"); table.addCell(getCell(" 0 / 1 "));
                table.addCell(" "); table.addCell("                     Production écrite"); table.addCell(getCell(" 0 / 1 "));
                table.addCell(" "); table.addCell(" ANGLAIS: Compréhension orale"); table.addCell(getCell(" 0 / 2 "));
                table.addCell(" "); table.addCell("                   Production orale"); table.addCell(getCell(" 0 / 2 "));
                table.addCell(" "); table.addCell("                   Compréhension écrite"); table.addCell(getCell(" 0 / 1 "));
                table.addCell(" "); table.addCell("                   Production écrite"); table.addCell(getCell(" 0 / 1 "));
                table.addCell(" "); table.addCell(" Séjour au Québec"); table.addCell(getCell(" 0 / 5 "));
                table.addCell(" "); table.addCell(" Famille au Québec"); table.addCell(getCell(" 0 / 3 "));
                table.addCell(" "); table.addCell(" Offre d’emploi validée"); table.addCell(getCell(" 0 / 10 "));
                table.addCell(getCellColor(" 43 points")); table.addCell(getCellColor(" Seuil éliminatoire d'employabilité")); table.addCell(getCellColorCenter(" 0 / 43 "));
                table.addCell(" "); table.addCell(" Enfants"); table.addCell(getCell(" 0 / 8 "));
                table.addCell(" 1 point "); table.addCell(" Autonomie financière "); table.addCell(getCell(" 1 / 1 "));


                PdfPTable ftable = new PdfPTable(2);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{77, 23};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Seuil de passage à l'examen préliminaire et en sélection"));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase("             / 50"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                //cell = new PdfPCell(new Paragraph("Vous sembez satisfaire au criteres de selection"));
                //cell.setColspan(6);
                ftable.addCell(getCell2("Vous semblez satisfaire au criteres de selection"));
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getApplicationContext(), "Fichier PDF sauvegardé dans Téléchargement", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell getCell(String s) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(1);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(
                String.format(s),
                new Font(Font.FontFamily.HELVETICA, 12));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }
    private PdfPCell getCellColor(String s) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(1);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(
                String.format(s),
                new Font(Font.FontFamily.HELVETICA, 12));
        cell.setBackgroundColor(myColor1);
        cell.addElement(p);
        return cell;
    }
    private PdfPCell getCellColorCenter(String s) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(
                String.format(s),
                new Font(Font.FontFamily.HELVETICA, 12));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(myColor1);
        cell.addElement(p);
        return cell;
    }
    private PdfPCell getCell2(String s) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(3);
        Paragraph p = new Paragraph(
                String.format(s),
                new Font(Font.FontFamily.HELVETICA, 12));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }

}
