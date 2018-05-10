package org.kablambda.apis;

import org.kablambda.aws.handler.RegisterHandler; /**
 * Lists the platform APIs
 */
public enum API {
    STRIDE {
        @Override
        public String getCredentials(RegisterHandler.Register tenant) {
            return tenant.getStrideCredentials();
        }
    }, MEDIA {
        @Override
        public String getCredentials(RegisterHandler.Register tenant) {
            return null;
        }
    }, USER {
        @Override
        public String getCredentials(RegisterHandler.Register tenant) {
            return null;
        }
    };

    public abstract String getCredentials(RegisterHandler.Register tenant);
}
