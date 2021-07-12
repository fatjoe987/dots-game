package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;






public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "sHw73FF+";
    public static final String SET_RATING =
            "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?) ON CONFLICT (player, game)" +
                    " DO UPDATE SET rating = excluded.rating, ratedon = excluded.ratedon";
    public static final String SELECT_RATING =
            "SELECT player, game, rating, ratedon FROM rating WHERE game = ? AND player = ?";
    public static final String AVERAGE_RATING =
            "SELECT AVG(rating) FROM rating WHERE game = ?";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SET_RATING)){
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setTimestamp(4, new Timestamp(rating.getRatedon().getTime()));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(AVERAGE_RATING)){
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading average rating", e);
        }
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_RATING)){
                ps.setString(1, game);
                ps.setString(2, player);
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()) {
                        Rating rating = new Rating(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        return rating.getRating();
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Rating rating = new Rating("jaro", "dots", 5, new Timestamp(System.currentTimeMillis()));
        RatingService ratingService = new RatingServiceJDBC();
        ratingService.setRating(rating);
        System.out.println(ratingService.getRating("dots","jaro"));
        System.out.println(ratingService.getAverageRating("dots"));
    }
}
