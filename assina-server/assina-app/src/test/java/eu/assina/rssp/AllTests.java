package eu.assina.rssp;

import eu.assina.rssp.api.ApiSuite;
import eu.assina.rssp.csc.CSCSuite;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Suite.SuiteClasses({ApiSuite.class, CSCSuite.class })
public class AllTests {
}
