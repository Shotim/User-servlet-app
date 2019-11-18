package com.leverx.utils;

import com.leverx.user.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.leverx.constants.UserFields.ID;
import static com.leverx.constants.UserFields.NAME;

public class RepositoryUtils {

    public static List<User> extractUsersFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            users.add(
                    new User(resultSet.getInt(ID),
                            resultSet.getString(NAME)));
        }
        return users;
    }

    public static User extractFirstUserFromResultSet(ResultSet resultSet) throws SQLException {
        return extractUsersFromResultSet(resultSet).stream()
                .findFirst()
                .orElseThrow();
    }

}
