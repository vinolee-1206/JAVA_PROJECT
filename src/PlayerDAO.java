import java.sql.*;

public class PlayerDAO {
    private Connection con;

    public PlayerDAO(Connection con) {
        this.con = con;
    }

    public void addPlayer(String table, int gameChoice, String name, String position, int jersey) {
        String sql = "INSERT INTO " + table + " (player_name, position, jersey_number, game_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, position);
            ps.setInt(3, jersey);
            ps.setInt(4, gameChoice);
            ps.executeUpdate();
            System.out.println("Player added successfully.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Jersey number must be unique.");
        } catch (Exception e) {
            System.out.println("Error adding player: " + e.getMessage());
        }
    }

    public void viewPlayers(String table) throws SQLException {
        String sql = "SELECT * FROM " + table;
        try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nPlayer list:");
            System.out.printf("%-5s %-20s %-15s %-10s %-7s\n", "ID", "Name", "Position", "Jersey#", "GameID");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("player_id");
                String name = rs.getString("player_name");
                String position = rs.getString("position");
                int jersey = rs.getInt("jersey_number");
                int gameId = rs.getInt("game_id");

                System.out.printf("%-5d %-20s %-15s %-10d %-7d\n", id, name, position, jersey, gameId);
            }
        }
    }

    public void updatePlayer(String table, int id, String name, String position, int jersey) {
        String sql = "UPDATE " + table + " SET player_name = ?, position = ?, jersey_number = ? WHERE player_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, position);
            ps.setInt(3, jersey);
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("Player updated successfully.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Jersey number must be unique.");
        } catch (Exception e) {
            System.out.println("Error updating player: " + e.getMessage());
        }
    }

    public void deletePlayer(String table, int id) {
        String sql = "DELETE FROM " + table + " WHERE player_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Player deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting player: " + e.getMessage());
        }
    }

    public boolean playerExists(String table, int id) throws SQLException {
        String sql = "SELECT player_id FROM " + table + " WHERE player_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void viewAllPlayersAcrossGames() throws SQLException {
        String sql =
            "SELECT player_id, player_name, position, jersey_number, game_name " +
            "FROM VolleyballPlayers vp JOIN Games g ON vp.game_id = g.game_id " +
            "UNION ALL " +
            "SELECT player_id, player_name, position, jersey_number, game_name " +
            "FROM ThrowballPlayers tp JOIN Games g ON tp.game_id = g.game_id " +
            "UNION ALL " +
            "SELECT player_id, player_name, position, jersey_number, game_name " +
            "FROM FootballPlayers fp JOIN Games g ON fp.game_id = g.game_id " +
            "UNION ALL " +
            "SELECT player_id, player_name, position, jersey_number, game_name " +
            "FROM KhoKhoPlayers kp JOIN Games g ON kp.game_id = g.game_id " +
            "ORDER BY game_name, player_name";

        try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nAll Players Across All Games:");
            System.out.printf("%-5s %-20s %-15s %-10s %-15s\n", "ID", "Name", "Position", "Jersey#", "Game");
            System.out.println("-------------------------------------------------------------------");

            int totalPlayers = 0;
            while (rs.next()) {
                int id = rs.getInt("player_id");
                String name = rs.getString("player_name");
                String position = rs.getString("position");
                int jersey = rs.getInt("jersey_number");
                String game = rs.getString("game_name");

                System.out.printf("%-5d %-20s %-15s %-10d %-15s\n", id, name, position, jersey, game);
                totalPlayers++;
            }
            System.out.println("\nTotal number of players across all games: " + totalPlayers);
        }
    }
}