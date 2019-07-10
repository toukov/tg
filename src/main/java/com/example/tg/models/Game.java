package com.example.tg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity("games")
public class Game {
    @Id
    private ObjectId id;
    private Long appId; // Not unique. Several different markets can have the same game with the same appId.
    private String name;
    private String market;
    private String artist;

    // Ignore the Instant-type and use milliseconds since epoch as Long instead. All the other
    // timestamps in the data are represented that way. see: getReleasedAsLong
    @JsonIgnore
    private Instant released;

    private Integer rankMonth;
    private Integer rankWeek;
    private Integer rankYesterday;
    private Integer drankWeek;
    private Integer drankMonth;
    private Integer rank;
    private Integer drank;
    private Integer drankYesterday;
    private Integer srank;
    private Integer sdrank;
    private Long dataCreated;
    private Long dataModified;
    private Long downloadDataModified;
    private Map<Integer, Long> genreDataModified;
    private Map<Integer, Long> genreDownloadDataModified;
    private Float price;
    private String link;
    private List<String> images;
    private Float revenueAvg;
    private Float revenueSum;
    private Float downloadAvg;
    private Integer downloadSum;
    private List<Integer> appGenres;
    private Map<String, Integer> appGenreRanks;
    private Map<String, List<Rank>> appGenreHistories;
    private Map<String, Integer> appGenreDranks;
    private Map<String, List<Rank>> appGenreDhistories;
    private Map<String, Integer> appGenreSranks;
    private Map<String, Integer> appGenreSdranks;
    private List<Rank> history;
    private List<Rank> dhistory;
    private List<Rank> historyWeek;
    private List<Rank> dhistoryWeek;
    private List<Rank> historyMonth;
    private List<Rank> dhistoryMonth;
    private List<Rank> srankHistory;
    private List<Rank> sdrankHistory;
    private Float gpsChange;
    private Float gps;
    private String type;
    private String genre;
    private Integer genreId;
    private String subGenre;
    private Integer subGenreId;
    private String gameId;
    private Long top10Entry;
    private Long top10Exit;
    private Long top20Entry;
    private Long top20Exit;
    private Long top50Entry;
    private Long top50Exit;
    private Long top100Entry;
    private Long top100Exit;
    private Long top200Entry;
    private Long top200Exit;
    private Boolean changesInFeatures;
    private Long lastAnalysisAt;
    private Long lastScoreAt;
    private Long lastRankTrendSent;
    private Boolean top200DropNotified;
    private Long createdAt;
    private Long modifiedAt;

    public Game() {
        // empty
    }

