package eu.assina.rssp.csc;
import eu.assina.rssp.csc.controller.CSCCredentialControllerTest;
import eu.assina.rssp.csc.controller.CSCInfoControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CSCCredentialControllerTest.class, CSCInfoControllerTest.class
})
public class CSCSuite {
}
