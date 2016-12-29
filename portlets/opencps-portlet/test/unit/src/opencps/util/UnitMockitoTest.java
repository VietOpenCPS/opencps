
package opencps.util;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserLocalServiceUtil.class)
public class UnitMockitoTest {

	@Before
	public void setUp()
	    throws Exception {

	}

	@After
	public void tearDown()
	    throws Exception {

	}

	@Test
	public void test()
	    throws SystemException {

		PowerMockito.mockStatic(UserLocalServiceUtil.class);
		when(UserLocalServiceUtil.getUsersCount()).thenReturn(5);
		assertEquals(5, UserLocalServiceUtil.getUsersCount());
	}

}
