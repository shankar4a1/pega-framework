

package com.pega.util;

import com.pega.framework.*;
import io.cucumber.java.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public class DataTestIdUtil {
    static String location;
    static String elementVariableName;
    private static FileInputStream FIS;
    static File f;
    static DataTestIdUtil dtUtilObj;
    static final Logger LOGGER;

    static {
        DataTestIdUtil.location = "";
        DataTestIdUtil.elementVariableName = "";
        DataTestIdUtil.f = null;
        DataTestIdUtil.dtUtilObj = new DataTestIdUtil();
        LOGGER = Logger.getLogger(DataTestIdUtil.class.getName());
    }

    public static void setEmptyWebDriver() {
        PegaWebDriverImpl.setElementsEmpty();
        PegaWebElementImpl.setElementsEmpty();
    }

    public static void analyzeDataTestID(final Scenario scenario, final List<File> paths, final String ExcelBookName, final String sheetName) throws IOException {
        final Set<String> uniquerReturnedElementsFromElement = PegaWebElementImpl.getElements();
        final Set<String> mergedSetOfElements = PegaWebDriverImpl.getElements();
        mergedSetOfElements.addAll(uniquerReturnedElementsFromElement);
        Workbook workbook = null;
        OutputStream file = null;
        try {
            DataTestIdUtil.f = new File(ExcelBookName);
            if (!DataTestIdUtil.f.exists()) {
                workbook = new XSSFWorkbook();
                workbook.createSheet();
                file = new FileOutputStream(DataTestIdUtil.f);
                workbook.write(file);
            }
            workbook = getWorkbook(DataTestIdUtil.f.getAbsolutePath());
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            final CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            final CellStyle cellStyle2 = DataTestIdUtil.dtUtilObj.applyCellStyle(sheet, cellStyle);
            final Iterator<String> iterator1 = mergedSetOfElements.iterator();
            final Row row = sheet.createRow(0);
            final Cell cell = DataTestIdUtil.dtUtilObj.setValues(cellStyle2, row);
            whileLoopmethod(iterator1, sheet, paths, cellStyle, scenario, row, cell, workbook);
        } catch (InvalidFormatException e) {
            e.printStackTrace(System.err);
            return;
        } finally {
            if (file != null) {
                file.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        }
        if (file != null) {
            file.close();
        }
        if (workbook != null) {
            workbook.close();
        }
    }

    public CellStyle applyCellStyle(final Sheet sheet, final CellStyle cellStyle) {
        final Font font = sheet.getWorkbook().createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Trebuchet MS");
        cellStyle.setFont(font);
        cellStyle.setWrapText(false);
        cellStyle.setShrinkToFit(false);
        final CellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        final Font font2 = sheet.getWorkbook().createFont();
        font2.setBold(true);
        font2.setFontHeightInPoints((short) 16);
        font2.setFontName("Times New Roman");
        cellStyle2.setAlignment((short) 2);
        cellStyle2.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle2.setFont(font2);
        cellStyle2.setWrapText(false);
        cellStyle2.setShrinkToFit(false);
        return cellStyle2;
    }

    public Cell setValues(final CellStyle cellStyle1, final Row row) {
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Element Variable Name");
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Element Identification");
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Data Test Id Value From Page");
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("No.of times Data Test ID value repeated in Page");
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Tag Name of the element in application");
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("IS DataTestID Is Used In Scripting?");
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Scenario Name");
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue("Interface Name and Location");
        return cell;
    }

    public static void whileLoopmethod(final Iterator<String> iterator1, final Sheet sheet, final List<File> paths, final CellStyle cellStyle, final Scenario scenario, final Row row, final Cell cell, final Workbook workbook) {
        while (iterator1.hasNext()) {
            final String itrvalue = iterator1.next();
            final String[] string1 = itrvalue.split("~");
            DataTestIdUtil.elementVariableName = "";
            DataTestIdUtil.location = "";
            for (int i = 0; i < paths.size(); ++i) {
                try {
                    searchForString(paths.get(i), string1[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            boolean found = false;
            for (int j = 1; j < sheet.getLastRowNum(); ++j) {
                final Row itrrow = sheet.getRow(j);
                if (itrrow.getCell(1).getStringCellValue().trim().equals(string1[0].trim()) && ((itrrow.getCell(0).getStringCellValue() == null && DataTestIdUtil.elementVariableName == null) || itrrow.getCell(0).getStringCellValue().trim().equals(DataTestIdUtil.elementVariableName.trim()))) {
                    final Cell itrcell0 = itrrow.getCell(2);
                    final String s0 = itrcell0.getStringCellValue();
                    itrcell0.setCellStyle(cellStyle);
                    itrcell0.setCellValue(s0 + "\n" + string1[1].trim());
                    final Cell itrcell2 = itrrow.getCell(3);
                    final String s2 = itrcell2.getStringCellValue();
                    itrcell2.setCellStyle(cellStyle);
                    itrcell2.setCellValue(s2 + "\n" + string1[2].trim());
                    final Cell itrcell3 = itrrow.getCell(4);
                    final String s3 = itrcell3.getStringCellValue();
                    itrcell3.setCellStyle(cellStyle);
                    itrcell3.setCellValue(s3 + "\n" + string1[3].trim());
                    final Cell itrcell4 = itrrow.getCell(5);
                    final String s4 = itrcell4.getStringCellValue();
                    itrcell4.setCellStyle(cellStyle);
                    itrcell4.setCellValue(s4 + "\n" + string1[4].trim());
                    final Cell itrcell5 = itrrow.getCell(6);
                    final String s5 = itrcell5.getStringCellValue();
                    itrcell5.setCellStyle(cellStyle);
                    itrcell5.setCellValue(s5 + "\nScenarioName-" + scenario.getId() + ",\nTag Names-" + scenario.getSourceTagNames());
                    found = true;
                }
            }
            if (!found) {
                forlooptest(sheet, cellStyle, string1, scenario, workbook);
            }
        }
    }

    public static void forlooptest(final Sheet sheet, final CellStyle cellStyle, final String[] string1, final Scenario scenario, final Workbook workbook) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(DataTestIdUtil.elementVariableName);
        for (int k = 0; k < string1.length; ++k) {
            cell = row.createCell(k + 1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(string1[k]);
        }
        cell = row.createCell(string1.length + 1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("ScenarioName-" + scenario.getId() + ",\nTag Names-" + scenario.getSourceTagNames());
        cell = row.createCell(string1.length + 2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(DataTestIdUtil.location);
        if (DataTestIdUtil.FIS != null) {
            try {
                DataTestIdUtil.FIS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Throwable t = null;
            try {
                final FileOutputStream outputStream = new FileOutputStream(DataTestIdUtil.f.getAbsolutePath());
                try {
                    workbook.write(outputStream);
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
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
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void searchForString(final File file, String elementFromList) throws IOException {
        Scanner in2 = null;
        if (file.isDirectory()) {
            if (file.canRead()) {
                File[] listFiles;
                for (int length = (listFiles = file.listFiles()).length, k = 0; k < length; ++k) {
                    final File temp = listFiles[k];
                    if (temp.isDirectory()) {
                        searchForString(temp, elementFromList);
                    } else if (temp.getName().contains("java")) {
                        int linenum = 0;
                        try {
                            in2 = new Scanner(new FileReader(temp.getAbsolutePath()));
                            while (in2.hasNextLine()) {
                                ++linenum;
                                final String element = in2.nextLine();
                                if (element != null && element.contains(elementFromList.trim())) {
                                    final String i1 = new StringBuilder().append(element.trim().charAt(0)).toString();
                                    if (i1.equals("/")) {
                                        continue;
                                    }
                                    final String[] split1 = element.split("=");
                                    final String[] split2 = split1[0].trim().split(" ");
                                    if (split2.length == 2) {
                                        DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + split2[1].trim() + "\n";
                                    } else {
                                        DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + "_" + "\n";
                                    }
                                    DataTestIdUtil.location = DataTestIdUtil.location + temp.getAbsolutePath() + "::line" + linenum + "\n";
                                } else {
                                    if (element == null) {
                                        continue;
                                    }
                                    final String change1 = elementFromList.trim();
                                    if (change1.contains("By.linkText(")) {
                                        String[] changesplit = null;
                                        if (change1.contains("\"")) {
                                            changesplit = change1.split("\"");
                                        } else if (change1.contains("'")) {
                                            changesplit = change1.split("'");
                                        }
                                        final String finalpath = elementFromList = "By.linkText(LocalizationUtil.getLocalizedWord(\"" + changesplit[1] + "\"))";
                                        if (!element.contains(elementFromList.trim())) {
                                            continue;
                                        }
                                        final String i2 = new StringBuilder().append(element.trim().charAt(0)).toString();
                                        if (i2.equals("/")) {
                                            continue;
                                        }
                                        final String[] split3 = element.split("=");
                                        final String[] split4 = split3[0].trim().split(" ");
                                        if (split4.length == 2) {
                                            DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + split4[1].trim() + "\n";
                                        } else {
                                            DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + "_" + "\n";
                                        }
                                        DataTestIdUtil.location = DataTestIdUtil.location + temp.getAbsolutePath() + "::line" + linenum + "\n";
                                    } else if (change1.contains("By.partialLinkText(")) {
                                        String[] changesplit = null;
                                        if (change1.contains("\"")) {
                                            changesplit = change1.split("\"");
                                        } else if (change1.contains("'")) {
                                            changesplit = change1.split("'");
                                        }
                                        final String finalpath = elementFromList = "By.partialLinkinkText(LocalizationUtil.getLocalizedWord(\"" + changesplit[1] + "\"))";
                                        if (!element.contains(elementFromList.trim())) {
                                            continue;
                                        }
                                        final String i2 = new StringBuilder().append(element.trim().charAt(0)).toString();
                                        if (i2.equals("/")) {
                                            continue;
                                        }
                                        final String[] split3 = element.split("=");
                                        final String[] split4 = split3[0].trim().split(" ");
                                        if (split4.length == 2) {
                                            DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + split4[1].trim() + "\n";
                                        } else {
                                            DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + "_" + "\n";
                                        }
                                        DataTestIdUtil.location = DataTestIdUtil.location + temp.getAbsolutePath() + "::line" + linenum + "\n";
                                    } else {
                                        if (!change1.contains("By.xpath(") || !change1.contains("text()")) {
                                            continue;
                                        }
                                        String finalpath2 = "";
                                        String finalpath3 = "";
                                        final String[] changesplit2 = change1.split("'");
                                        for (int j = 0; j < changesplit2.length; ++j) {
                                            if (!changesplit2[j].contains("text()")) {
                                                if (!changesplit2[j].contains("(") && !changesplit2[j].contains(")")) {
                                                    finalpath2 = finalpath2 + "'" + changesplit2[j] + "'";
                                                    finalpath3 = finalpath3 + "'" + changesplit2[j] + "'";
                                                } else {
                                                    finalpath2 = finalpath2 + changesplit2[j];
                                                    finalpath3 = finalpath3 + changesplit2[j];
                                                }
                                            } else {
                                                finalpath2 = finalpath2 + changesplit2[j];
                                                finalpath3 = finalpath3 + changesplit2[j];
                                                if (!changesplit2[j + 1].contains("(") && !changesplit2[j + 1].contains(")")) {
                                                    finalpath2 = finalpath2 + "'\"+LocalizationUtil.getLocalizedWord(\"" + changesplit2[j + 1] + "\")+\"'";
                                                    finalpath3 = finalpath3 + "\"+LocalizationUtil.getLocalizedWord(\"'" + changesplit2[j + 1] + "'\")+\"";
                                                }
                                                ++j;
                                            }
                                        }
                                        if (element.contains(finalpath2.trim())) {
                                            final String i3 = new StringBuilder().append(element.trim().charAt(0)).toString();
                                            if (i3.equals("/")) {
                                                continue;
                                            }
                                            final String[] split5 = element.split("=");
                                            final String[] split6 = split5[0].trim().split(" ");
                                            if (split6.length == 2) {
                                                DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + split6[1].trim() + "\n";
                                            } else {
                                                DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + "_" + "\n";
                                            }
                                            DataTestIdUtil.location = DataTestIdUtil.location + temp.getAbsolutePath() + "::line" + linenum + "\n";
                                        } else {
                                            if (!element.contains(finalpath3.trim())) {
                                                continue;
                                            }
                                            final String i3 = new StringBuilder().append(element.trim().charAt(0)).toString();
                                            if (i3.equals("/")) {
                                                continue;
                                            }
                                            final String[] split5 = element.split("=");
                                            final String[] split6 = split5[0].trim().split(" ");
                                            if (split6.length == 2) {
                                                DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + split6[1].trim() + "\n";
                                            } else {
                                                DataTestIdUtil.elementVariableName = DataTestIdUtil.elementVariableName + "_" + "\n";
                                            }
                                            DataTestIdUtil.location = DataTestIdUtil.location + temp.getAbsolutePath() + "::line" + linenum + "\n";
                                        }
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            if (in2 != null) {
                                in2.close();
                            }
                        }
                        if (in2 != null) {
                            in2.close();
                        }
                    }
                }
            } else {
                Reporter.log(file.getAbsoluteFile() + "Permission Denied", true);
            }
        } else {
            Reporter.log(file.getAbsoluteFile() + "is not a file", true);
        }
        if (!DataTestIdUtil.location.contains("::line")) {
            DataTestIdUtil.elementVariableName = "_";
            DataTestIdUtil.location = "_";
        }
    }

    private static Workbook getWorkbook(final String excelFilePath) throws IOException, InvalidFormatException {
        final FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = null;
        workbook = new XSSFWorkbook(fis);
        return workbook;
    }

    public static void updateElementDeclaration(final String excelBookName, final String sheetName) {
        Workbook workbook = null;
        File f = null;
        String elementline = null;
        try {
            f = new File(excelBookName);
            if (!f.exists()) {
                Reporter.log("Not able to find the " + excelBookName + "Excel work Book in the project folder", true);
            }
            Reporter.log("excel sheet absolute path: " + f.getAbsolutePath(), true);
            workbook = getWorkbook(f.getAbsolutePath());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InvalidFormatException e2) {
            e2.printStackTrace();
        }
        final Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            Reporter.log("Not able to find the " + sheetName + "sheet in the " + excelBookName + " work Book in the project folder", true);
        } else {
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                String s2 = null;
                String s3 = null;
                String s4 = null;
                boolean tagsequal = true;
                boolean datatestIDequal = true;
                boolean datatestIdvalue = true;
                final Row itrrow = sheet.getRow(i);
                final String[] dataTestIDvaluesplit = itrrow.getCell(3).getStringCellValue().trim().split("\n");
                if (dataTestIDvaluesplit.length == 1) {
                    s4 = itrrow.getCell(3).getStringCellValue().trim();
                }
                for (int m = 0; m < dataTestIDvaluesplit.length - 1; ++m) {
                    s4 = dataTestIDvaluesplit[m].trim();
                    if (!s4.equals(dataTestIDvaluesplit[m + 1])) {
                        datatestIdvalue = false;
                        break;
                    }
                    Reporter.log("tag names are not equal", true);
                }
                if (s4.trim().equals("1")) {
                    final String[] tagnamesplit = itrrow.getCell(4).getStringCellValue().trim().split("\n");
                    if (tagnamesplit.length == 1) {
                        s2 = itrrow.getCell(4).getStringCellValue().trim();
                    }
                    for (int j = 0; j < tagnamesplit.length - 1; ++j) {
                        s2 = tagnamesplit[j].trim();
                        if (!s2.equals(tagnamesplit[j + 1])) {
                            tagsequal = false;
                            break;
                        }
                        Reporter.log("tag names are not equal", true);
                    }
                    final String[] dataTestIDsplit = itrrow.getCell(2).getStringCellValue().trim().split("\n");
                    if (dataTestIDsplit.length == 1) {
                        s3 = itrrow.getCell(2).getStringCellValue().trim();
                    }
                    for (int k = 0; k < dataTestIDsplit.length - 1; ++k) {
                        s3 = dataTestIDsplit[k].trim();
                        if (!s3.equals(dataTestIDsplit[k + 1])) {
                            datatestIDequal = false;
                            break;
                        }
                        Reporter.log("tag names are not equal", true);
                    }
                    if (tagsequal && datatestIDequal && datatestIdvalue && !itrrow.getCell(0).getStringCellValue().trim().equalsIgnoreCase("_") && !itrrow.getCell(7).getStringCellValue().trim().equalsIgnoreCase("_")) {
                        final String[] locationssplit1 = itrrow.getCell(7).getStringCellValue().trim().split("\n");
                        for (int l = 0; l < locationssplit1.length; ++l) {
                            String locationaftersplit1 = locationssplit1[l];
                            if (locationssplit1[l].contains("_C:")) {
                                locationaftersplit1 = locationssplit1[l].replaceAll("_C:", "C:");
                            } else {
                                locationaftersplit1 = locationssplit1[l];
                            }
                            if (!locationaftersplit1.equals("_")) {
                                final String[] locationsplit2 = locationaftersplit1.split("::");
                                final String locationsplitreplaced = locationsplit2[0].replaceAll("\\\\.\\\\", "\\\\");
                                locationsplitreplaced.trim();
                                final File javafile = new File(locationsplitreplaced);
                                if (!javafile.exists()) {
                                    Reporter.log("Not able to find the " + javafile + " in the project folder", true);
                                }
                                Scanner reader1 = null;
                                try {
                                    String elementline2 = null;
                                    String conditionalstring = null;
                                    reader1 = new Scanner(new FileReader(javafile));
                                    while (reader1.hasNextLine()) {
                                        elementline = reader1.nextLine();
                                        if (s2 != null) {
                                            if (elementline != null && elementline.contains(itrrow.getCell(1).getStringCellValue().trim())) {
                                                elementline2 = elementline.replace(itrrow.getCell(1).getStringCellValue().trim(), "By.xpath(\"//" + s2 + "[@data-test-id=" + s3 + "]\")");
                                                conditionalstring = itrrow.getCell(1).getStringCellValue().trim();
                                            } else {
                                                final String change1 = itrrow.getCell(1).getStringCellValue().trim();
                                                String finalpath = "";
                                                String finalpath2 = "";
                                                if (change1.contains("By.linkText(")) {
                                                    String[] changesplit = null;
                                                    if (change1.contains("\"")) {
                                                        changesplit = change1.split("\"");
                                                    } else if (change1.contains("'")) {
                                                        changesplit = change1.split("'");
                                                    }
                                                    finalpath = "By.linkText(LocalizationUtil.getLocalizedWord(\"" + changesplit[1] + "\"))";
                                                } else if (change1.contains("By.partialLinkText(")) {
                                                    String[] changesplit = null;
                                                    if (change1.contains("\"")) {
                                                        changesplit = change1.split("\"");
                                                    } else if (change1.contains("'")) {
                                                        changesplit = change1.split("'");
                                                    }
                                                    finalpath = "By.partialLinkinkText(LocalizationUtil.getLocalizedWord(\"" + changesplit[1] + "\"))";
                                                } else if (change1.contains("By.xpath(") && change1.contains("text()")) {
                                                    final String[] changesplit = change1.split("'");
                                                    for (int f2 = 0; f2 < changesplit.length; ++f2) {
                                                        if (!changesplit[f2].contains("text()")) {
                                                            if (!changesplit[f2].contains("(") && !changesplit[f2].contains(")")) {
                                                                finalpath = finalpath + "'" + changesplit[f2] + "'";
                                                                finalpath2 = finalpath2 + "'" + changesplit[f2] + "'";
                                                            } else {
                                                                finalpath = finalpath + changesplit[f2];
                                                                finalpath2 = finalpath2 + changesplit[f2];
                                                            }
                                                        } else {
                                                            finalpath = finalpath + changesplit[f2];
                                                            finalpath2 = finalpath2 + changesplit[f2];
                                                            if (!changesplit[f2 + 1].contains("(") && !changesplit[f2 + 1].contains(")")) {
                                                                finalpath = finalpath + "'\"+LocalizationUtil.getLocalizedWord(\"" + changesplit[f2 + 1] + "\")+\"'";
                                                                finalpath2 = finalpath2 + "\"+LocalizationUtil.getLocalizedWord(\"'" + changesplit[f2 + 1] + "'\")+\"";
                                                            }
                                                            ++f2;
                                                        }
                                                    }
                                                }
                                                if (elementline != null && elementline.contains(finalpath) && !elementline.isEmpty() && !finalpath.isEmpty()) {
                                                    elementline2 = elementline.replace(finalpath.trim(), "By.xpath(\"//" + s2 + "[@data-test-id=" + s3 + "]\")");
                                                    conditionalstring = finalpath;
                                                } else if (elementline != null && elementline.contains(finalpath2) && !elementline.isEmpty() && !finalpath2.isEmpty()) {
                                                    elementline2 = elementline.replace(finalpath2.trim(), "By.xpath(\"//" + s2 + "[@data-test-id=" + s3 + "]\")");
                                                    conditionalstring = finalpath2;
                                                }
                                            }
                                            try {
                                                final List<String> fileContent = new ArrayList<String>(Files.readAllLines(javafile.toPath(), StandardCharsets.UTF_8));
                                                for (int l2 = 0; l2 < fileContent.size(); ++l2) {
                                                    if (conditionalstring != null && elementline2 != null && fileContent.get(l2).contains(conditionalstring)) {
                                                        fileContent.set(l2, elementline2.trim());
                                                        break;
                                                    }
                                                }
                                                Files.write(javafile.toPath(), fileContent, StandardCharsets.UTF_8);
                                            } catch (IOException e3) {
                                                e3.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (FileNotFoundException e4) {
                                    e4.printStackTrace();
                                    continue;
                                } finally {
                                    if (reader1 != null) {
                                        reader1.close();
                                    }
                                }
                                if (reader1 != null) {
                                    reader1.close();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void percentageDetails(final String excelBookName, final String sheetName, final String sheetname1) throws IOException {
        Workbook workbook = null;
        File f = null;
        OutputStream file = null;
        try {
            f = new File(excelBookName);
            if (!f.exists()) {
                workbook = new XSSFWorkbook();
                workbook.createSheet();
                file = new FileOutputStream(f);
                workbook.write(file);
            }
            workbook = getWorkbook(f.getAbsolutePath());
            int totalElements = 0;
            int datatestIdnotused = 0;
            f = new File(excelBookName);
            if (!f.exists()) {
                Reporter.log("Not able to find the " + excelBookName + "Excel work Book in the project folder", true);
            }
            Reporter.log("excel sheet absolute path: " + f.getAbsolutePath(), true);
            workbook = getWorkbook(f.getAbsolutePath());
            final Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                Reporter.log("Not able to find the " + sheetName + "sheet in the " + excelBookName + " work Book in the project folder", true);
            } else {
                totalElements = sheet.getLastRowNum();
                for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                    final Row itrrow = sheet.getRow(i);
                    final String dataTestIdused = itrrow.getCell(5).getStringCellValue().trim();
                    if (dataTestIdused.contains("N")) {
                        ++datatestIdnotused;
                    }
                }
            }
            Sheet sheet2 = workbook.getSheet(sheetname1);
            if (sheet2 == null) {
                sheet2 = workbook.createSheet(sheetname1);
            }
            final Row row = sheet2.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Total Elements");
            cell = row.createCell(1);
            cell.setCellValue("No.of Elements used Data Test ID");
            cell = row.createCell(2);
            cell.setCellValue("No.of Elements used not Data Test ID");
            cell = row.createCell(3);
            cell.setCellValue("percentageofElementsUsedDataTestID(%)");
            final Row row2 = sheet2.createRow(workbook.getNumberOfSheets() + 1);
            cell = row2.createCell(0);
            cell.setCellValue(totalElements);
            cell = row2.createCell(1);
            cell.setCellValue(totalElements - datatestIdnotused);
            cell = row2.createCell(2);
            cell.setCellValue(totalElements - (totalElements - datatestIdnotused));
            cell = row2.createCell(3);
            if (totalElements != 0) {
                cell.setCellValue((100 - datatestIdnotused * 100 / totalElements) + "%");
            }
            if (DataTestIdUtil.FIS != null) {
                DataTestIdUtil.FIS.close();
            }
            Throwable t = null;
            try {
                final FileOutputStream outputStream = new FileOutputStream(f.getAbsolutePath());
                try {
                    workbook.write(outputStream);
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
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
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InvalidFormatException e2) {
            e2.printStackTrace();
        } finally {
            if (file != null) {
                file.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        }
        if (file != null) {
            file.close();
        }
        if (workbook != null) {
            workbook.close();
        }
    }
}
