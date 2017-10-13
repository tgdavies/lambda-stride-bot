package org.kablambda.apis.media;

import java.util.List;
import java.util.Map;

public class AuthenticationClaims {
    private String iss;
    private Map<String,List<AccessMethod>> access;
    private String nbf;
    private String exp;

    public AuthenticationClaims(String iss,
                                Map<String, List<AccessMethod>> access, String nbf, String exp) {
        this.iss = iss;
        this.access = access;
        this.nbf = nbf;
        this.exp = exp;
    }
}
