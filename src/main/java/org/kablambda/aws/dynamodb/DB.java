package org.kablambda.aws.dynamodb;

import java.util.Optional;

public interface DB {
    void write(String tenantUuid, String cloudId, String name, String value);

    Optional<String> read(String tenantUuid, String cloudId, String name);
}
