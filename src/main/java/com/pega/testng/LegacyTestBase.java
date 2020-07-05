

package com.pega.testng;

import com.pega.Configuration;
import com.pega.*;
import com.pega.exceptions.*;
import com.pega.framework.*;
import com.pega.testng.exceptions.*;
import com.pega.util.*;
import org.testng.*;
import org.testng.annotations.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class LegacyTestBase {


    private static TestEnvironment testEnv;
    private static Configuration configuration;
    private static Browser browser;
    protected static PegaWebDriver pegaDriver;

    static {
        LegacyTestBase.testEnv = null;
        LegacyTestBase.configuration = null;
        LegacyTestBase.browser = null;
        LegacyTestBase.pegaDriver = null;
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(final ITestContext context) throws Exception {
    }

    @AfterSuite
    public void afterSuite() {
    }

    @BeforeClass(alwaysRun = true)
    public void aaaSetUp() {
        this.setTestEnv(new TestEnvironmentImpl());
        setConfiguration(this.getTestEnv().getConfiguration());
        setBrowser(this.getTestEnv().getBrowser());
        getBrowser().open();
        setPegaDriver(this.getTestEnv().getPegaDriver());
    }

    @DataProvider(name = "TestData")
    public Iterator<Object[]> dataProvider(final Method m) {
        System.setProperty("data.dir", System.getProperty("user.dir") + System.getProperty("file.separator") + DataUtil.getDataFolder() + System.getProperty("file.separator") + "BuildWatcher");
        String fileName = this.getFileName(m);
        if (!fileName.toLowerCase().endsWith(".csv")) {
            fileName = fileName + ".csv";
        }
        final File dataFile = new File(System.getProperty("data.dir") + System.getProperty("file.separator") + fileName);
        BufferedReader br = null;
        String line = "";
        StringTokenizer tokens = null;
        final String delimiter = ",";
        final List<Object[]> data = new LinkedList<Object[]>();
        Object[] rowData = null;
        int counter = 0;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                tokens = new StringTokenizer(line, delimiter);
                rowData = new Object[tokens.countTokens()];
                counter = 0;
                while (tokens.hasMoreTokens()) {
                    rowData[counter++] = tokens.nextToken();
                }
                data.add(rowData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new PegaWebDriverException("Data file with name : \"" + dataFile.getName() + "\" is not found");
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new PegaWebDriverException("Error reading data from file: " + fileName);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return data.iterator();
    }

    @AfterClass(alwaysRun = true)
    public void zzzTearDown() {
        if (!this.getTestEnv().getConfiguration().getSUTConfig().isDebugMode()) {
            getBrowser().logout();
            getBrowser().close();
        }
    }

    @AfterClass(alwaysRun = true)
    public void zzzzCloseAllOpenBrowsers() {
        if (!this.getTestEnv().getConfiguration().getSUTConfig().isDebugMode()) {
            this.getTestEnv().terminate();
        }
    }

    protected String getFileName(final Method m) {
        final TestInfo testInfo = m.getAnnotation(TestInfo.class);
        if (testInfo == null) {
            throw new PegaTestNGException("@TestInfo annotation is not declared on Test method");
        }
        final Test test = m.getAnnotation(Test.class);
        if (!test.dataProvider().equals("") && testInfo.dataFileName().equals("")) {
            throw new PegaTestNGException("Element 'dataFileName' is not defined for @TestInfo on a Test Method that has 'dataProvider' element defined in @Test annotation");
        }
        final String fileName = testInfo.dataFileName();
        return fileName;
    }

    public TestEnvironment getTestEnv() {
        return LegacyTestBase.testEnv;
    }

    public void setTestEnv(final TestEnvironment testEnv) {
        LegacyTestBase.testEnv = testEnv;
    }

    public static Configuration getConfiguration() {
        return LegacyTestBase.configuration;
    }

    public static void setConfiguration(final Configuration configuration) {
        LegacyTestBase.configuration = configuration;
    }

    public static Browser getBrowser() {
        return LegacyTestBase.browser;
    }

    public static void setBrowser(final Browser browser) {
        LegacyTestBase.browser = browser;
    }

    public PegaWebDriver getPegaDriver() {
        return LegacyTestBase.pegaDriver;
    }

    public static void setPegaDriver(final PegaWebDriver pegaDriver) {
        LegacyTestBase.pegaDriver = pegaDriver;
    }
}
