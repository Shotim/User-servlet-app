package com.company.driver;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class MySQLDriverTest {

    @Test
    void getConnection() {
        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.establishConnection();
        assertNotEquals(connection,null);
    }
}