

package com.pega.util;

import org.testng.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class SpreadSheetUtil {
    public static void openAndSaveXMLSpreadSheet(final String xmlFilePath) {
        File vbScriptFilePath = new File("Framework\\resources\\VBScripts\\OpenAndSaveXL.vbs");
        if (!vbScriptFilePath.exists()) {
            vbScriptFilePath = new File("..\\Framework\\resources\\VBScripts\\OpenAndSaveXL.vbs");
        }
        final File xmlFile = new File(xmlFilePath);
        Reporter.log("Formatting the file to be able to read", true);
        Reporter.log("Executing the following command:\ncscript " + vbScriptFilePath.getAbsolutePath() + " " + xmlFile.getAbsolutePath(), true);
        try {
            Runtime.getRuntime().exec("cscript " + vbScriptFilePath.getAbsolutePath() + " " + xmlFile.getAbsolutePath());
            Thread.sleep(120000L);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        Reporter.log("File Converted Successfully", true);
    }

    public static List<String> readXMLSpreadSheet(final String xmlFilePath) throws Exception {
        final Reader reader = new InputStreamReader(new FileInputStream(xmlFilePath), StandardCharsets.UTF_8);
        final BufferedReader br = new BufferedReader(reader);
        final LinkedList<String> englishWords = new LinkedList<String>();
        int i = 1;
        int j = 0;
        String englishWord = null;
        try {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains("<Row")) {
                    i = 1;
                    ++j;
                }
                if (sCurrentLine.contains("<Cell")) {
                    ++i;
                    while (!sCurrentLine.contains("/Cell>")) {
                        sCurrentLine = sCurrentLine + br.readLine();
                    }
                    if (i == 2) {
                        final String test = sCurrentLine;
                        final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                        final Matcher m = p.matcher(test);
                        if (m.find()) {
                            final String word = m.group(1);
                            if (word == null || !word.trim().equals("")) {
                                englishWord = word.replaceAll("&45;", "-");
                            } else {
                                englishWord = "";
                            }
                        } else {
                            englishWord = "";
                        }
                    }
                    if (i != 3) {
                        continue;
                    }
                    final String test = sCurrentLine;
                    final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                    final Matcher m = p.matcher(test);
                    if (m.find()) {
                        final String word = m.group(1);
                        if (word.trim().toLowerCase().contains("external translation")) {
                            Reporter.log("Stopped reading at line: " + j + " and word: " + englishWord, true);
                            break;
                        }
                        englishWords.add(englishWord);
                    } else {
                        englishWords.add(englishWord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return englishWords;
        } finally {
            br.close();
        }
        br.close();
        return englishWords;
    }

    public static void writeXMLSpreadSheet(final List<String> transaltedWords, final String xmlFilePath, final int columnNo) throws Exception {
        /*final Reader reader = new InputStreamReader(new FileInputStream(xmlFilePath), "utf-8");
        final BufferedReader br = new BufferedReader(reader);
        final LinkedList<String> englishWords = new LinkedList<String>();
        final StringBuffer input = new StringBuffer();
        int i = 1;
        String sCurrentLine = null;
        int j = 0;
        String englishWord = null;
        int k = 0;
        String translatedWord = null;
        while (true) {
            try {
                while ((sCurrentLine = br.readLine()) != null) {
                    if (sCurrentLine.contains("<Row")) {
                        i = 1;
                    }
                    if (sCurrentLine.contains("<Cell")) {
                        ++i;
                        while (!sCurrentLine.contains("/Cell>")) {
                            sCurrentLine = String.valueOf(sCurrentLine) + br.readLine();
                        }
                        if (i == 2) {
                            final String test = sCurrentLine;
                            final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                            final Matcher m = p.matcher(test);
                            if (m.find()) {
                                final String word = m.group(1);
                                if (word == null || !word.trim().equals("")) {
                                    englishWord = word;
                                }
                                else {
                                    englishWord = "";
                                }
                            }
                            else {
                                englishWord = "";
                            }
                        }
                        if (i == 3) {
                            if (k != transaltedWords.size()) {
                                translatedWord = transaltedWords.get(k++);
                            }
                            else {
                                Reporter.log("**********translated words doesn't fit the existing size", true);
                            }
                            final String test = sCurrentLine;
                            final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                            final Matcher m = p.matcher(test);
                            if (m.find()) {
                                final String word = m.group(1);
                                if (word.trim().toLowerCase().contains("external translation")) {
                                    Reporter.log("Stopped reading at line: " + j + " and word: " + englishWord, true);
                                    break;
                                }
                                englishWords.add(englishWord);
                            }
                            else {
                                englishWords.add(englishWord);
                            }
                            sCurrentLine = sCurrentLine.replace("<Data ss:Type=\"String\"></Data>", "<Data ss:Type=\"String\">" + translatedWord + "</Data>");
                        }
                    }
                    input.append(String.valueOf(sCurrentLine) + "\n");
                    ++j;
                }
                Reporter.log("Wrting to file", true);
                Thread.sleep(1000L);
            }
            catch (Exception e) {
                e.printStackTrace();
                break;// Label_0472;
            }
            finally {
                br.close();
            }
            Label_0467: {
                break Label_0467;
                do {
                    if (sCurrentLine != null) {
                        input.append(String.valueOf(sCurrentLine) + "\n");
                    }
                    ++j;
                } while ((sCurrentLine = br.readLine()) != null);
                FileUtil.writeToFileInUTF8(xmlFilePath, input.toString());
                return;
            }
            br.close();
            continue;
        }*/
    }
}
