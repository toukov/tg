package com.example.tg;

import com.example.tg.models.Game;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

public class GameDataRepository {
    public static final String MARKET = "market";
    public static final String RANK = "rank";
    public static final String APP_ID = "appId";
    public static final String ID = "id";

    @Inject
    private DataBaseProvider dbProvider;

    public enum TopAmount {
        TOP10("top10Entry"),
        TOP20("top20Entry"),
        TOP50("top50Entry");

        private String fieldName;

        TopAmount(String fieldName) {
            this.fieldName = fieldName;
        }

        String getFieldName() {
            return fieldName;
        }
    }

    public enum HistoryFrequency {
        DAILY("history"),
        WEEKLY("historyWeek"),
        MONTHLY("historyMonth");

        private String fieldName;

        HistoryFrequency(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    public GameDataRepository() {
        // empty
    }

    /**
     * Get top games.
     * @param count how many games
     * @param market market
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @return found games
     */
    public List<Game> topGames(int count, String market, Collection<String> filters,
            boolean retain) {
        return topGames(market, filters, retain, new FindOptions().limit(count));
    }

    /**
     * Get top games.
     * @param count how many games
     * @param market market
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @param page page number
     * @param pageSize size of page
     * @return found games
     */
    public List<Game> topGames(int count, String market, Collection<String> filters,
            boolean retain, int page, int pageSize) {
        return topGames(market, filters, retain, new FindOptions()
                .limit(Utils.getLimitWithCount(page, pageSize, count))
                .skip(Utils.getSkip(page, pageSize)));
    }

    private List<Game> topGames(String market, Collection<String> filters,
            boolean retain, FindOptions options) {
        final Query<Game> query = dbProvider.getDatastore().createQuery(Game.class);

        filters.forEach(filter -> query.project(filter, retain));

        return query.field(MARKET).equal(market).order(RANK).asList(options);
    }

    /**
     * Get recently top10, top20 or top50 reached games.
     * @param markets filter to these markets
     * @param daysAgo since this many days ago
     * @param topAmount top10, top20 or top50
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @return found games
     */
    public List<Game> recentTop(Collection<String> markets, int daysAgo, TopAmount topAmount,
            Collection<String> filters, boolean retain) {
        return recentTop(markets, daysAgo, topAmount, filters, retain, new FindOptions());
    }

    /**
     * Get recently top10, top20 or top50 reached games.
     * @param markets filter to these markets
     * @param daysAgo since this many days ago
     * @param topAmount top10, top20 or top50
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @param page page number
     * @param pageSize size of page
     * @return found games
     */
    public List<Game> recentTop(Collection<String> markets, int daysAgo, TopAmount topAmount,
            Collection<String> filters, boolean retain, int page, int pageSize) {
        return recentTop(markets, daysAgo, topAmount, filters, retain, new FindOptions()
                .limit(pageSize)
                .skip(Utils.getSkip(page, pageSize)));
    }

    private List<Game> recentTop(Collection<String> markets, int daysAgo, TopAmount topAmount,
            Collection<String> filters, boolean retain, FindOptions options) {
        new FindOptions();
        final Query<Game> query = dbProvider.getDatastore().createQuery(Game.class);

        // we want to fetch the "market" field no matter what the filters are since that is used for
        // grouping the data.
        Set<String> filtersSet = new HashSet<>(filters);
        if (retain) {
            filtersSet.add(MARKET);
        } else {
            filters.remove(MARKET);
        }

        filtersSet.forEach(filter -> query.project(filter, retain));

        if (!markets.isEmpty()) {
            query.field(MARKET).in(markets);
        }

        return query.field(topAmount.getFieldName()).exists()
                .field(topAmount.getFieldName())
                .greaterThan(Utils.getUnixTimestampMillisDaysAgo(daysAgo))
                .order(MARKET + "," + topAmount.getFieldName()).asList(options);
    }

    /**
     * Get rank history of a game.
     * @param appId app id of game
     * @param market market of game
     * @param freq daily, weekly or monthly rank history
     * @return found history
     */
    public Optional<Game> rankHistory(int appId, String market, HistoryFrequency freq) {
        return Optional.ofNullable(dbProvider.getDatastore().createQuery(Game.class)
                .project(freq.getFieldName(), true)
                .field(MARKET).equal(market)
                .field(APP_ID).equal(appId).get());
    }

    /**
     * Get a single game.
     * @param market market of game
     * @param appId app id of game
     * @return found game or empty optional
     */
    public Optional<Game> getGame(String market, int appId) {
        return Optional.ofNullable(dbProvider.getDatastore().createQuery(Game.class)
                .project(ID, true)
                .field(MARKET).equal(market)
                .field(APP_ID).equal(appId).get());
    }

    /**
     * Get all the games whose id is in the provided id list.
     * @param ids ids of the games
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @return found games
     */
    public List<Game> getGamesByIds(Collection<ObjectId> ids, Collection<String> filters,
            boolean retain) {
        return getGamesByIds(ids, filters, retain, new FindOptions());
    }

    /**
     * Get all the games whose id is in the provided id list.
     * @param ids ids of the games
     * @param filters which fields of game
     * @param retain do the filters keep fields or reject
     * @param page page number
     * @param pageSize size of page
     * @return found games
     */
    public List<Game> getGamesByIds(Collection<ObjectId> ids, Collection<String> filters,
            boolean retain, int page, int pageSize) {
        return getGamesByIds(ids, filters, retain, new FindOptions()
                .limit(pageSize)
                .skip(Utils.getSkip(page, pageSize)));
    }

    private List<Game> getGamesByIds(Collection<ObjectId> ids, Collection<String> filters,
            boolean retain, FindOptions options) {
        final Query<Game> query = dbProvider.getDatastore().createQuery(Game.class);
        query.field(ID).in(ids);
        filters.forEach(filter -> query.project(filter, retain));

        return query.order(ID).asList(options);
    }
}
