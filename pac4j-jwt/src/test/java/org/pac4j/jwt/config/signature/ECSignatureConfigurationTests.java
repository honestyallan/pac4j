package org.pac4j.jwt.config.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsHelper;
import org.pac4j.jwt.config.AbstractKeyEncryptionConfigurationTests;
import org.pac4j.jwt.util.JWKHelper;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ECSignatureConfiguration}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class ECSignatureConfigurationTests extends AbstractKeyEncryptionConfigurationTests {

    @Override
    protected String getAlgorithm() {
        return "EC";
    }

    private JWTClaimsSet buildClaims() {
        return new JWTClaimsSet.Builder().subject(VALUE).build();
    }

    @Test
    public void testMissingPrivateKey() {
        final var config = new ECSignatureConfiguration();
        TestsHelper.expectException(() -> config.sign(buildClaims()), TechnicalException.class, "privateKey cannot be null");
    }

    @Test
    public void testMissingPublicKey() {
        final var config = new ECSignatureConfiguration();
        config.setPrivateKey((ECPrivateKey) buildKeyPair().getPrivate());
        final var signedJWT = config.sign(buildClaims());
        TestsHelper.expectException(() -> config.verify(signedJWT), TechnicalException.class, "publicKey cannot be null");
    }

    @Test
    public void testMissingAlgorithm() {
        final var config = new ECSignatureConfiguration(buildKeyPair(), null);
        TestsHelper.expectException(config::init, TechnicalException.class, "algorithm cannot be null");
    }

    @Test
    public void testBadAlgorithm() {
        final var config = new ECSignatureConfiguration(buildKeyPair(), JWSAlgorithm.HS256);
        TestsHelper.expectException(config::init, TechnicalException.class,
            "Only the ES256, ES384 and ES512 algorithms are supported for elliptic curve signature");
    }

    @Test
    public void buildFromJwk() {
        final var json = new ECKey.Builder(Curve.P_256, (ECPublicKey) buildKeyPair().getPublic()).build().toJSONString();
        JWKHelper.buildECKeyPairFromJwk(json);
    }

    @Test
    public void testSignVerify() throws JOSEException {
        final var config = new ECSignatureConfiguration(buildKeyPair());
        final var claims = new JWTClaimsSet.Builder().subject(VALUE).build();
        final var signedJwt = config.sign(claims);
        assertTrue(config.verify(signedJwt));
    }
}
