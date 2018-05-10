package org.kablambda.aws.handler;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PathHttpHandlerTest {

    @Test
    public void testNormalizePath() {
        PathHttpHandler h = new PathHttpHandler((x) -> false, null);
        assertEquals("xyz", h.normalizePath("/api/xyz"));
        assertEquals("xyz", h.normalizePath("/api/cb7e84e8-e726-4245-b1a6-0f8edb4879c9/xyz"));
    }
}