package zc.study.spring.multipleplateformconfiguration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	Test_DEV_Properties.class, 
	Test_TEST_properties.class, 
	Test_QUALIF_Properties.class, 
	Test_PROD_Properties.class })
public class AllTests {

}

/* EOF*/
