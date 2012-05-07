/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import gov.hhs.fha.nhinc.callback.SamlConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.AuthzDecisionStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.DecisionTypeEnumeration;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.signature.Exponent;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.signature.Modulus;
import org.opensaml.xml.signature.RSAKeyValue;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAML2ComponentBuilder implements SAMLCompontentBuilder {

	private SAMLObjectBuilder<AuthnStatement> authnStatementBuilder;

	private SAMLObjectBuilder<AuthnContext> authnContextBuilder;

	private SAMLObjectBuilder<AuthnContextClassRef> authnContextClassRefBuilder;

	private SAMLObjectBuilder<AttributeStatement> attributeStatementBuilder;

	private SAMLObjectBuilder<Attribute> attributeBuilder;
	private static final String X509_NAME_ID = "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName";
	private static final String DEFAULT_ISSUER_VALUE = "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US";

	private SAMLObjectBuilder<Assertion> assertionBuilder;

	private SAMLObjectBuilder<NameID> nameIdBuilder;

	private SAMLObjectBuilder<Conditions> conditionsBuilder;

	private SAMLObjectBuilder<Action> actionElementBuilder;

	private SAMLObjectBuilder<AuthzDecisionStatement> authorizationDecisionStatementBuilder;

	private SAMLObjectBuilder<AudienceRestriction> audienceRestrictionBuilder;

	private SAMLObjectBuilder<Audience> audienceBuilder;

	private XSStringBuilder stringBuilder;

	private SAMLObjectBuilder<Evidence> evidenceBuilder;

	private XSAnyBuilder xsAnyBuilder;

	private static SAMLObjectBuilder<SubjectLocality> subjectLocalityBuilder;

	private static XMLObjectBuilderFactory builderFactory = Configuration
			.getBuilderFactory();

	private OpenSAML2ComponentBuilder() {

		builderFactory = Configuration.getBuilderFactory();

		authnStatementBuilder = (SAMLObjectBuilder<AuthnStatement>) builderFactory
				.getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
		authnContextBuilder = (SAMLObjectBuilder<AuthnContext>) builderFactory
				.getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
		authnContextClassRefBuilder = (SAMLObjectBuilder<AuthnContextClassRef>) builderFactory
				.getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
		subjectLocalityBuilder = (SAMLObjectBuilder<SubjectLocality>) builderFactory
				.getBuilder(SubjectLocality.DEFAULT_ELEMENT_NAME);

		authorizationDecisionStatementBuilder = (SAMLObjectBuilder<AuthzDecisionStatement>) builderFactory
				.getBuilder(AuthzDecisionStatement.DEFAULT_ELEMENT_NAME);

		actionElementBuilder = (SAMLObjectBuilder<Action>) builderFactory
				.getBuilder(Action.DEFAULT_ELEMENT_NAME);

		nameIdBuilder = (SAMLObjectBuilder<NameID>) builderFactory
				.getBuilder(NameID.DEFAULT_ELEMENT_NAME);

		assertionBuilder = (SAMLObjectBuilder<Assertion>) builderFactory
				.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

		conditionsBuilder = (SAMLObjectBuilder<Conditions>) builderFactory
				.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);

		audienceRestrictionBuilder = (SAMLObjectBuilder<AudienceRestriction>) builderFactory
				.getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);

		audienceBuilder = (SAMLObjectBuilder<Audience>) builderFactory
				.getBuilder(Audience.DEFAULT_ELEMENT_NAME);

		evidenceBuilder = (SAMLObjectBuilder<Evidence>) builderFactory
				.getBuilder(Evidence.DEFAULT_ELEMENT_NAME);

		stringBuilder = (XSStringBuilder) builderFactory
				.getBuilder(XSString.TYPE_NAME);

		xsAnyBuilder = (XSAnyBuilder) builderFactory
				.getBuilder(XSAny.TYPE_NAME);

	}

	private static OpenSAML2ComponentBuilder INSTANCE = new OpenSAML2ComponentBuilder();

	public static OpenSAML2ComponentBuilder getInstance() {
		return INSTANCE;
	}

	private XMLObject createOpenSAMLObject(QName qname) {
		return builderFactory.getBuilder(qname).buildObject(qname);
	}

	/**
	 * @return
	 */
	public AuthnStatement createAuthenicationStatements(String cntxCls,
			String sessionIndex, DateTime authInstant, String inetAddr,
			String dnsName) {

		AuthnStatement authnStatement = authnStatementBuilder.buildObject();

		AuthnContextClassRef authnContextClassRef = authnContextClassRefBuilder
				.buildObject();

		authnContextClassRef.setAuthnContextClassRef(cntxCls);

		AuthnContext authnContext = authnContextBuilder.buildObject();
		authnContext.setAuthnContextClassRef(authnContextClassRef);
		authnStatement.setAuthnContext(authnContext);

		authnStatement.setSessionIndex(sessionIndex);

		authnStatement.setAuthnInstant(authInstant);

		if (inetAddr != null && dnsName != null) {

			SubjectLocality subjectLocality = subjectLocalityBuilder
					.buildObject();
			subjectLocality.setDNSName(dnsName);
			subjectLocality.setAddress(inetAddr);

			authnStatement.setSubjectLocality(subjectLocality);
		}

		return authnStatement;
	}

	public AuthzDecisionStatement createAuthzDecisionStatement(String resource,
			String decisionTxt, String action, Evidence evidence) {
		AuthzDecisionStatement authDecision = authorizationDecisionStatementBuilder
				.buildObject();
		authDecision.setResource(resource);

		DecisionTypeEnumeration decision = DecisionTypeEnumeration.DENY;

		authDecision.setDecision(decision);

		Action actionElement = actionElementBuilder.buildObject();
		actionElement
				.setNamespace("urn:oasis:names:tc:SAML:1.0:action:rwedc-negation");
		actionElement.setAction(action);

		authDecision.getActions().add(actionElement);
		authDecision.setEvidence(evidence);

		return authDecision;

	}

	public Assertion createAssertion(final String uuid) {
		Assertion assertion = assertionBuilder.buildObject(
				Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
		assertion.setID("_" + uuid);
		assertion.setVersion(SAMLVersion.VERSION_20);
		assertion.setIssueInstant(new DateTime());
		return assertion;
	}

	@SuppressWarnings("unchecked")
	public NameID createNameID(String qualifier, String format, String value) {
		NameID nameID = nameIdBuilder.buildObject();
		nameID.setNameQualifier(qualifier);
		nameID.setFormat(format);
		nameID.setValue(value);
		return nameID;
	}

	/**
	 * @return
	 */
	private NameID createNameID(String format,
			String value) {
		NameID nameId = (NameID) createOpenSAMLObject(NameID.DEFAULT_ELEMENT_NAME);

		nameId.setFormat(format);
		nameId.setValue(value);

		return nameId;
	}

	/**
	 * @param format
	 * @param sIssuer
	 * @return
	 */
	public Issuer createIssuer(String format, String sIssuer) {
		Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setFormat(format);
		issuer.setValue(sIssuer);
		return issuer;
	}

	protected Issuer createDefaultIssuer() {
		Issuer issuer = (Issuer) createOpenSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setFormat(X509_NAME_ID);
		issuer.setValue(DEFAULT_ISSUER_VALUE);
		return issuer;
	}

	/**
	 * @return
	 */
	public Subject createSubject(String x509Name) {
		Subject subject = (org.opensaml.saml2.core.Subject) createOpenSAMLObject(Subject.DEFAULT_ELEMENT_NAME);
		subject.setNameID(createNameID(X509_NAME_ID, x509Name));
		subject.getSubjectConfirmations().add(createHoKConfirmation());
		return subject;
	}

	private SubjectConfirmation createHoKConfirmation() {
		SubjectConfirmation subjectConfirmation = (SubjectConfirmation) createOpenSAMLObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		subjectConfirmation
				.setMethod(org.opensaml.saml2.core.SubjectConfirmation.METHOD_HOLDER_OF_KEY);
		subjectConfirmation
				.setSubjectConfirmationData(createSubjectConfirmationData());

		return subjectConfirmation;
	}

	private SubjectConfirmationData createSubjectConfirmationData() {
		SubjectConfirmationData subjectConfirmationData = (SubjectConfirmationData) createOpenSAMLObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
		subjectConfirmationData.getUnknownAttributes().put(
				new QName("http://www.w3.org/2001/XMLSchema-instance", "type",
						"xsi"), "saml:KeyInfoConfirmationDataType");

		KeyInfo ki = (KeyInfo) createOpenSAMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);
		KeyValue kv = (KeyValue) createOpenSAMLObject(KeyValue.DEFAULT_ELEMENT_NAME);

		RSAKeyValue _RSAKeyValue = (RSAKeyValue) createOpenSAMLObject(RSAKeyValue.DEFAULT_ELEMENT_NAME);
		Exponent exp = (Exponent) createOpenSAMLObject(Exponent.DEFAULT_ELEMENT_NAME);
		Modulus mod = (Modulus) createOpenSAMLObject(Modulus.DEFAULT_ELEMENT_NAME);

		RSAPublicKey RSAPk = (RSAPublicKey) getPublicKey();

		exp.setValue(RSAPk.getPublicExponent().toString());
		_RSAKeyValue.setExponent(exp);
		mod.setValue(RSAPk.getModulus().toString());
		_RSAKeyValue.setModulus(mod);

		kv.setRSAKeyValue(_RSAKeyValue);
		ki.getKeyValues().add(kv);

		subjectConfirmationData.getUnknownXMLObjects().add(ki);

		return subjectConfirmationData;
	}

	public PublicKey getPublicKey() {
		KeyStore ks;
		KeyStore.PrivateKeyEntry pkEntry = null;
		try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			char[] password = "cspass".toCharArray();
			FileInputStream fis = new FileInputStream(
					"c:/opt/keystores/clientKeystore.jks");
			ks.load(fis, password);
			fis.close();
			pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("myclientkey",
					new KeyStore.PasswordProtection("ckpass".toCharArray()));
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		X509Certificate certificate = (X509Certificate) pkEntry
				.getCertificate();
		return certificate.getPublicKey();
	}

	/**
	 * @return
	 */
	public Assertion createAssertion() {
		Assertion assertion = assertionBuilder.buildObject(
				Assertion.DEFAULT_ELEMENT_NAME, Assertion.TYPE_NAME);
		return assertion;
	}

	public Conditions createConditions(DateTime notBefore, DateTime notAfter,
			String audienceURI) {
		Conditions conditions = conditionsBuilder.buildObject();

		conditions.setNotBefore(notBefore);
		conditions.setNotOnOrAfter(notAfter);

		AudienceRestriction audienceRestriction = audienceRestrictionBuilder
				.buildObject();
		Audience audience = audienceBuilder.buildObject();
		audience.setAudienceURI(audienceURI);
		audienceRestriction.getAudiences().add(audience);
		conditions.getAudienceRestrictions().add(audienceRestriction);

		return conditions;
	}

	public Attribute createAttribute(String friendlyName, String name,
			String nameFormat) {

		Attribute attribute = attributeBuilder.buildObject();
		attribute.setFriendlyName(friendlyName);
		if (nameFormat == null) {
			attribute
					.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:uri");
		} else {
			attribute.setNameFormat(nameFormat);
		}
		attribute.setName(name);
		return attribute;
	}

	public Attribute createAttribute(String friendlyName, String name,
			String nameFormat, List<?> values) {

		Attribute attribute = createAttribute(friendlyName, name, nameFormat);

		for (Object value : values) {
			if (value instanceof String) {
				XSString attributeValue = stringBuilder
						.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
								XSString.TYPE_NAME);
				attributeValue.setValue((String) value);
				attribute.getAttributeValues().add(attributeValue);
			} else if (value instanceof XMLObject) {
				attribute.getAttributeValues().add((XMLObject) value);
			}
		}

		return attribute;
	}

	public XSAny createAttributeValue(final String namespace,
			final String name, final String prefix,
			Map<String, String> attributes) {

		XSAny attributeValue = xsAnyBuilder
				.buildObject(namespace, name, prefix);
		for (String atrName : attributes.keySet()) {
			attributeValue.getUnknownAttributes().put(new QName(atrName),
					attributes.get(atrName));

		}
		return attributeValue;
	}

	public List<AttributeStatement> createAttributeStatement(
			List<Attribute> attributes) {
		List<AttributeStatement> attributeStatements = new ArrayList<AttributeStatement>();
		if (attributes != null && attributes.size() > 0) {

			AttributeStatement attributeStatement = attributeStatementBuilder
					.buildObject();
			for (Attribute attribute : attributes) {
				attributeStatement.getAttributes().add(attribute);

			}
			// Add the completed attribute statementBean to the collection
			attributeStatements.add(attributeStatement);
		}

		return attributeStatements;
	}

	public Evidence createEvidence(List<Assertion> assertions) {
		Evidence evidence = evidenceBuilder.buildObject();
		evidence.getAssertions().addAll(assertions);
		return evidence;
	}

	public List<AttributeStatement> createEvidenceStatements(
			List accessConstentValues,
			List evidenceInstanceAccessConsentValues, final String namespace) {
		List<AttributeStatement> statements = new ArrayList<AttributeStatement>();

		List<Attribute> attributes = new ArrayList<Attribute>();

		if (accessConstentValues != null) {
			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute("AccessConsentPolicy", namespace, null,
							accessConstentValues));
		}

		if (evidenceInstanceAccessConsentValues != null) {
			attributes.add(OpenSAML2ComponentBuilder.getInstance()
					.createAttribute("InstanceAccessConsentPolicy", namespace,
							null, evidenceInstanceAccessConsentValues));
		}
		if (!attributes.isEmpty()) {
			statements = OpenSAML2ComponentBuilder.getInstance()
					.createAttributeStatement(attributes);
		}

		return statements;
	}

}