/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy;

import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryAsyncReqErrorNoOpImplTest {

    public AdapterPatientDiscoveryAsyncReqErrorNoOpImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processPatientDiscoveryAsyncReqError method, of class AdapterPatientDiscoveryAsyncReqErrorNoOpImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReqError() {
        System.out.println("processPatientDiscoveryAsyncReqError");

        AsyncAdapterPatientDiscoveryErrorRequestType request = null;
        AdapterPatientDiscoveryAsyncReqErrorNoOpImpl instance = new AdapterPatientDiscoveryAsyncReqErrorNoOpImpl();
        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReqError(request);

        assertNotNull(result);
    }

}