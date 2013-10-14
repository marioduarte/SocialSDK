package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.fail;
import lib.MockEndpoint;

import org.junit.Test;

import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

public class CommunitiesMockTest extends BaseUnitTest {
	protected static final String	TEST_COMMUNITY_DESCRIPTION	= "Test Community Description 8";
	protected static final String	NEW_COMMUNITY				= "New Community 8";

	@Test
	public void testCreateCommunityTwice() {
		try {
			CommunityService svc = createCommunityService();
			Community c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			svc.createCommunity(c);
			svc.createCommunity(c);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling CommunityService.createCommunity() caused by: "+e.getMessage());
		}
	}

	private CommunityService createCommunityService() throws AuthenticationException {
		ConnectionsBasicEndpoint endpoint = new ConnectionsBasicEndpoint();
		endpoint.setUrl("https://qs.renovations.com:444");
		endpoint.setForceTrustSSLCertificate(true);
		endpoint.login("fadams", "passw0rd"); // TODO externalize these
		MockEndpoint mockEndpoint = new MockEndpoint(endpoint);
		CommunityService service = new CommunityService(mockEndpoint);
		return service;			
	}
}
