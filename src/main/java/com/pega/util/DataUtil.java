

package com.pega.util;

import com.pega.exceptions.*;
import org.apache.commons.io.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;
import org.testng.*;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.regex.*;

public class DataUtil {
    String COPYRIGHT;
    private static final String VERSION = "$Id: DataUtil.java 194486 2016-05-25 11:47:44Z ShakkariSakethkumar $";
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(DataUtil.class.getName());
    }

    public DataUtil() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static String getRandomNumberString(final String value) {
        final SecureRandom rn = new SecureRandom();
        final int randomNum = rn.nextInt(9999) + 1000;
        return String.valueOf(value) + randomNum;
    }

    public static String getRandomString(final int length) {
        final StringBuffer buffer = new StringBuffer();
        String characters = "";
        characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final SecureRandom randmNo = new SecureRandom();
        for (int i = 0; i < length; ++i) {
            final double index = randmNo.nextInt(9);
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }

    public static String getRegExpPattern(final String regeExpPattern, final String actualString) {
        final Pattern p = Pattern.compile(regeExpPattern);
        final Matcher m = p.matcher(actualString);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String getRegExpPattern(final String regeExpPattern, final String actualString, final int matchNo) {
        final Pattern p = Pattern.compile(regeExpPattern);
        final Matcher m = p.matcher(actualString);
        if (m.find()) {
            return m.group(matchNo);
        }
        return null;
    }

    public static String getLastRegExpMatch(final String regeExpPattern, final String actualString) {
        String match = null;
        final Pattern p = Pattern.compile(regeExpPattern);
        final Matcher m = p.matcher(actualString);
        while (m.find()) {
            match = m.group(1);
        }
        return match;
    }

    public static String urlEncode(final String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e) {
            throw new PegaClientException(e);
        }
    }

    public static void writeStringToFile(final String content, final String fileName) {
        final File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + getDataFolder() + System.getProperty("file.separator") + fileName);
        try {
            FileUtils.writeStringToFile(file, content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to file: " + fileName, e);
        }
    }

    public static String readFileToString(final String fileName) {
        final File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + getDataFolder() + System.getProperty("file.separator") + fileName);
        String content = null;
        try {
            content = FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read from file: " + fileName, e);
        }
        return content;
    }

    public static String trimSentence(final String sentence, final int noOfWordsToTrimTo) {
        String finalSentence = sentence;
        if (sentence.split(" ").length > noOfWordsToTrimTo) {
            finalSentence = "";
            final String[] words = sentence.split(" ");
            for (int j = 0; j < noOfWordsToTrimTo; ++j) {
                finalSentence = finalSentence + words[j] + " ";
            }
        }
        return finalSentence;
    }

    public static String[][] readExcelData(final String path, final int sheetNo) throws IOException {
        String[][] data = null;
        Sheet sheet = null;
        String value = null;
        Workbook workbook = null;
        try {
            final FileInputStream fis = new FileInputStream(path);
            Reporter.log("Reading excel file: " + path, true);
            if (path.endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                workbook = new XSSFWorkbook(fis);
            }
            sheet = workbook.getSheetAt(sheetNo);
            int temp = 0;
            int colNum = 0;
            final int rowNum = sheet.getLastRowNum() + 1;
            int rowCount = 0;
            for (int j = 0; j < rowNum; ++j) {
                final int lastCell = sheet.getRow(j).getLastCellNum();
                if (sheet.getRow(j).getCell(lastCell - 1).getStringCellValue().trim().equalsIgnoreCase("yes")) {
                    ++rowCount;
                }
                if (sheet.getRow(j) != null) {
                    temp = sheet.getRow(j).getLastCellNum();
                    if (temp > colNum) {
                        colNum = temp;
                    }
                }
            }
            data = new String[rowCount][colNum - 1];
            int currentRowPointer = -1;
            for (int i = 0; i < rowNum; ++i) {
                final int lastCell2 = sheet.getRow(i).getLastCellNum();
                if (sheet.getRow(i) != null && sheet.getRow(i).getCell(lastCell2 - 1).getStringCellValue().trim().equalsIgnoreCase("yes")) {
                    final Row row = sheet.getRow(i);
                    ++currentRowPointer;
                    for (int k = 0; k < colNum - 1; ++k) {
                        final Cell cell = row.getCell(k);
                        if (cell != null) {
                            value = cellToString(cell);
                            data[currentRowPointer][k] = value;
                        }
                    }
                }
            }
            fis.close();
        } catch (Exception e) {
            DataUtil.LOGGER.error("Exception during the Excel processing:: " + e.getMessage());
            e.printStackTrace();
            return data;
        } finally {
            workbook.close();
        }
        workbook.close();
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
                throw new RuntimeException("There is no support for this type of cell: " + cell.getCellType());
            }
        }
        return result.toString();
    }

    public static File getGlobalSettingsFile() {
        File f = null;
        final String dataDir = getDataFolder();
        if (FileUtil.isFileExists(dataDir + System.getProperty("file.separator") + "global-settings.properties")) {
            f = new File(dataDir + System.getProperty("file.separator") + "global-settings.properties");
        } else {
            f = new File(dataDir + System.getProperty("file.separator") + "GlobalProperties.properties");
        }
        return f;
    }

    public static String getDataFolder() {
        String dataDir = null;
        if (new File("Data").exists()) {
            dataDir = "Data";
        } else {
            if (!new File("data").exists()) {
                throw new PegaWebDriverException("Data folder not found in your tests project");
            }
            dataDir = "data";
        }
        return dataDir;
    }
}
