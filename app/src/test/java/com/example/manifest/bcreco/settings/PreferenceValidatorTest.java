package com.example.manifest.bcreco.settings;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreferenceValidatorTest {

    @Test
    public void isIPV4inputNull() {
        assertFalse("IP null should be invalid", PreferenceValidator.isIPV4(null));
    }

    @Test
    public void isIPV4emptyString() {
        assertFalse("IP empty String should be invalid", PreferenceValidator.isIPV4(""));
    }

    @Test
    public void isIPV4NegativeNumber() {
        assertFalse("IP -127.0.0.1 should be invalid", PreferenceValidator.isIPV4("-127.0.0.1"));
        assertFalse("IP 127.-10.0.1 should be invalid", PreferenceValidator.isIPV4("127.-10.0.1"));
        assertFalse("IP 127.0.-120.1 should be invalid", PreferenceValidator.isIPV4("127.0.-120.1"));
        assertFalse("IP 127.0.0.-251 should be invalid", PreferenceValidator.isIPV4("127.0.0.-251"));
    }

    @Test
    public void isIPV4alphabetic() {
        assertFalse("IP \"abc\" should be invalid", PreferenceValidator.isIPV4("abc"));
    }

    @Test
    public void isIPV4lessThenFourNumbers() {
        assertFalse("IP 112 should be invalid", PreferenceValidator.isIPV4("112"));
        assertFalse("IP 112.101 should be invalid", PreferenceValidator.isIPV4("112.101"));
        assertFalse("IP 112.101.102 should be invalid", PreferenceValidator.isIPV4("112.101.102"));
    }

    @Test
    public void isIPV4oneNumberIsMoreThen255() {
        assertFalse("IP 256.112.101.102 should be invalid", PreferenceValidator.isIPV4("256.112.101.102"));
        assertFalse("IP 112.303.101.102 should be invalid", PreferenceValidator.isIPV4("112.303.101.102"));
        assertFalse("IP 112.101.999.102 should be invalid", PreferenceValidator.isIPV4("112.101.999.102"));
        assertFalse("IP 112.101.102.256 should be invalid", PreferenceValidator.isIPV4("112.101.102.256"));
    }

    @Test
    public void isIPV4correct() {
        assertTrue("IP 127.0.0.1 should be valid", PreferenceValidator.isIPV4("127.0.0.1"));
        assertTrue("IP 255.255.255.255 should be valid", PreferenceValidator.isIPV4("255.255.255.255"));
    }

    @Test
    public void isPortValidNotNumber() {
        assertFalse(PreferenceValidator.isPortValid("zero"));
    }

    @Test
    public void isPortValidEmpty() {
        assertFalse(PreferenceValidator.isPortValid(""));
    }

    @Test
    public void isPortValidZero() {
        assertFalse(PreferenceValidator.isPortValid("0"));
    }

    @Test
    public void isPortValidNegative() {
        assertFalse(PreferenceValidator.isPortValid("-100"));
    }

    @Test
    public void isPortValidTooBig() {
        assertFalse(PreferenceValidator.isPortValid("65536"));
    }

    @Test
    public void isPortValidValidNumber() {
        assertTrue(PreferenceValidator.isPortValid("8080"));
    }

    @Test
    public void isStoreIdValidNotNumber() {
        assertFalse(PreferenceValidator.isStoreIdValid("zero"));
    }

    @Test
    public void isStoreIdValidEmpty() {
        assertFalse(PreferenceValidator.isStoreIdValid(""));
    }

    @Test
    public void isStoreIdValidZero() {
        assertTrue(PreferenceValidator.isStoreIdValid("0"));
    }

    @Test
    public void isStoreIdValidNegative() {
        assertFalse(PreferenceValidator.isStoreIdValid("-100"));
    }

    @Test
    public void isStoreIdValidTooBig() {
        assertFalse(PreferenceValidator.isStoreIdValid("100000"));
    }

    @Test
    public void isStoreIdValidValidNumber() {
        assertTrue(PreferenceValidator.isStoreIdValid("119"));
    }
}