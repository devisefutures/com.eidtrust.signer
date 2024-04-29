package com.eidtrust.signer.rssp;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.eidtrust.signer.rssp.api.ApiSuite;
import com.eidtrust.signer.rssp.csc.CSCSuite;

@RunWith(Categories.class)
@Suite.SuiteClasses({ApiSuite.class, CSCSuite.class })
public class AllTests {
}
