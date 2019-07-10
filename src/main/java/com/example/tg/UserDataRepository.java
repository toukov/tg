package com.example.tg;

import com.example.tg.models.User;

import java.util.Optional;

import javax.inject.Inject;



public class UserDataRepository {
    @Inject
    private DataBaseProvider dbProvider;

    /**
     * find user by username.
     * @param username username
     * @return user or empty optional
     */
    public Optional<User> findUser(String username) {
        return Optional.ofNullable(
                dbProvider.getDatastore()
                        .createQuery(User.class)
                        .field("username")
                        .equal(username).get());
    }

    /**
     * Saves the given user to database.
     * @param user user
     */
    public void saveUser(User user) {
        dbProvider.getDatastore().save(user);
    }
}
