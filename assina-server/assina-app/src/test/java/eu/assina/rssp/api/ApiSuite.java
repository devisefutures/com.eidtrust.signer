package eu.assina.rssp.api;
import eu.assina.rssp.api.controller.CredentialControllerTest;
import eu.assina.rssp.api.service.CredentialServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CredentialControllerTest.class, CredentialServiceTest.class
})
public class ApiSuite {
}
