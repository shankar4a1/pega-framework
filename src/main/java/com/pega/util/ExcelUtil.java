

package com.pega.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.impl.regex.ParseException;
import org.testng.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class ExcelUtil {
    public static void writeToExcel(final String path, final String sheet_name, final int rowcount, final int colcount, final Object value) {
        Workbook workbook = null;
        Sheet sheet = null;
        try {
            final FileInputStream fis = new FileInputStream(new File(path));
            Reporter.log("Opening excel file: " + path, true);
            if (path.endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = new XSSFWorkbook(fis);
            }
            Boolean isSheetExist = false;
            for (int i = workbook.getNumberOfSheets() - 1; i >= 0; --i) {
                final Sheet tmpSheet = workbook.getSheetAt(i);
                if (tmpSheet.getSheetName().equals(sheet_name)) {
                    sheet = workbook.getSheetAt(i);
                    isSheetExist = true;
                }
            }
            if (!isSheetExist) {
                Reporter.log(sheet_name + ": sheet does not exist in the " + path + " excel file", true);
            }
            Row row = sheet.getRow(rowcount);
            if (row == null) {
                row = sheet.createRow(rowcount);
            }
            Cell cell = row.getCell(colcount);
            if (cell == null) {
                cell = row.createCell(colcount);
            }
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((int) value);
            }
            final FileOutputStream outputStream = new FileOutputStream(path);
            workbook.write(outputStream);
            Reporter.log("Writing to excel file: " + path, true);
            Reporter.log(value + " data has been updated to the " + path + " Excel file.", true);
        } catch (Exception e) {
            Reporter.log("Exception during the Excel processing::", true);
            e.printStackTrace();
            try {
                workbook.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return;
        } finally {
            try {
                workbook.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        try {
            workbook.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void appendDatatoExcel(final String path, final String sheetName, final String value) {
        appendDatatoExcel(path, sheetName, 0, value);
    }

    public static void appendDatatoExcel(final String path, final String sheetname, final int cellIndex, final String value) {
        Workbook workbook = null;
        try {
            final FileInputStream fis = new FileInputStream(new File(path));
            if (path.endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = new XSSFWorkbook(fis);
            }
            final Sheet sheet = workbook.getSheet(sheetname);
            if (sheet == null) {
                Reporter.log("Sheet not found :: " + sheetname, true);
            }
            int a = sheet.getLastRowNum();
            Reporter.log("Data will be added to row number: " + (a + 2), true);
            final Row row = sheet.createRow(++a);
            for (int i = 0; i <= cellIndex; ++i) {
                if (row.getCell(i) == null) {
                    row.createCell(i);
                }
            }
            row.getCell(cellIndex).setCellValue(value);
            final FileOutputStream fos = new FileOutputStream(new File(path));
            workbook.write(fos);
            Reporter.log("Data: " + value + " is successfully written to file " + path, true);
        } catch (Exception e) {
            Reporter.log("Exception during the Excel processing::", true);
            e.printStackTrace();
            try {
                workbook.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return;
        } finally {
            try {
                workbook.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        try {
            workbook.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String readFromExcel(final String path, final String sheet_name, final int rowcount, final int colcount) throws IOException {
        Sheet sheet = null;
        String value = null;
        Boolean sheetexist = false;
        try {
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(path);
                try {
                    Label_0354:
                    {
                        if (path.endsWith("xls")) {
                            try {
                                Throwable t2 = null;
                                try {
                                    final Workbook hssfWorkbook = new HSSFWorkbook(fis);
                                    try {
                                        for (int i = hssfWorkbook.getNumberOfSheets() - 1; i >= 0; --i) {
                                            final Sheet tmpSheet = hssfWorkbook.getSheetAt(i);
                                            if (tmpSheet.getSheetName().equals(sheet_name)) {
                                                sheet = hssfWorkbook.getSheetAt(i);
                                                sheetexist = true;
                                            }
                                        }
                                    } finally {
                                        if (hssfWorkbook != null) {
                                            hssfWorkbook.close();
                                        }
                                    }
                                } finally {
                                    if (t2 == null) {
                                        final Throwable exception = new Throwable();
                                        t2 = exception;
                                    } else {
                                        final Throwable exception = new Throwable();
                                        if (t2 != exception) {
                                            t2.addSuppressed(exception);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Reporter.log("Exception during the Excel processing::", true);
                                e.printStackTrace();
                                break Label_0354;
                            }
                        }
                        try {
                            Throwable t3 = null;
                            try {
                                final Workbook xssfWorkbook = new XSSFWorkbook(fis);
                                try {
                                    for (int i = xssfWorkbook.getNumberOfSheets() - 1; i >= 0; --i) {
                                        final Sheet tmpSheet = xssfWorkbook.getSheetAt(i);
                                        if (tmpSheet.getSheetName().equals(sheet_name)) {
                                            sheet = xssfWorkbook.getSheetAt(i);
                                            sheetexist = true;
                                        }
                                    }
                                } finally {
                                    if (xssfWorkbook != null) {
                                        xssfWorkbook.close();
                                    }
                                }
                            } finally {
                                if (t3 == null) {
                                    final Throwable exception2 = new Throwable();
                                    t3 = exception2;
                                } else {
                                    final Throwable exception2 = new Throwable();
                                    if (t3 != exception2) {
                                        t3.addSuppressed(exception2);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Reporter.log("Exception during the Excel processing::", true);
                            e.printStackTrace();
                        }
                    }
                    if (!sheetexist) {
                        Reporter.log(sheet_name + ": sheet does not exist in the " + path + " excel file", true);
                    }
                    final Row row = sheet.getRow(rowcount);
                    final Cell cell = row.getCell(colcount);
                    value = cellToString(cell);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            } finally {
                if (t == null) {
                    final Throwable exception3 = new Throwable();
                    t = exception3;
                } else {
                    final Throwable exception3 = new Throwable();
                    if (t != exception3) {
                        t.addSuppressed(exception3);
                    }
                }
            }
        } catch (Exception e2) {
            Reporter.log("Exception during the Excel processing::", true);
            e2.printStackTrace();
        }
        return value;
    }

    public static String[][] readCompleteExcelSheet(final String path, final String sheet_name) throws IOException {
        String[][] data = null;
        Sheet sheet = null;
        String value = null;
        Workbook hssfWorkbook = null;
        Workbook xssfWorkbook = null;
        Boolean sheetexist = false;
        try {
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(path);
                try {
                    Reporter.log("Reading excel file: " + path, true);
                    if (path.endsWith("xls")) {
                        hssfWorkbook = new HSSFWorkbook(fis);
                        for (int i = hssfWorkbook.getNumberOfSheets() - 1; i >= 0; --i) {
                            final Sheet tmpSheet = hssfWorkbook.getSheetAt(i);
                            if (tmpSheet.getSheetName().equals(sheet_name)) {
                                sheet = hssfWorkbook.getSheetAt(i);
                                sheetexist = true;
                            }
                        }
                    } else {
                        xssfWorkbook = new XSSFWorkbook(fis);
                        for (int i = xssfWorkbook.getNumberOfSheets() - 1; i >= 0; --i) {
                            final Sheet tmpSheet = xssfWorkbook.getSheetAt(i);
                            if (tmpSheet.getSheetName().equals(sheet_name)) {
                                sheet = xssfWorkbook.getSheetAt(i);
                                sheetexist = true;
                            }
                        }
                    }
                    if (!sheetexist) {
                        Reporter.log(sheet_name + ": sheet does not exist in the " + path + " excel file", true);
                    }
                    int temp = 0;
                    int colNum = 0;
                    final int rowNum = sheet.getLastRowNum() + 1;
                    for (int j = 0; j < rowNum; ++j) {
                        if (sheet.getRow(j) != null) {
                            temp = sheet.getRow(j).getLastCellNum();
                            if (temp > colNum) {
                                colNum = temp;
                            }
                        }
                    }
                    data = new String[rowNum][colNum];
                    for (int k = 0; k < rowNum; ++k) {
                        if (sheet.getRow(k) != null) {
                            final Row row = sheet.getRow(k);
                            for (int l = 0; l < colNum; ++l) {
                                final Cell cell = row.getCell(l);
                                if (cell != null) {
                                    value = cellToString(cell);
                                    data[k][l] = value;
                                }
                            }
                        }
                    }
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            } finally {
                if (t == null) {
                    final Throwable exception = new Throwable();
                    t = exception;
                } else {
                    final Throwable exception = new Throwable();
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        } catch (Exception e) {
            Reporter.log("Exception during the Excel processing::", true);
            e.printStackTrace();
            return data;
        } finally {
            xssfWorkbook.close();
            hssfWorkbook.close();
        }
        xssfWorkbook.close();
        hssfWorkbook.close();
        return data;
    }

    private static String cellToString(final Cell cell) {
        final int type = cell.getCellType();
        Object result = null;
        switch (type) {
            case 0: {
                result = cell.getNumericCellValue();
                break;
            }
            case 1: {
                result = cell.getStringCellValue();
                break;
            }
            case 4: {
                result = cell.getBooleanCellValue();
                break;
            }
            default: {
                throw new RuntimeException("There is no support for this type of cell");
            }
        }
        return result.toString();
    }

    public static Date readDateFromExcel(final SimpleDateFormat sdf, final String path, final String sheetName, final int rowIndex, final int cellIndex) {
        String value = null;
        Date date = null;
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        Label_0282:
        {
            try {
                final FileInputStream fis = new FileInputStream(path);
                Reporter.log("Reading excel file: " + path, true);
                workbook = new XSSFWorkbook(fis);
                Boolean isSheetExisting = false;
                for (int i = workbook.getNumberOfSheets() - 1; i >= 0; --i) {
                    final Sheet tmpSheet = workbook.getSheetAt(i);
                    if (tmpSheet.getSheetName().equals(sheetName)) {
                        sheet = workbook.getSheetAt(i);
                        isSheetExisting = true;
                    }
                }
                sheet = workbook.getSheet(sheetName);
                if (!isSheetExisting) {
                    Reporter.log(sheetName + ": sheet does not exist in the " + path + " excel file", true);
                }
                row = sheet.getRow(rowIndex);
                cell = row.getCell(cellIndex);
                value = cellToString(cell);
            } catch (Exception e) {
                Reporter.log("Exception during the Excel processing::", true);
                e.printStackTrace();
                try {
                    workbook.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                break Label_0282;
            } finally {
                try {
                    workbook.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            try {
                workbook.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (DateUtil.isCellDateFormatted(cell)) {
            try {
                value = sdf.format(cell.getDateCellValue());
            } catch (ParseException e3) {
                e3.printStackTrace();
            }
        } else {
            Reporter.log("We do not have appropriate date value in " + cell.getAddress() + " with format" + sdf.toPattern(), true);
        }
        try {
            date = sdf.parse(value);
        } catch (java.text.ParseException e4) {
            e4.printStackTrace();
        }
        return date;
    }
}
