package com.example.tg;

import com.example.tg.dto.CurrentTopRequestDto;
import com.example.tg.dto.FiltersAndPagingDto;
import com.example.tg.dto.RankHistoryRequestDto;
import com.example.tg.dto.RecentTopRequestDto;
import com.example.tg.models.Game;
import com.example.tg.models.Rank;
import com.example.tg.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/v1.0/games/")
public class GameRest {
    private static ObjectMapper mapper = new ObjectMapper();

    @Inject
    private GameDataRepository gameRepo;

    @Inject
    private Validator validator;

    @Inject
    private UserDataRepository userRepo;

    private <T> void validateRequest(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            final String message = violations.stream()
                    .map(vi -> vi.getPropertyPath() + " " + vi.getMessage())
                    .collect(Collectors.joining(" , "));
            throw ResponseFactory.generateBadRequestException(message);
        }
    }

    private <T> JsonNode convertToJsonAndFilter(T convertible, Collection<String> filters,
            boolean retain) {
        ObjectNode asJson = mapper.valueToTree(convertible);
        if (retain) {
            return asJson.retain(filters);
        }
        return asJson.remove(filters);
    }

    /**
     * Find n top games ordered by their rank. Supports filtering and pagination.
     *
     * @param request options
     * @return found games
     */
    @POST
    @Path("current_top")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonNode currentTop(@NotNull CurrentTopRequestDto request) {
        validateRequest(request);
        boolean usePaging = Utils.validatePagination(request.getPage(), request.getPageSize());

        Map<String, Integer> filters =
                Optional.ofNullable(request.getFilters()).orElse(Collections.emptyMap());

        boolean retain = Utils.validateFiltersAndGetRetain(filters);

        List<Game> games;
        if (usePaging) {
            games = gameRepo.topGames(request.getCount(), request.getMarket(),
                    filters.keySet(), retain, request.getPage(), request.getPageSize());
        } else {
            games = gameRepo.topGames(request.getCount(), request.getMarket(),
                    filters.keySet(), retain);
        }

        return mapper.valueToTree(
                games.stream()
                        .map(game -> convertToJsonAndFilter(game, filters.keySet(), retain))
                        .collect(Collectors.toList()));
    }

    /**
     * Find games that have given amount of days ago entered top10 or top20 or top50.
     *
     * @param request options
     * @return found games
     */
    @POST
    @Path("recent_top")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonNode recentTop(@NotNull RecentTopRequestDto request) {
        validateRequest(request);
        boolean usePaging = Utils.validatePagination(request.getPage(), request.getPageSize());

        Map<String, Integer> filters =
                Optional.ofNullable(request.getFilters()).orElse(Collections.emptyMap());
        boolean retain = Utils.validateFiltersAndGetRetain(filters);
        List<String> markets =
                Optional.ofNullable(request.getMarkets()).orElse(Collections.emptyList());

        List<Game> games;
        if (usePaging) {
            games = gameRepo.recentTop(markets, request.getDaysAgo(),
                    request.getTopAmount(), filters.keySet(), retain, request.getPage(),
                    request.getPageSize());
        } else {
            games = gameRepo.recentTop(markets, request.getDaysAgo(),
                    request.getTopAmount(), filters.keySet(), retain);
        }

        Map<String, List<JsonNode>> gamesByMarket = new HashMap<>();
        games.stream().forEach(game -> {
            if (game.getMarket() == null) {
                game.setMarket("unknown");
            }
            if (!gamesByMarket.containsKey(game.getMarket())) {
                gamesByMarket.put(game.getMarket(), new ArrayList<>());
            }
            gamesByMarket.get(game.getMarket())
                    .add(convertToJsonAndFilter(game, filters.keySet(), retain));
        });

        return mapper.valueToTree(gamesByMarket);
    }

    private User secToUser(SecurityContext sec) {
        Optional<User> user = userRepo.findUser(sec.getUserPrincipal().getName());
        if (!user.isPresent()) {
            // SecurityContext has been populated by username found from the database, so we never
            // get here.
            throw new InternalServerErrorException();
        }
        return user.get();
    }

    /**
     * Mark a game as favourite. 404 if game not found.
     *
     * @param market market of the game
     * @param appId appId of the game
     * @param sec injected security context for username
     * @return 204 if all ok
     */
    @PUT
    @Path("favourite/{market}/{appId}")
    public Response createFavourite(@PathParam("market") String market,
            @PathParam("appId") Integer appId,
            @Context SecurityContext sec) {
        Optional<Game> game = gameRepo.getGame(market, appId);
        if (!game.isPresent()) {
            return ResponseFactory.generateNotFoundResponse("game not found");
        }

        User user = secToUser(sec);
        user.getFavouriteGames().add(game.get().getId());
        userRepo.saveUser(user);

        return ResponseFactory.generateOkNoContentResponse();
    }

    /**
     * Unmark game as favourite. 404 if game not found.
     * @param market market of the game
     * @param appId appId of the game
     * @param sec injected security context for username
     * @return 204 if all ok
     */
    @DELETE
    @Path("favourite/{market}/{appId}")
    public Response deleteFavourite(@PathParam("market") String market,
            @PathParam("appId") Integer appId,
            @Context SecurityContext sec) {
        Optional<Game> game = gameRepo.getGame(market, appId);
        if (!game.isPresent()) {
            return ResponseFactory.generateNotFoundResponse("game not found");
        }

        User user = secToUser(sec);
        user.getFavouriteGames().remove(game.get().getId());
        userRepo.saveUser(user);

        return ResponseFactory.generateOkNoContentResponse();
    }

    /**
     * Get all the favourite games.
     * @param request options
     * @param sec injected security context for username
     * @return found games
     */
    @POST
    @Path("favourite/find_all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonNode listFavourites(@NotNull FiltersAndPagingDto request,
            @Context SecurityContext sec) {
        validateRequest(request);
        Map<String, Integer> filters =
                Optional.ofNullable(request.getFilters()).orElse(Collections.emptyMap());
        boolean retain = Utils.validateFiltersAndGetRetain(filters);
        User user = secToUser(sec);
        boolean usePaging = Utils.validatePagination(request.getPage(), request.getPageSize());

        List<Game> games;
        if (usePaging) {
            games = gameRepo.getGamesByIds(user.getFavouriteGames(), filters.keySet(), retain,
                    request.getPage(), request.getPageSize());
        } else {
            games = gameRepo.getGamesByIds(user.getFavouriteGames(), filters.keySet(), retain);
        }

        return mapper.valueToTree(
                games.stream()
                        .map(game -> convertToJsonAndFilter(game, filters.keySet(), retain))
                        .collect(Collectors.toList()));
    }

    /**
     * Get rank history of a game.
     * @param request options
     * @param market market of the game
     * @param appId appId of the game
     * @return rank history (daily, weekly or monthly) of game or 404
     */
    @POST
    @Path("{market}/{appId}/rank_history")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rankHistory(@NotNull RankHistoryRequestDto request,
            @PathParam("market") String market,
            @PathParam("appId") Integer appId) {
        validateRequest(request);
        Map<String, Integer> filters =
                Optional.ofNullable(request.getFilters()).orElse(Collections.emptyMap());
        boolean retain = Utils.validateFiltersAndGetRetain(filters);

        Optional<Game> game = gameRepo.rankHistory(appId, market, request.getFrequency());

        if (!game.isPresent()) {
            return ResponseFactory.generateNotFoundResponse(
                    "There doesn't exist any game with given market and appId");
        }

        List<Rank> rankList;
        switch (request.getFrequency()) {
            case DAILY:
                rankList = game.get().getHistory();
                break;
            case WEEKLY:
                rankList = game.get().getHistoryWeek();
                break;
            default:
                rankList = game.get().getHistoryMonth();
                break;
        }

        return Response.ok(
                rankList.stream()
                        .map(rank -> convertToJsonAndFilter(rank, filters.keySet(), retain))
                        .collect(Collectors.toList()))
                .build();
    }
}
