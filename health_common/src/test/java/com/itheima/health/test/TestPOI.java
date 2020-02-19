package com.itheima.health.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestPOI {

    @Test
    public void readExcel() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
//        XSSFSheet sheet = workbook.getSheet("预约设置模板");
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        for (Row row : sheetAt) {
            for (Cell cell : row) {
                String val = cell.getStringCellValue();
                System.out.println(val);
            }
        }
        workbook.close();
    }

    @Test
    public void readExcel_2() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
//        XSSFSheet sheet = workbook.getSheet("预约设置模板");
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();
        System.out.println("一共有多少行？    "+lastRowNum);
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            System.out.println("一共有多少单元格？    "+lastCellNum);
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                String value = cell.getStringCellValue();
                System.out.println(value);
            }

        }
        workbook.close();
    }

    @Test
    public void writeExcel_1() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("用户信息");
        XSSFRow row_1 = sheet.createRow(0);
        row_1.createCell(0).setCellValue("姓名");
        row_1.createCell(1).setCellValue("年龄");
        row_1.createCell(2).setCellValue("地址");

        XSSFRow row_2 = sheet.createRow(1);
        row_2.createCell(0).setCellValue("张无忌");
        row_2.createCell(1).setCellValue("25");
        row_2.createCell(2).setCellValue("武当山");

        XSSFRow row_3 = sheet.createRow(2);
        row_3.createCell(0).setCellValue("灭绝师太");
        row_3.createCell(1).setCellValue("50");
        row_3.createCell(2).setCellValue("峨眉山");

        OutputStream stream = new FileOutputStream(new File("D:/倚天屠龙记.xlsx"));
        workbook.write(stream);
        stream.flush();
        stream.close();
        workbook.close();


    }
}
