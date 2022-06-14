package olegpash.lab7.server.interfaces;

import olegpash.lab7.common.exceptions.DatabaseException;

import java.sql.Connection;

public interface DBConnectable {
    void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException;

    <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException;
}