    @JsonProperty("released")
    public Long getReleasedAsLong() {
        return released != null ? released.toEpochMilli() : null;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Instant getReleased() {
        return released;
    }

    public void setReleased(Instant released) {
        this.released = released;
    }

    public Integer getRankMonth() {
        return rankMonth;
    }

    public void setRankMonth(Integer rankMonth) {
        this.rankMonth = rankMonth;
    }

    public Integer getRankWeek() {
        return rankWeek;
    }

    public void setRankWeek(Integer rankWeek) {
        this.rankWeek = rankWeek;
    }

    public Integer getRankYesterday() {
        return rankYesterday;
    }

    public void setRankYesterday(Integer rankYesterday) {
        this.rankYesterday = rankYesterday;
    }

    public Integer getDrankWeek() {
        return drankWeek;
    }

    public void setDrankWeek(Integer drankWeek) {
        this.drankWeek = drankWeek;
    }

    public Integer getDrankMonth() {
        return drankMonth;
    }

    public void setDrankMonth(Integer drankMonth) {
        this.drankMonth = drankMonth;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getDrank() {
        return drank;
    }

    public void setDrank(Integer drank) {
        this.drank = drank;
    }

    public Integer getDrankYesterday() {
        return drankYesterday;
    }

    public void setDrankYesterday(Integer drankYesterday) {
        this.drankYesterday = drankYesterday;
    }

    public Integer getSrank() {
        return srank;
    }

    public void setSrank(Integer srank) {
        this.srank = srank;
    }

    public Integer getSdrank() {
        return sdrank;
    }

    public void setSdrank(Integer sdrank) {
        this.sdrank = sdrank;
    }

    public Long getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Long dataCreated) {
        this.dataCreated = dataCreated;
    }

    public Long getDataModified() {
        return dataModified;
    }

    public void setDataModified(Long dataModified) {
        this.dataModified = dataModified;
    }

    public Long getDownloadDataModified() {
        return downloadDataModified;
    }

    public void setDownloadDataModified(Long downloadDataModified) {
        this.downloadDataModified = downloadDataModified;
    }

    public Map<Integer, Long> getGenreDataModified() {
        return genreDataModified;
    }

    public void setGenreDataModified(Map<Integer, Long> genreDataModified) {
        this.genreDataModified = genreDataModified;
    }

    public Map<Integer, Long> getGenreDownloadDataModified() {
        return genreDownloadDataModified;
    }

    public void setGenreDownloadDataModified(Map<Integer, Long> genreDownloadDataModified) {
        this.genreDownloadDataModified = genreDownloadDataModified;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Float getRevenueAvg() {
        return revenueAvg;
    }

    public void setRevenueAvg(Float revenueAvg) {
        this.revenueAvg = revenueAvg;
    }

    public Float getRevenueSum() {
        return revenueSum;
    }

    public void setRevenueSum(Float revenueSum) {
        this.revenueSum = revenueSum;
    }

    public Float getDownloadAvg() {
        return downloadAvg;
    }

    public void setDownloadAvg(Float downloadAvg) {
        this.downloadAvg = downloadAvg;
    }

    public Integer getDownloadSum() {
        return downloadSum;
    }

    public void setDownloadSum(Integer downloadSum) {
        this.downloadSum = downloadSum;
    }

    public List<Integer> getAppGenres() {
        return appGenres;
    }

    public void setAppGenres(List<Integer> appGenres) {
        this.appGenres = appGenres;
    }

    public Map<String, Integer> getAppGenreRanks() {
        return appGenreRanks;
    }

    public void setAppGenreRanks(Map<String, Integer> appGenreRanks) {
        this.appGenreRanks = appGenreRanks;
    }

    public Map<String, List<Rank>> getAppGenreHistories() {
        return appGenreHistories;
    }

    public void setAppGenreHistories(Map<String, List<Rank>> appGenreHistories) {
        this.appGenreHistories = appGenreHistories;
    }

    public Map<String, Integer> getAppGenreDranks() {
        return appGenreDranks;
    }

    public void setAppGenreDranks(Map<String, Integer> appGenreDranks) {
        this.appGenreDranks = appGenreDranks;
    }

    public Map<String, List<Rank>> getAppGenreDhistories() {
        return appGenreDhistories;
    }

    public void setAppGenreDhistories(Map<String, List<Rank>> appGenreDhistories) {
        this.appGenreDhistories = appGenreDhistories;
    }

    public Map<String, Integer> getAppGenreSranks() {
        return appGenreSranks;
    }

    public void setAppGenreSranks(Map<String, Integer> appGenreSranks) {
        this.appGenreSranks = appGenreSranks;
    }

    public Map<String, Integer> getAppGenreSdranks() {
        return appGenreSdranks;
    }

    public void setAppGenreSdranks(Map<String, Integer> appGenreSdranks) {
        this.appGenreSdranks = appGenreSdranks;
    }

    public List<Rank> getHistory() {
        return history;
    }

    public void setHistory(List<Rank> history) {
        this.history = history;
    }

    public List<Rank> getDhistory() {
        return dhistory;
    }

    public void setDhistory(List<Rank> dhistory) {
        this.dhistory = dhistory;
    }

    public List<Rank> getHistoryWeek() {
        return historyWeek;
    }

    public void setHistoryWeek(List<Rank> historyWeek) {
        this.historyWeek = historyWeek;
    }

    public List<Rank> getDhistoryWeek() {
        return dhistoryWeek;
    }

    public void setDhistoryWeek(List<Rank> dhistoryWeek) {
        this.dhistoryWeek = dhistoryWeek;
    }

    public List<Rank> getHistoryMonth() {
        return historyMonth;
    }

    public void setHistoryMonth(List<Rank> historyMonth) {
        this.historyMonth = historyMonth;
    }

    public List<Rank> getDhistoryMonth() {
        return dhistoryMonth;
    }

    public void setDhistoryMonth(List<Rank> dhistoryMonth) {
        this.dhistoryMonth = dhistoryMonth;
    }

    public List<Rank> getSrankHistory() {
        return srankHistory;
    }

    public void setSrankHistory(List<Rank> srankHistory) {
        this.srankHistory = srankHistory;
    }

    public List<Rank> getSdrankHistory() {
        return sdrankHistory;
    }

    public void setSdrankHistory(List<Rank> sdrankHistory) {
        this.sdrankHistory = sdrankHistory;
    }

    public Float getGpsChange() {
        return gpsChange;
    }

    public void setGpsChange(Float gpsChange) {
        this.gpsChange = gpsChange;
    }

    public Float getGps() {
        return gps;
    }

    public void setGps(Float gps) {
        this.gps = gps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(String subGenre) {
        this.subGenre = subGenre;
    }

    public Integer getSubGenreId() {
        return subGenreId;
    }

    public void setSubGenreId(Integer subGenreId) {
        this.subGenreId = subGenreId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Long getTop10Entry() {
        return top10Entry;
    }

    public void setTop10Entry(Long top10Entry) {
        this.top10Entry = top10Entry;
    }

    public Long getTop10Exit() {
        return top10Exit;
    }

    public void setTop10Exit(Long top10Exit) {
        this.top10Exit = top10Exit;
    }

    public Long getTop20Entry() {
        return top20Entry;
    }

    public void setTop20Entry(Long top20Entry) {
        this.top20Entry = top20Entry;
    }

    public Long getTop20Exit() {
        return top20Exit;
    }

    public void setTop20Exit(Long top20Exit) {
        this.top20Exit = top20Exit;
    }

    public Long getTop50Entry() {
        return top50Entry;
    }

    public void setTop50Entry(Long top50Entry) {
        this.top50Entry = top50Entry;
    }

    public Long getTop50Exit() {
        return top50Exit;
    }

    public void setTop50Exit(Long top50Exit) {
        this.top50Exit = top50Exit;
    }

    public Long getTop100Entry() {
        return top100Entry;
    }

    public void setTop100Entry(Long top100Entry) {
        this.top100Entry = top100Entry;
    }

    public Long getTop100Exit() {
        return top100Exit;
    }

    public void setTop100Exit(Long top100Exit) {
        this.top100Exit = top100Exit;
    }

    public Long getTop200Entry() {
        return top200Entry;
    }

    public void setTop200Entry(Long top200Entry) {
        this.top200Entry = top200Entry;
    }

    public Long getTop200Exit() {
        return top200Exit;
    }

    public void setTop200Exit(Long top200Exit) {
        this.top200Exit = top200Exit;
    }

    public Boolean getChangesInFeatures() {
        return changesInFeatures;
    }

    public void setChangesInFeatures(Boolean changesInFeatures) {
        this.changesInFeatures = changesInFeatures;
    }

    public Long getLastAnalysisAt() {
        return lastAnalysisAt;
    }

    public void setLastAnalysisAt(Long lastAnalysisAt) {
        this.lastAnalysisAt = lastAnalysisAt;
    }

    public Long getLastScoreAt() {
        return lastScoreAt;
    }

    public void setLastScoreAt(Long lastScoreAt) {
        this.lastScoreAt = lastScoreAt;
    }

    public Long getLastRankTrendSent() {
        return lastRankTrendSent;
    }

    public void setLastRankTrendSent(Long lastRankTrendSent) {
        this.lastRankTrendSent = lastRankTrendSent;
    }

    public Boolean getTop200DropNotified() {
        return top200DropNotified;
    }

    public void setTop200DropNotified(Boolean top200DropNotified) {
        this.top200DropNotified = top200DropNotified;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
