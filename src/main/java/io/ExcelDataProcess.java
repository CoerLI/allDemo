package io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ExcelDataProcess {
    public void writeCsv(String filePath,ArrayList<ArrayList<String>> data){
        File file = new File(filePath);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            for (ArrayList<String> al : data) {
                for (String str : al) {
                    writer.write(str);
                    writer.write(",");
                }
                writer.write("\r\n");
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  ArrayList<ArrayList<String>> readXls(String filePath) {
        Workbook wb = getWorkbookByfilePath(filePath);
        Sheet sheet = wb.getSheetAt(0);

        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        ArrayList<String> analysis;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            ArrayList<String> al = new ArrayList<String>();
            for (int j = 0; j < r.getLastCellNum(); j++) {
                Cell c = r.getCell(j);
                c.setCellType(CellType.STRING);
                al.add(c.getStringCellValue());
            }
            res.add(al);
        }
        return res;
    }

    public  ArrayList<ArrayList<String>> analysis(ArrayList<ArrayList<String>> res) {
        ArrayList<String> head = new ArrayList<>();
        ArrayList<ArrayList<String>> analysisRes = new ArrayList<>();
        head.add("class");
        int classes = 0;

        for (int i = 2; i < res.get(0).size(); i++) {
            head.add(res.get(0).get(i) + " max grade");
            head.add(res.get(0).get(i) + " min grade");
            head.add(res.get(0).get(i) + " avg grade");
        }
        analysisRes.add(head);
        HashMap<String, Integer> map = new HashMap<>();

        for (ArrayList<String> al : res) {
            if (!map.containsKey(al.get(1))) {
                map.put(al.get(1), 1);
            } else {
                map.put(al.get(1), map.get(al.get(1)) + 1);
            }
        }
        classes = map.size();

        for (int i = 1; i < classes; i++) {
            ArrayList<String> al = new ArrayList<>();
            al.add(String.valueOf(i));
            for (int j = 1; j < head.size(); j++) {
                if (j == 2 || j == 5 || j == 8) {
                    al.add("100");
                } else {
                    al.add("0");
                }

            }
            analysisRes.add(al);
        }

        res.remove(0);
        ArrayList<String> change = null;

        for (ArrayList<String> al : res) {
            String classId = al.get(1);
            int math = Integer.parseInt(al.get(2));
            int physical = Integer.parseInt(al.get(3));
            int chemistry = Integer.parseInt(al.get(4));


            for (ArrayList<String> als : analysisRes) {
                if (classId.equals(als.get(0))) {
                    change = als;
                    break;
                }
            }
            change.set(1, String.valueOf(Math.max(Integer.parseInt(change.get(1)), math)));
            change.set(2, String.valueOf(Math.min(Integer.parseInt(change.get(2)), math)));
            change.set(3, String.valueOf(Integer.parseInt(change.get(3)) + math));
            change.set(4, String.valueOf(Math.max(Integer.parseInt(change.get(4)), physical)));
            change.set(5, String.valueOf(Math.min(Integer.parseInt(change.get(5)), physical)));
            change.set(6, String.valueOf(Integer.parseInt(change.get(6)) + physical));
            change.set(7, String.valueOf(Math.max(Integer.parseInt(change.get(7)), chemistry)));
            change.set(8, String.valueOf(Math.min(Integer.parseInt(change.get(8)), chemistry)));
            change.set(9, String.valueOf(Integer.parseInt(change.get(9)) + chemistry));


        }
        for (int i = 1; i < analysisRes.size(); i++) {
            for (int j = 2; j <= 9; j++) {
                if (j % 3 == 0) {
                    double db = Double.valueOf(analysisRes.get(i).get(j)) / Double.valueOf(map.get(analysisRes.get(i).get(0)));
                    DecimalFormat df = new DecimalFormat("0.00");
                    analysisRes.get(i).set(j, String.valueOf(df.format(db)));

                }
            }
        }

        return analysisRes;
    }


    public  Workbook getWorkbookByfilePath(String filePath) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(filePath));
            if (filePath.toLowerCase().endsWith("xls")) {
                return new HSSFWorkbook(file);
            } else if (filePath.toLowerCase().endsWith("xlsx")) {
                return new XSSFWorkbook(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(file);
        }
        return null;
    }
}
