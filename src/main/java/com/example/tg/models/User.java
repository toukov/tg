package com.example.tg.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PostLoad;

import java.util.HashSet;
import java.util.Set;

@Entity("users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String passwordSaltedHash;
    private String salt;

    // @Reference and List<Game> could be used here but it would require somewhat awkward nested
    // projections, so using list of ObjectId:s instead. Also saving back to database doesn't work
    // if the Game is fetched with projections but instead merge would have to be used.
    private Set<ObjectId> favouriteGames;

    public User() {
        // empty
    }

    @PostLoad
    public void postLoad() {
        if (favouriteGames == null) {
            favouriteGames = new HashSet<>();
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordSaltedHash() {
        return passwordSaltedHash;
    }

    public void setPasswordSaltedHash(String passwordSaltedHash) {
        this.passwordSaltedHash = passwordSaltedHash;
    }

    public Set<ObjectId> getFavouriteGames() {
        return favouriteGames;
    }

    public void setFavouriteGames(Set<ObjectId> favouriteGames) {
        this.favouriteGames = favouriteGames;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
