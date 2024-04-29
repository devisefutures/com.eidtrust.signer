package com.eidtrust.signer.rssp.csc;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.eidtrust.signer.rssp.csc.controller.CSCCredentialControllerTest;
import com.eidtrust.signer.rssp.csc.controller.CSCInfoControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CSCCredentialControllerTest.class, CSCInfoControllerTest.class
})
public class CSCSuite {
}
