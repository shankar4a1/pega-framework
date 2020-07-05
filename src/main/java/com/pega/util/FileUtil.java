

package com.pega.util;

import com.pega.exceptions.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.model.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.apache.poi.xssf.usermodel.*;
import org.testng.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class FileUtil {


    public FileUtil() {

    }

    public static String readFile(final String filePath) {
        final StringBuffer sb = new StringBuffer();
        final File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
        } catch (FileNotFoundException ex) {
            return sb.toString();
        } finally {
            sc.close();
        }
        sc.close();
        return sb.toString();
    }

    public static void writeToFileInUTF8(final String filePath, final String textToWrite) throws IOException {
        final File fileDir = new File(filePath);
        try {
            Throwable t = null;
            try {
                final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), StandardCharsets.UTF_8));
                try {
                    out.append(textToWrite);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            } finally {
                if (t == null) {
                    final Throwable exception = null;
                    t = exception;
                } else {
                    final Throwable exception = null;
                    if (t != exception) {

                        t.addSuppressed(exception);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            Reporter.log(e.getMessage(), true);
        } catch (IOException e2) {
            Reporter.log(e2.getMessage(), true);
        } catch (Exception e3) {
            Reporter.log(e3.getMessage(), true);
        }
    }

    public static List<String> readFileAsList(String filePath) {
        final List<String> lines = new ArrayList<String>();
        filePath = filePath.replace('\\', File.separatorChar);
        Scanner sc = null;
        final File file = new File(filePath);
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        } catch (FileNotFoundException ex) {
            return lines;
        } finally {
            sc.close();
        }
        sc.close();
        return lines;
    }

    public static void deleteFiles(final String folderPath, final String extn, final String startsWith) {
        final File downloadFolderFile = new File(folderPath);
        if (downloadFolderFile != null && downloadFolderFile.isDirectory()) {
            final String[] fileList = downloadFolderFile.list();
            String[] array;
            for (int length = (array = fileList).length, i = 0; i < length; ++i) {
                final String fileName = array[i];
                if (fileName.endsWith(extn) && fileName.startsWith(startsWith)) {
                    new File(downloadFolderFile + File.separator + fileName).delete();
                    Reporter.log("deleted:" + downloadFolderFile + File.separator + fileName, true);
                }
            }
            return;
        }
        throw new PegaWebDriverException("Unable to validate package download, download path specified either does not exist or not a directory:" + folderPath);
    }

    public static void pollForFile(final String folderPath, final String extn, final String startsWith, final int timeoutInSec, final int intervalInSec) throws InterruptedException {
        int time = 0;
        do {
            Reporter.log("polling after:" + time + " seconds", true);
            if (fileFound(folderPath, extn, startsWith)) {
                Reporter.log("Download Successful", true);
                return;
            }
            Thread.sleep(intervalInSec * 1000);
            time += intervalInSec;
        } while (time < timeoutInSec);
        throw new PegaWebDriverException("Timed out after " + timeoutInSec + " seconds");
    }

    private static boolean fileFound(final String folderPath, final String extn, final String startsWith) {
        boolean fileFound = Boolean.FALSE;
        final File downloadFolderFile = new File(folderPath);
        if (downloadFolderFile != null && downloadFolderFile.isDirectory()) {
            final String[] fileList = downloadFolderFile.list();
            String[] array;
            for (int length = (array = fileList).length, i = 0; i < length; ++i) {
                final String fileName = array[i];
                if (fileName.endsWith(extn) && fileName.startsWith(startsWith)) {
                    Reporter.log("Download successful:" + downloadFolderFile + File.separator + fileName, true);
                    fileFound = Boolean.TRUE;
                }
            }
            return fileFound;
        }
        throw new PegaWebDriverException("Unable to validate package download, download path specified either does not exist or not a directory:" + folderPath);
    }

    public static void copyFile(final String source, final String destination) {
        try {
            final Path dest = Paths.get(destination);
            final Path src = Paths.get(source);
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isFileExists(final String filePath) {
        final Path path = Paths.get(filePath);
        return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isEmpty(final String dirPath) {
        final File file = new File(dirPath);
        return file.list().length <= 0;
    }

    public static void replaceInFile(final String filePath, final String regex, final String replacement) throws IOException {
        final File file = new File(filePath);
        final File tempFile = File.createTempFile("buffer", ".tmp");
        final FileWriter fw = new FileWriter(tempFile);
        final Reader fr = new FileReader(file);
        Label_0197:
        {
            try {
                Throwable t = null;
                try {
                    final BufferedReader br = new BufferedReader(fr);
                    try {
                        while (br.ready()) {
                            fw.write(br.readLine().replaceAll(regex, replacement).replaceAll("&#25;", ""));
                        }
                    } finally {
                        if (br != null) {
                            br.close();
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
                e.printStackTrace();
                break Label_0197;
            } finally {
                fw.close();
                fr.close();
            }
            fw.close();
            fr.close();
        }
        if (file.exists()) {
            System.out.println("Deleting the existing file");
            System.out.println("File Path: " + file.getAbsolutePath() + file.getCanonicalPath());
            Files.delete(FileSystems.getDefault().getPath(file.getAbsolutePath()));
        }
        System.out.println("Text replaced :" + regex);
        tempFile.renameTo(file);
    }

    public static void zip(final String targetFolderOrFileToZip_Path, final String zipFilePath, final boolean includeTopLevelDirectory, final String password) throws IOException {
        try {
            final ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(8);
            parameters.setCompressionLevel(5);
            if (password != null && password.length() > 0) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(99);
                parameters.setAesKeyStrength(3);
                parameters.setPassword(password);
            }
            final File zip = new File(zipFilePath);
            if (zip.exists()) {
                Files.delete(FileSystems.getDefault().getPath(zipFilePath));
            }
            final ZipFile zipFile = new ZipFile(zipFilePath);
            final File targetFile = new File(targetFolderOrFileToZip_Path);
            if (targetFile.isFile()) {
                zipFile.addFile(targetFile, parameters);
            } else if (targetFile.isDirectory()) {
                if (includeTopLevelDirectory) {
                    zipFile.addFolder(targetFile, parameters);
                } else {
                    File[] listFiles;
                    for (int length = (listFiles = targetFile.listFiles()).length, i = 0; i < length; ++i) {
                        final File file = listFiles[i];
                        if (file.isFile()) {
                            zipFile.addFile(file, parameters);
                        } else if (file.isDirectory()) {
                            zipFile.addFolder(file, parameters);
                        }
                    }
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static void unzip(final String zipFilePath, final String destinationFolderToUnZip_Path, final String password) {
        try {
            final ZipFile zipFile = new ZipFile(zipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destinationFolderToUnZip_Path);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static int getCSVRowCount(final String fileName) throws IOException {
        Scanner scanIn = null;
        int numberOfRows = 0;
        final String file = System.getProperty("user.dir") + File.separator + DataUtil.getDataFolder() + File.separator + fileName;
        scanIn = new Scanner(new BufferedReader(new FileReader(file)));
        while (scanIn.hasNextLine()) {
            scanIn.nextLine();
            ++numberOfRows;
        }
        Reporter.log("Total number of rows : " + numberOfRows, true);
        scanIn.close();
        return numberOfRows;
    }

    public static int getCSVColCount(final String fileName, final String delimiter) throws FileNotFoundException {
        Scanner scanIn = null;
        String inputLine = "";
        int numberOfColums = 0;
        final String file = System.getProperty("user.dir") + File.separator + DataUtil.getDataFolder() + File.separator + fileName;
        scanIn = new Scanner(new BufferedReader(new FileReader(file)));
        inputLine = scanIn.nextLine();
        final String[] array = inputLine.split(delimiter);
        numberOfColums = array.length;
        Reporter.log("Total number of cols : " + numberOfColums, true);
        scanIn.close();
        return numberOfColums;
    }

    public static String getCSVData(final String fileName, final int rowNumber, final int columnNumber, final String delimiter) throws FileNotFoundException {
        Scanner scanIn = null;
        String inputLine = "";
        String output = null;
        final String file = System.getProperty("user.dir") + File.separator + DataUtil.getDataFolder() + File.separator + fileName;
        scanIn = new Scanner(new BufferedReader(new FileReader(file)));
        int num = 0;
        while (scanIn.hasNextLine()) {
            ++num;
            inputLine = scanIn.nextLine();
            if (num == rowNumber + 1) {
                final String[] array = inputLine.split(delimiter);
                output = array[columnNumber];
                break;
            }
        }
        scanIn.close();
        return output;
    }

    public static void setCSVData(final String fileName, final int rowNumber, final int columnNumber, final String newValue, final String delimiter) throws IOException {
        final String output = getCSVData(fileName, rowNumber, columnNumber, delimiter);
        final File file = new File(System.getProperty("user.dir") + File.separator + DataUtil.getDataFolder() + File.separator + fileName);
        final List<String> lines = FileUtils.readLines(file);
        String[] arrayvalues = new String[0];
        if (lines != null && lines.get(rowNumber) != null && lines.get(rowNumber).contains(output + ",")) {
            arrayvalues = lines.get(rowNumber).split(",");
            if (arrayvalues[columnNumber].contains(output)) {
                arrayvalues[columnNumber] = arrayvalues[columnNumber].replace(output, newValue);
            }
        }
        lines.remove(rowNumber);
        lines.add(rowNumber, StringUtils.join(arrayvalues, ","));
        FileUtils.writeLines(file, lines);
    }

    public static void writeCsvFile(final String fileName, final List<List<String>> data, final List<String> header) {
        final String COMMA_DELIMITER = ",";
        final String NEW_LINE_SEPARATOR = "\n";
        try {
            Throwable t = null;
            try {
                final FileWriter fileWriter = new FileWriter(fileName);
                try {
                    String csvRow = "";
                    for (final String csvData : header) {
                        csvRow = csvRow + csvData + COMMA_DELIMITER;
                    }
                    csvRow = csvRow.substring(0, csvRow.length() - 1);
                    fileWriter.append(csvRow);
                    fileWriter.append(NEW_LINE_SEPARATOR);
                    for (final List<String> list : data) {
                        csvRow = "";
                        for (final String csvData2 : list) {
                            csvRow = csvRow + csvData2 + COMMA_DELIMITER;
                        }
                        csvRow = csvRow.substring(0, csvRow.length() - 1);
                        fileWriter.append(csvRow);
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                    System.out.println("CSV file was created successfully !!!");
                } finally {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                }
            } finally {
                if (t == null) {
                    final Throwable exception = new Throwable();
                    t = exception;
                } else {
                    final Throwable exception = null;
                    if (t != exception) {
                        t.addSuppressed(exception);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
            return;
        } finally {
            final File file = new File(fileName);
            try {
                System.out.println("***************************FilePath: " + file.getCanonicalPath());
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            System.out.println("***************************FilePath: " + file.getAbsolutePath());
        }
        final File file = new File(fileName);
        try {
            System.out.println("***************************FilePath: " + file.getCanonicalPath());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        System.out.println("***************************FilePath: " + file.getAbsolutePath());
    }

    public static void copySheet(final String srcFilePath, final String destFilePath, final String sheetName) {
        XSSFWorkbook oldWB = null;
        try {
            Throwable t = null;
            try {
                final XSSFWorkbook newWB = new XSSFWorkbook(new FileInputStream(new File(destFilePath)));
                try {
                    oldWB = new XSSFWorkbook(new FileInputStream(new File(srcFilePath)));
                    final XSSFCellStyle newStyle = newWB.createCellStyle();
                    final XSSFSheet sheetFromOldWB = oldWB.getSheet(sheetName);
                    final XSSFSheet sheetForNewWB = newWB.createSheet(sheetFromOldWB.getSheetName());
                    for (int rowIndex = 0; rowIndex < sheetFromOldWB.getPhysicalNumberOfRows(); ++rowIndex) {
                        final XSSFRow row = sheetForNewWB.createRow(rowIndex);
                        for (int colIndex = 0; colIndex < sheetFromOldWB.getRow(rowIndex).getPhysicalNumberOfCells(); ++colIndex) {
                            final XSSFCell cell = row.createCell(colIndex);
                            final XSSFCell c = sheetFromOldWB.getRow(rowIndex).getCell(colIndex, XSSFRow.CREATE_NULL_AS_BLANK);
                            if (c.getCellType() != 3) {
                                final XSSFCellStyle origStyle = c.getCellStyle();
                                newStyle.cloneStyleFrom(origStyle);
                                cell.setCellStyle(newStyle);
                                switch (c.getCellType()) {
                                    case 1: {
                                        cell.setCellValue(c.getRichStringCellValue().getString());
                                        break;
                                    }
                                    case 0: {
                                        cell.setCellValue(c.getNumericCellValue());
                                        break;
                                    }
                                    case 4: {
                                        cell.setCellValue(c.getBooleanCellValue());
                                        break;
                                    }
                                    case 2: {
                                        cell.setCellValue(c.getCellFormula());
                                        break;
                                    }
                                    case 3: {
                                        cell.setCellValue("");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    final FileOutputStream fileOut = new FileOutputStream(destFilePath);
                    newWB.write(fileOut);
                    fileOut.close();
                } finally {
                    if (newWB != null) {
                        newWB.close();
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                oldWB.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            oldWB.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public static List<List<String>> readSheet(final String excelFilePath, final int sheetIndex, final int noOfHeaderRowsToIgnore, final boolean ignoreEmptyCells) {
        final List<List<String>> list = new ArrayList<List<String>>();
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(new File(excelFilePath)));
            final XSSFSheet sheet = wb.getSheetAt(sheetIndex);
            for (int rowIndex = noOfHeaderRowsToIgnore; rowIndex < sheet.getPhysicalNumberOfRows(); ++rowIndex) {
                final List<String> list2 = new ArrayList<String>();
                Object value = "";
                for (int colIndex = 0; colIndex < sheet.getRow(rowIndex).getPhysicalNumberOfCells(); ++colIndex) {
                    final XSSFCell c = sheet.getRow(rowIndex).getCell(colIndex, XSSFRow.CREATE_NULL_AS_BLANK);
                    value = "";
                    if (c.getCellType() != 3) {
                        switch (c.getCellType()) {
                            case 1: {
                                value = c.getRichStringCellValue().getString();
                                break;
                            }
                            case 0: {
                                value = c.getNumericCellValue();
                                break;
                            }
                            case 4: {
                                value = c.getBooleanCellValue();
                                break;
                            }
                            case 2: {
                                value = c.getCellFormula();
                                break;
                            }
                            default: {
                                value = "";
                                break;
                            }
                        }
                        if (value.toString().trim().equals("")) {
                            if (!ignoreEmptyCells) {
                                list2.add(new StringBuilder().append(value).toString());
                            }
                        } else {
                            list2.add(new StringBuilder().append(value).toString());
                        }
                    }
                    if (!ignoreEmptyCells) {
                        list2.add("");
                    }
                }
                list.add(list2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            wb.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return list;
    }

    public static void deleteSheet(final String excelFilePath, final int sheetIndex) throws IOException {
        final File excelFile = new File(excelFilePath);
        XSSFWorkbook book = null;
        try {
            book = new XSSFWorkbook(new FileInputStream(excelFile));
            book.removeSheetAt(sheetIndex);
            book.write(new FileOutputStream(excelFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            book.close();
        }
        book.close();
    }

    public static File getLatestFile(final File dir) {
        final File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("There were no files in :" + dir.getName());
            return null;
        }
        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; ++i) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }

    public static void writeToFileFromString(final String filepath, final String content) throws IOException {
        Writer output = null;
        try {
            final File file = new File(filepath);
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
            System.out.println("File has been written");
        } catch (Exception e) {
            System.out.println("Could not create file");
            return;
        } finally {
            output.close();
        }
        output.close();
    }

    public static Properties loadPropertiesFile(final String filepath) {
        try {
            final Properties props = new Properties();
            props.load(new FileReader(filepath));
            return props;
        } catch (Exception e) {
            System.out.println("Could not load the given properties file. Following exception occured\n" + e.getMessage());
            return null;
        }
    }
}
