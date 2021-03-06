package org.ovirt.engine.core.searchbackend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SyntaxCheckerFactoryTest {
    @Test
    public void createUISyntaxChecker() {
        ISyntaxChecker checker = SyntaxCheckerFactory.createUISyntaxChecker("foo");
        assertNotNull(checker);
        assertEquals(checker, SyntaxCheckerFactory.createUISyntaxChecker("foo"));
        assertEquals("foo", SyntaxCheckerFactory.getConfigAuthenticationMethod());
    }

    @Test
    public void createBackendSyntaxChecker() {
        ISyntaxChecker checker = SyntaxCheckerFactory.createBackendSyntaxChecker("foo");
        assertNotNull(checker);
        assertEquals(checker, SyntaxCheckerFactory.createBackendSyntaxChecker("foo"));
        assertEquals("foo", SyntaxCheckerFactory.getConfigAuthenticationMethod());
    }

    @Test
    public void createADSyntaxChecker() {
        ISyntaxChecker checker = SyntaxCheckerFactory.createADSyntaxChecker("foo");
        assertNotNull(checker);
        assertEquals(checker, SyntaxCheckerFactory.createADSyntaxChecker("foo"));
        assertEquals("foo", SyntaxCheckerFactory.getConfigAuthenticationMethod());
    }
}
