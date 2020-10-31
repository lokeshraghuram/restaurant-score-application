import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true,
        plugin = {"pretty", "html:target/cucumber","junit:target/cucumber-reports/cucumber.xml"},
        features = "src/test/resources/features",
        extraGlue = "com.nhs.inspection.bdd.component")
public class CucumberTestRunner {
}