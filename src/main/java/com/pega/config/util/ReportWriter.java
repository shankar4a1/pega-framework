

package com.pega.config.util;

import com.pega.util.*;
import net.sf.json.*;
import org.apache.commons.io.*;
import org.testng.*;

import java.io.*;
import java.util.*;

public class ReportWriter {


    static List<File> filesList;

    static {
        ReportWriter.filesList = new LinkedList<File>();
    }

    public ReportWriter() {

    }

    private static void parseFolders(final String dirPath) throws IOException {
        final File file = new File(dirPath);
        for (final File currFile : file.listFiles()) {
            if (currFile.isDirectory()) {
                parseFolders(currFile.getAbsolutePath());
            } else if (currFile.getName().endsWith(".json")) {
                parseFile(currFile);
            }
        }
    }

    public static void parseFile(final File f) throws IOException {
        ReportWriter.filesList.add(f);
    }

    public static void main(final String[] args) {
        try {
            parseFolders(System.getenv("WORKSPACE") + System.getProperty("file.separator") + "LatestReports");
            combineReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void combineReport() throws IOException {
        final File f1 = new File("AggregatedReports");
        if (f1.exists()) {
            f1.delete();
        }
        f1.mkdir();
        File[] files = new File[ReportWriter.filesList.size()];
        files = ReportWriter.filesList.toArray(files);
        JSONArray report = new JSONArray();
        if (files.length > 0) {
            JSONArray jsonFirst = null;
            try {
                jsonFirst = (JSONArray) JSONSerializer.toJSON(FileUtils.readFileToString(files[0]));
            } catch (Exception e) {
                jsonFirst = new JSONArray();
                Reporter.log("Error reading report file: " + files[0].getName(), true);
            }
            report = jsonFirst;
            Reporter.log("Total json files are:" + files.length, true);
            for (int i = 1; i < files.length; ++i) {
                JSONArray json;
                try {
                    json = (JSONArray) JSONSerializer.toJSON(FileUtils.readFileToString(files[i]));
                } catch (Exception e2) {
                    Reporter.log("Error reading report file: " + files[i].getName(), true);
                    continue;
                }
                if (json != null && !json.isEmpty()) {
                    final JSONObject obj = (JSONObject) json.get(0);
                    final String featureName = obj.get("id").toString();
                    JSONObject obj2 = null;
                    boolean exists = false;
                    String existingFeatureName = "";
                    for (int j = 0; j < report.size(); ++j) {
                        obj2 = (JSONObject) report.get(j);
                        existingFeatureName = obj2.get("id").toString();
                        if (featureName.equals(existingFeatureName)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        final JSONArray elementsInReport = (JSONArray) obj2.get("elements");
                        final JSONArray elements = (JSONArray) obj.get("elements");
                        final int size = (elements != null) ? elements.size() : 0;
                        System.out.println("Size : " + size);
                        for (int index = 0; index < size; ++index) {
                            elementsInReport.add(elements.get(index));
                        }
                    } else {
                        report.add(json.get(0));
                    }
                }
            }
            final PrintWriter writer = new PrintWriter(new File(f1.getAbsolutePath() + System.getProperty("file.separator") + GlobalConstants.CONSOLIDATED_REPORT_NAME));
            writer.println(report.toString());
            writer.close();
        }
    }
}
