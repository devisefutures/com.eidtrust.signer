package com.eidtrust.signer.rssp.api;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.eidtrust.signer.rssp.api.controller.CredentialControllerTest;
import com.eidtrust.signer.rssp.api.service.CredentialServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CredentialControllerTest.class, CredentialServiceTest.class
})
public class ApiSuite {
}
