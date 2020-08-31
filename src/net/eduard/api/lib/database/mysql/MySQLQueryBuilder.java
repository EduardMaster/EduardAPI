package net.eduard.api.lib.database.mysql;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.database.api.SQLQueryBuilder;

public class MySQLQueryBuilder implements SQLQueryBuilder {
    private final MySQLOption option= new MySQLOption();
    private final StringBuilder builder = new StringBuilder();

    @Override
    public SQLOption option() {
        return option;
    }

}
