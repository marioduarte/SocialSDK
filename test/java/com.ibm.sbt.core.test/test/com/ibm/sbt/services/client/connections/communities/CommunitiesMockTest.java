package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;

public class CommunitiesMockTest extends BaseUnitTest {

	protected static final String TEST_COMMUNITY_DESCRIPTION = "Test Community Description";
	protected static final String NEW_COMMUNITY = "New Community "+ System.currentTimeMillis();

	@Test
	public void testCreateCommunityTwice() {
		String uuid1 = null;
		String uuid2 = null;
		CommunityService svc = null;
		try {
			svc = new CommunityService();
			Community c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid1 = svc.createCommunity(c);
			c = svc.getCommunity(uuid1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			Community c = new Community(null);
			c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid2 = svc.createCommunity(c);
		} catch (CommunityServiceException e) {
			if (e.getCause() instanceof ClientServicesException
					&& (((ClientServicesException) e.getCause())
							.getResponseStatusCode() == 409)) {
				// duplicate entity exception
				return;
			}
			// in all other cases log the exception and fail
			e.printStackTrace();
			fail(e.getMessage());

		} finally {
			if (svc != null) {
				if (uuid1 != null) {
					try {
						svc.deleteCommunity(uuid1);
					} catch (Exception c) {
					}
				}
				if (uuid2 != null) {
					try {
						svc.deleteCommunity(uuid2);
					} catch (Exception c) {
					}
				}
			}

		}
		fail("Duplicated creation did not fail");
	}
}
