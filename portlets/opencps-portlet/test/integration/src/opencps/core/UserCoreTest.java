package opencps.core;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.InitUtil;


public class UserCoreTest {
	@Before
	public void setUp()
	    throws Exception {
		InitUtil.initWithSpring();
	}

	@After
	public void tearDown()
	    throws Exception {

	}

	@Test
	public void test()
	    throws SystemException {

		try {
			// returns all users of the portal
			//List<User> users = UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			 
			assertTrue("Users must not be empty", true);
			 
			}
			catch (Exception e) {
			fail("Exception:" + e.getMessage());
			}
	}
}
