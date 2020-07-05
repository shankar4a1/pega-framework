

package com.pega.config.util;

import org.apache.commons.io.*;

import java.io.*;

public class ChangeFilePermission {


    public ChangeFilePermission() {

    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        File scriptsDir = new File(System.getenv("WORKSPACE") + File.separator + "Tests" + File.separator + "scripts");
        final File newScriptsDir = new File(System.getenv("WORKSPACE") + File.separator + "Tests" + File.separator + "new_scripts");
        if (!newScriptsDir.exists()) {
            newScriptsDir.mkdirs();
        }
        final File[] filesList = scriptsDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File pathname) {
                return pathname.getName().toLowerCase().endsWith("sh");
            }
        });
        BufferedReader reader = null;
        PrintWriter writer = null;
        System.out.println("Length: " + filesList.length);
        for (int i = 0; i < filesList.length; ++i) {
            final File f = filesList[i];
            final File f2 = new File(newScriptsDir.getAbsolutePath() + File.separator + f.getName());
            f2.createNewFile();
            try {
                writer = new PrintWriter(new FileOutputStream(f2));
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line.trim());
                }
            } catch (Exception e) {
                System.out.println("Exception is  " + e);
                continue;
            } finally {
                writer.close();
                reader.close();
            }
            writer.close();
            reader.close();
        }
        FileUtils.deleteDirectory(scriptsDir);
        scriptsDir = new File(System.getenv("WORKSPACE") + File.separator + "Tests" + File.separator + "scripts");
        FileUtils.copyDirectory(newScriptsDir, scriptsDir);
        final File[] list = scriptsDir.listFiles();
        for (int j = 0; j < list.length; ++j) {
            list[j].setExecutable(true);
            System.out.println("Setting " + list[j].getName() + " to as executable");
            System.out.println(FileUtils.readFileToString(list[j]));
        }
    }

    public static void instalDos2Unix() {
        try {
            final Process p = Runtime.getRuntime().exec("yum install dos2unix");
            final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            p.waitFor();
        } catch (Exception e) {
            System.out.println("Exception installing do2unix command");
        }
    }
}
