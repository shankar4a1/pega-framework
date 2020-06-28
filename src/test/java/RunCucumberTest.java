import io.cucumber.testng.*;
import org.testng.annotations.*;


@CucumberOptions(

        /* features = {"src/test/resources/features"},

         monochrome = true,
         plugin = "message:target/cucumber-report.ndjson",
         tags = "@dev"*/
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {


    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }


}
