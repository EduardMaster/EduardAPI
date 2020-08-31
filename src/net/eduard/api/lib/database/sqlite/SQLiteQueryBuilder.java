package net.eduard.api.lib.database.sqlite;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.database.api.SQLQueryBuilder;
import net.eduard.api.lib.database.mysql.MySQLOption;

public class SQLiteQueryBuilder implements SQLQueryBuilder {
    private final SQLiteOption option = new SQLiteOption();
    private final StringBuilder builder = new StringBuilder();

    @Override
    public SQLOption option() {
        return option;
    }


}
