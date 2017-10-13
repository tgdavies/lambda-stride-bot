package org.kablambda.aws.dynamodb;

import java.util.Optional;

public interface DB {
    void createTable();

    void write(String cloudId, String name, String value);

    Optional<String> read(String cloudId, String name);
}
