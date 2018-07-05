/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.patientlocationquery.inbound;

import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import java.util.Properties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;


public class PassthroughInboundPatientLocationQueryTest {

    PassthroughInboundPatientLocationQuery plqInbound;
    PatientLocationQueryRequestType request;
    AssertionType assertion;
    Properties properties;

    @BeforeClass
    public static void setNHINCPropertyDirectory()
    {
        // We need to set this property so the PropertyAccessor class doesnt complain and error out.
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources/");
    }
    @Before
    public void setup() {

        plqInbound =  Mockito.spy(PassthroughInboundPatientLocationQuery.class);
        request = new PatientLocationQueryRequestType();
        assertion = new AssertionType();
        properties = new Properties();
    }



    @Test
    public void testProcessPatientLocationQuery() {
        //Future story: Check if Audit Request was sent.
        PatientLocationQueryResponseType result = plqInbound.processPatientLocationQuery(request, assertion, properties);
        Mockito.verify(plqInbound).sendToAdapter(request, assertion);
        assertNotNull(result);

    }

    @Test
    public void testSendToAdapter() {
        PatientLocationQueryResponseType result = plqInbound.sendToAdapter(request, assertion);
        assertNotNull(result);
    }

}
