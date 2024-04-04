package com.la.letsassemble.Util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;


import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;

//@Component
public class MessegeFilter {
    private ArrayList<String> list;
    private String filepath;
    public MessegeFilter(){
        list = new ArrayList<>();
        ReadXls();
    }
    private void ReadXls(){
        try(FileInputStream fis = new FileInputStream("LetsAssemble_Filter.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis)){
            XSSFSheet sheet = workbook.getSheetAt(0);
            for(int i = 0 ; i <= sheet.getLastRowNum(); i++){
                XSSFRow row = sheet.getRow(i);
                if(row == null){continue;}
                for(int j = 0 ; j < row.getLastCellNum(); j++){
                    XSSFCell cell = row.getCell(j);
                    if(cell != null){
                        list.add(cell.toString());
                    }
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String filterMessage(String message){
        for(String msg : list){
            if(message.contains(msg)){
                String replacement = "*".repeat(msg.length());
                message = message.replaceAll(msg,replacement);
            }
        }
        return message;
    }
    public void print(){
        int c = 0;
        for(String s : list){
            System.err.println((c++)+"번째 단어 : "+s);
        }
    }
}

