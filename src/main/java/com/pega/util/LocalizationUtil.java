

package com.pega.util;

import com.pega.exceptions.*;
import com.pega.framework.*;
import org.apache.commons.lang3.*;
import org.openqa.selenium.*;
import org.testng.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class LocalizationUtil {

    public static final String language;
    private static String languageBundlePath;
    protected static Properties translatedProperties;
    private static String xmlFilePath;
    private static HashMap<String, String> translationsMap;

    static {
        language = System.getProperty("l10n.language");
        LocalizationUtil.languageBundlePath = System.getProperty("l10n.bundle.path");
        LocalizationUtil.translatedProperties = null;
        LocalizationUtil.xmlFilePath = null;
        LocalizationUtil.translationsMap = null;
    }

    public static void translateWords(final String sourceXMLPath, final String targetXMLPath, final HTTPUtil.TransaltionLang lang, final String languageBundlePath) throws Exception {
        int totalRequests = 0;
        final List<String> translatedTextList = new LinkedList<String>();
        final String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=" + lang.getLang() + "&dt=t&q=";
        final List<String> wordsToConvert = SpreadSheetUtil.readXMLSpreadSheet(sourceXMLPath);
        Reporter.log("Total Words to convert: " + wordsToConvert.size(), true);
        boolean repeat = true;
        int i = 0;
        while (repeat) {
            Reporter.log("Current i position: " + i, true);
            String word = wordsToConvert.get(i++);
            String wordToConvert = null;
            final List pyExcludedWords = new LinkedList();
            if (word.contains("({.py") || word.contains("{.py")) {
                String regex = "";
                if (word.contains("({.py")) {
                    regex = "(\\(\\{.+?\\}\\))";
                } else if (word.contains("{.py")) {
                    regex = "(\\{.+?\\})";
                }
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(word);
                String pyText = "";
                wordToConvert = word;
                while (matcher.find()) {
                    final String match = matcher.group(1);
                    pyText = pyText + match + " ";
                    wordToConvert = wordToConvert.replace(match, "");
                }
                pyExcludedWords.add(pyText.trim());
            } else if (word.contains(".py") || word.startsWith("py")) {
                if (word.startsWith("py") || (word.startsWith(".py") && !word.contains(" "))) {
                    if (word.split(" ").length > 1) {
                        wordToConvert = word.split(" ")[1];
                    } else {
                        wordToConvert = "";
                    }
                    pyExcludedWords.add(word.split(" ")[0]);
                } else {
                    final Pattern pattern2 = Pattern.compile("(\\.py.*?) ");
                    final Matcher matcher2 = pattern2.matcher(word);
                    String pyText2 = "";
                    wordToConvert = word;
                    while (matcher2.find()) {
                        final String match2 = matcher2.group(1);
                        pyText2 = pyText2 + match2 + " ";
                        wordToConvert = wordToConvert.replace(match2, "");
                    }
                    pyExcludedWords.add(pyText2.trim());
                }
            } else {
                wordToConvert = DataUtil.trimSentence(word, 5);
                pyExcludedWords.add("");
            }
            if (wordToConvert.trim().equals("")) {
                wordToConvert = ":";
            }
            String currentWord = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(wordToConvert), ' ').trim();
            String newUrl = url + URLEncoder.encode(currentWord.replaceAll(" +", " ").replaceAll("\\.", ":").replaceAll("& nbsp ;", ":").replaceAll(";", ":").replaceAll("&", ":").replaceAll("\\\t", "").replaceAll("\\\n", "").replaceAll("#", ":").replaceAll("\\|", ":").replaceAll(";", ":").replaceAll("\\?", ":").replaceAll("!", ":"), "UTF-8");
            while (i < wordsToConvert.size()) {
                word = wordsToConvert.get(i);
                wordToConvert = null;
                if (word.contains("({.py") || word.contains("{.py")) {
                    String regex2 = "";
                    if (word.contains("({.py")) {
                        regex2 = "(\\(\\{.+?\\}\\))";
                    } else if (word.contains("{.py")) {
                        regex2 = "(\\{.+?\\})";
                    }
                    final Pattern pattern3 = Pattern.compile(regex2);
                    final Matcher matcher3 = pattern3.matcher(word);
                    String pyText3 = "";
                    wordToConvert = word;
                    while (matcher3.find()) {
                        final String match3 = matcher3.group(1);
                        pyText3 = pyText3 + match3 + " ";
                        wordToConvert = wordToConvert.replace(match3, "");
                    }
                    pyExcludedWords.add(pyText3.trim());
                } else if (word.contains(".py") || word.startsWith("py")) {
                    if (word.startsWith("py") || (word.startsWith(".py") && !word.contains(" "))) {
                        if (word.split(" ").length > 1) {
                            wordToConvert = word.split(" ")[1];
                        } else {
                            wordToConvert = "";
                        }
                        pyExcludedWords.add(word.split(" ")[0]);
                    } else {
                        final int totalPys = word.split(".py").length - 1;
                        final Pattern pattern3 = Pattern.compile("(\\.py.*?) ");
                        final Matcher matcher3 = pattern3.matcher(word);
                        String pyText3 = "";
                        wordToConvert = word;
                        while (matcher3.find()) {
                            final String match3 = matcher3.group(1);
                            pyText3 = pyText3 + match3 + " ";
                            wordToConvert = wordToConvert.replace(match3, "");
                        }
                        if (pyText3 == null || pyText3.trim().equals("") || pyText3.split(" ").length != totalPys) {
                            pyText3 = ".py" + wordToConvert.split("\\.py")[1];
                            wordToConvert = wordToConvert.split("\\.py")[0];
                        }
                        pyExcludedWords.add(pyText3.trim());
                    }
                } else {
                    wordToConvert = DataUtil.trimSentence(word, 5);
                    pyExcludedWords.add("");
                }
                if (wordToConvert.trim().equals("")) {
                    wordToConvert = ":";
                }
                currentWord = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(wordToConvert), ' ').trim();
                newUrl = newUrl + "%0A" + URLEncoder.encode(currentWord.replaceAll(" +", " ").replaceAll("\\.", ":").replaceAll("& nbsp ;", ":").replaceAll(";", ":").replaceAll("&", ":").replaceAll("\\\t", "").replaceAll("\\\n", "").replaceAll("#", ":").replaceAll("\\|", ":").replaceAll(";", ":").replaceAll("\\?", ":").replaceAll("!", ":"), "UTF-8");
                Reporter.log("Current i position: " + i, true);
                if (i % 25 == 0) {
                    ++i;
                    break;
                }
                ++i;
            }
            newUrl = newUrl.replaceAll("\\\t", "");
            Reporter.log("\nURL to hit is below:", true);
            Reporter.log(newUrl, true);
            ++totalRequests;
            try {
                final String response = HTTPUtil.getResponseText(newUrl, "");
                Reporter.log(response, true);
                final Pattern pattern3 = Pattern.compile("\\[\"(.+?)\",\"");
                final Matcher matcher3 = pattern3.matcher(response);
                int k = 0;
                while (matcher3.find()) {
                    String translatedText = matcher3.group(1).replaceAll("\\\\n", "").replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">").replaceAll("\\\\\"", "\"").replaceAll(": quot: ", "\"").replaceAll("quot", "\"").replaceAll(": Quot: ", "\"").replaceAll(": Amp: ", "&").replaceAll("Amp", "&").replaceAll("Quot", "\"");
                    if (translatedText.trim().equalsIgnoreCase(":")) {
                        translatedText = "";
                    }
                    translatedTextList.add((translatedText + " " + pyExcludedWords.get(k++)).trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i >= wordsToConvert.size()) {
                repeat = false;
            }
        }
        Reporter.log("Total Requests Fired: " + totalRequests, true);
        Reporter.log("Total Words converted: " + translatedTextList.size(), true);
        SpreadSheetUtil.writeXMLSpreadSheet(translatedTextList, targetXMLPath, 1);
        if (languageBundlePath != null) {
            if (wordsToConvert.size() != translatedTextList.size()) {
                throw new RuntimeException("translated and original list does not have same size. Can not proceed with test");
            }
            final Properties p = new Properties();
            for (int index = 0; index < wordsToConvert.size(); ++index) {
                p.setProperty(wordsToConvert.get(index), translatedTextList.get(index));
            }
            p.store(new OutputStreamWriter(new FileOutputStream(languageBundlePath)), "translated bundle");
        }
    }

    private static Set<String> splitAndFind(final String text) {
        final Set<String> nonLocalizedWords = new HashSet<String>();
        final FileInputStream inputStream = null;
        try {
            final String[] sentences = text.split("\n");
            final List<String> TranslatedWords = getTranslatedWords();
            final Set<String> dictionaryData = getEnglishDictionary();
            final List<String> excludedEnglishWords = getExcludedEnglishWords();
            String[] array;
            for (int length = (array = sentences).length, i = 0; i < length; ++i) {
                String sentence = array[i];
                sentence = sentence.trim();
                Reporter.log("---------------------Current Sentence:" + sentence, true);
                if (!TranslatedWords.contains(sentence) && !excludedEnglishWords.contains(sentence)) {
                    Reporter.log("---------------------Not Excluded Sentence:" + sentences, true);
                    Reporter.log(sentence, true);
                    final String[] words = sentence.split(" ");
                    String[] array2;
                    for (int length2 = (array2 = words).length, j = 0; j < length2; ++j) {
                        final String word = array2[j];
                        if (!excludedEnglishWords.contains(word) && dictionaryData.contains(word.toLowerCase())) {
                            nonLocalizedWords.add(sentence);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return nonLocalizedWords;
            }
            return nonLocalizedWords;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return nonLocalizedWords;
    }

    private static Set<String> getEnglishDictionary() {
        final Set<String> dictionaryData = new HashSet<String>();
        Scanner file1 = null;
        try {
            file1 = new Scanner(new File(System.getProperty("user.dir") + "/Data/EnglishDictionary.txt"));
            while (file1.hasNext()) {
                dictionaryData.add(file1.next().trim().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return dictionaryData;
        } finally {
            file1.close();
        }
        file1.close();
        return dictionaryData;
    }

    private static List<String> getExcludedEnglishWords() {
        final List<String> excludedWords = new LinkedList<String>();
        return excludedWords;
    }

    private static List<String> getTranslatedWords() throws IOException {
        final LinkedList<String> translatedWords = new LinkedList<String>();
        BufferedReader br = null;
        try {
            int i = 1;
            final String fileName = getTranslationfilepath();
            if (fileName.endsWith(".xml")) {
                final Reader reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
                br = new BufferedReader(reader);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    if (sCurrentLine.contains("<Row>")) {
                        i = 1;
                    }
                    if (sCurrentLine.contains("</Cell>") && ++i == 3) {
                        final String test = sCurrentLine;
                        final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                        final Matcher m = p.matcher(test);
                        while (m.find()) {
                            final String word = m.group(1);
                            translatedWords.add(word);
                        }
                    }
                }
            } else {
                try {
                    getTranslatedProperties().load(new InputStreamReader(new FileInputStream(new File(fileName)), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    throw new RuntimeException("Problem while reading transalted properties file.", e);
                }
                final Iterator<Object> iter = getTranslatedProperties().values().iterator();
                while (iter.hasNext()) {
                    translatedWords.add(iter.next().toString());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return translatedWords;
        } finally {
            br.close();
        }
        br.close();
        return translatedWords;
    }

    public static Set<String> getNonLocalizedWords(final PegaWebElement pegaWebElement) {
        final String text = pegaWebElement.getText();
        return splitAndFind(text);
    }

    public static Set<String> getNonLocalizedWords(final PegaWebDriver driver) {
        final String text = driver.findElement(By.tagName("body")).getText();
        return splitAndFind(text);
    }

    public static boolean isLocalized(final PegaWebElement pegaWebElement) {
        return getNonLocalizedWords(pegaWebElement).isEmpty();
    }

    public static boolean isLocalized(final PegaWebDriver pegaWebDriver) {
        return getNonLocalizedWords(pegaWebDriver).isEmpty();
    }

    public static void verifyNonLocalizedWords(final PegaWebElement pegaWebElement) {
        final String text = pegaWebElement.getText();
        parse(text);
    }

    private static void parse(String text) {
        String message = "";
        if (text.contains("\\*")) {
            text = text.replaceAll("\\*", "");
        }
        final Set<String> nonLocalWords = splitAndFind(text);
        if (nonLocalWords.size() > 0) {
            message = message + "\n\nFollowing words are not localized: ";
            message = message + "\n+--------------------------------------------+\n";
            final Iterator iterator = nonLocalWords.iterator();
            while (iterator.hasNext()) {
                message = message + iterator.next() + "\n";
            }
            message = message + "+--------------------------------------------+\n";
            message = message + "End of non Localized words list\n";
        }
        if (!message.equals("")) {
            throw new NonLocalizedWordsException(message);
        }
    }

    public static void verifyNonLocalizedWords(final PegaWebDriver driver) {
        final String text = driver.findElement(By.tagName("body")).getText();
        parse(text);
    }

    private static Map<String, String> getTranslationsMap() throws IOException {
        if (LocalizationUtil.translationsMap == null) {
            LocalizationUtil.translationsMap = new HashMap<String, String>();
            if (LocalizationUtil.languageBundlePath.endsWith(".properties")) {
                for (final String key : getTranslatedProperties().stringPropertyNames()) {
                    final String value = LocalizationUtil.translatedProperties.getProperty(key);
                    LocalizationUtil.translationsMap.put(key, value);
                }
            } else {
                BufferedReader br = null;
                try {
                    final Reader reader = new InputStreamReader(new FileInputStream(getTranslationfilepath()), StandardCharsets.UTF_8);
                    br = new BufferedReader(reader);
                    int i = 1;
                    String englishWord = null;
                    String sCurrentLine;
                    while ((sCurrentLine = br.readLine()) != null) {
                        if (sCurrentLine.contains("<Row")) {
                            i = 1;
                        }
                        if (sCurrentLine.contains("<Cell")) {
                            ++i;
                            while (!sCurrentLine.contains("/Cell>") && !sCurrentLine.contains("/>")) {
                                sCurrentLine = sCurrentLine + br.readLine();
                            }
                            if (i == 2) {
                                final String test = sCurrentLine;
                                final Pattern p = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                                final Matcher m = p.matcher(test);
                                if (m.find()) {
                                    final String word = m.group(1);
                                    if (word == null || !word.trim().equals("")) {
                                        englishWord = word;
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
                            String translatedWord = null;
                            final String test2 = sCurrentLine;
                            final String nowordpattern1 = "<Cell ss:StyleID=\"s64\"/>";
                            final String nowordpattern2 = "<Cell ss:StyleID=\"s64\"><Data ss:Type=\"String\"></Data></Cell>";
                            if (test2.trim().contains(nowordpattern1) || test2.trim().contains(nowordpattern2)) {
                                translatedWord = englishWord;
                                LocalizationUtil.translationsMap.put(englishWord, translatedWord);
                            } else {
                                final Pattern p2 = Pattern.compile("ss:Type[^>]*>(.*?)</Data>");
                                final Matcher j = p2.matcher(test2);
                                if (!j.find()) {
                                    continue;
                                }
                                translatedWord = j.group(1);
                                if (translatedWord.trim().toLowerCase().contains("external translation")) {
                                    translatedWord = englishWord;
                                    LocalizationUtil.translationsMap.put(englishWord, translatedWord);
                                } else {
                                    LocalizationUtil.translationsMap.put(englishWord, translatedWord);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalizationUtil.translationsMap;
                } finally {
                    br.close();
                }
                br.close();
            }
        }
        return LocalizationUtil.translationsMap;
    }

    private static String getTranslationfilepath() {
        if (!LocalizationUtil.languageBundlePath.endsWith(".properties")) {
            if (LocalizationUtil.xmlFilePath == null && !LocalizationUtil.language.equalsIgnoreCase(HTTPUtil.TransaltionLang.ENGLISH.getLang())) {
                LocalizationUtil.translatedProperties = new Properties();
                LocalizationUtil.languageBundlePath = LocalizationUtil.languageBundlePath.replace("<LANG>", LocalizationUtil.language);
                LocalizationUtil.languageBundlePath = System.getProperty("user.dir") + System.getProperty("file.separator") + DataUtil.getDataFolder() + System.getProperty("file.separator") + LocalizationUtil.languageBundlePath;
                LocalizationUtil.xmlFilePath = LocalizationUtil.languageBundlePath;
            }
            return LocalizationUtil.xmlFilePath;
        }
        if (LocalizationUtil.translatedProperties != null) {
            return LocalizationUtil.languageBundlePath;
        }
        initializeTranslatedProperties();
        return LocalizationUtil.languageBundlePath;
    }

    public static void main(final String[] args) {
        final String str = "abc/xyz/asds//fewrer";
        System.out.println(str);
        System.out.println(str.replace("//", "/"));
    }

    public static Properties getTranslatedProperties() {
        if (LocalizationUtil.translatedProperties != null) {
            return LocalizationUtil.translatedProperties;
        }
        initializeTranslatedProperties();
        return LocalizationUtil.translatedProperties;
    }

    private static void initializeTranslatedProperties() {
        if (!LocalizationUtil.language.equalsIgnoreCase(HTTPUtil.TransaltionLang.ENGLISH.getLang())) {
            LocalizationUtil.translatedProperties = new Properties();
            LocalizationUtil.languageBundlePath = LocalizationUtil.languageBundlePath.replace("<LANG>", LocalizationUtil.language);
            LocalizationUtil.languageBundlePath = System.getProperty("user.dir") + System.getProperty("file.separator") + DataUtil.getDataFolder() + System.getProperty("file.separator") + LocalizationUtil.languageBundlePath;
            LocalizationUtil.languageBundlePath = LocalizationUtil.languageBundlePath.replace("//", "/");
            try {
                System.out.println("Properties File Path: " + LocalizationUtil.languageBundlePath);
                LocalizationUtil.translatedProperties.load(new InputStreamReader(new FileInputStream(new File(LocalizationUtil.languageBundlePath)), StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException("Problem while reading transalted properties file.", e);
            }
        }
    }

    public static String getLocalizedWord(final String word2localize) {
        if (!LocalizationUtil.language.equalsIgnoreCase(HTTPUtil.TransaltionLang.ENGLISH.getLang())) {
            try {
                if (getTranslationsMap().get(word2localize) == null) {
                    return word2localize;
                }
                return getTranslationsMap().get(word2localize);
            } catch (IOException e) {
                e.printStackTrace();
                return word2localize;
            }
        }
        return word2localize;
    }

    public static void doLocalizedAssert(final boolean result, final String message) {
    }

    public static String getLocalizedXpath(final String l10nMyCasesTableHeaderXpath, final String englishTableHeader) {
        return null;
    }

    public static String getSeleniumCompatibleWord(final String localizedText) {
        if (localizedText != null) {
            return StringEscapeUtils.escapeJava(localizedText);
        }
        return localizedText;
    }

    public static String getTranslatedWord(final String word2Translate) {
        if (LocalizationUtil.language != null && !LocalizationUtil.language.equalsIgnoreCase(HTTPUtil.TransaltionLang.ENGLISH.getLang())) {
            final String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=" + LocalizationUtil.language + "&dt=t&q=" + word2Translate.replace(" ", "%20");
            String translatedWord = word2Translate;
            try {
                System.out.println(url);
                translatedWord = HTTPUtil.getResponseText(url, "");
                final Pattern pattern = Pattern.compile("\\[\"(.+?)\",\"");
                final Matcher matcher = pattern.matcher(translatedWord);
                while (matcher.find()) {
                    String translatedText = new String(matcher.group(1).replaceAll("\\\\n", "").replaceAll("\\\\u003c", "<").replaceAll("\\\\u003e", ">").replaceAll("\\\\\"", "\"").getBytes(), StandardCharsets.UTF_8);
                    if (translatedText.equalsIgnoreCase("n / a")) {
                        translatedText = "";
                    }
                    translatedWord = translatedText;
                }
            } catch (IOException e) {
                Reporter.log("getTranslatedWord failed, exception: " + e.getMessage(), true);
                e.printStackTrace();
            }
            return translatedWord;
        }
        return word2Translate;
    }

    public static String getJavaCompatibleWord(final String localizedText) {
        if (localizedText != null) {
            return StringEscapeUtils.unescapeJava(localizedText);
        }
        return localizedText;
    }

    public static void createPropertiesFileFromXML(final String xmlFilePath, final String propertiesFilePath) throws IOException {
        final HashMap<String, String> translateMap = new HashMap<String, String>();
        BufferedReader br = null;
        Label_0345:
        {
            try {
                final Reader reader = new InputStreamReader(new FileInputStream(xmlFilePath), StandardCharsets.UTF_8);
                br = new BufferedReader(reader);
                int i = 1;
                int j = 0;
                String englishWord = null;
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
                                    englishWord = word;
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
                        if (!m.find()) {
                            continue;
                        }
                        final String translatedWord = m.group(1);
                        if (translatedWord.trim().toLowerCase().contains("external translation")) {
                            Reporter.log("Stopped reading at line: " + j + " and word: " + englishWord, true);
                            break;
                        }
                        translateMap.put(englishWord, translatedWord);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break Label_0345;
            } finally {
                br.close();
            }
            br.close();
        }
        final Properties p2 = new Properties();
        for (final String key : translateMap.keySet()) {
            p2.setProperty(key, translateMap.get(key));
        }
        try {
            p2.store(new OutputStreamWriter(new FileOutputStream(propertiesFilePath), StandardCharsets.UTF_8), "translated bundle");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
