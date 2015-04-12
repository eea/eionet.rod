package eionet.rod;

import eionet.rpcserver.UITServiceRoster;
import eionet.rpcserver.UITServiceIF;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;


/**
 * Integration test to verify that the eionet.rpcserver module works correctly.
 * As long as ROD doesn't configure where to load the rpcserver module's configuration
 * this test shows that the fallback to properties file works.
 */
public class RpcServerModuleTest {

    @Test
    public void servicesMustExist() throws Exception {
        HashMap services = UITServiceRoster.getServices();
        assertNotNull("No services", services);
    }

    @Test
    public void areRODServicesLoaded() throws Exception {
        UITServiceIF service = UITServiceRoster.getService("WebRODService");
        assertNotNull("No services", service);
    }

    /**
     * Test that RPC services for ACL is turned on. (The acl.admin flag in
     * the rpc.properties file.
     */
    @Test
    public void areACLServicesLoaded() throws Exception {
        UITServiceIF service = UITServiceRoster.getService("XService");
        assertNotNull("No services", service);
    }

}

