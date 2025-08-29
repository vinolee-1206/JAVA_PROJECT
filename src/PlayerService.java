import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class PlayerService {
    private PlayerDAO dao;
    private Scanner sc;

    public PlayerService(Connection con, Scanner sc) {
        this.dao = new PlayerDAO(con);
        this.sc = sc;
    }

    public void handleCRUD(String table, int gameChoice) throws SQLException {
        while (true) {
            System.out.println("\n-- Manage " + table + " --");
            System.out.println("1. Add Player");
            System.out.println("2. View All Players");
            System.out.println("3. Update Player");
            System.out.println("4. Delete Player");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter player name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter position: ");
                    String position = sc.nextLine();
                    System.out.print("Enter jersey number: ");
                    int jersey = Integer.parseInt(sc.nextLine());
                    dao.addPlayer(table, gameChoice, name, position, jersey);
                    break;
                case 2:
                    dao.viewPlayers(table);
                    break;
                case 3:
                    System.out.print("Enter player ID to update: ");
                    int updateId = Integer.parseInt(sc.nextLine());
                    if (!dao.playerExists(table, updateId)) {
                        System.out.println("Player not found.");
                        break;
                    }
                    System.out.print("Enter new player name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new position: ");
                    String newPos = sc.nextLine();
                    System.out.print("Enter new jersey number: ");
                    int newJersey = Integer.parseInt(sc.nextLine());
                    dao.updatePlayer(table, updateId, newName, newPos, newJersey);
                    break;
                case 4:
                    System.out.print("Enter player ID to delete: ");
                    int deleteId = Integer.parseInt(sc.nextLine());
                    if (!dao.playerExists(table, deleteId)) {
                        System.out.println("Player not found.");
                        break;
                    }
                    dao.deletePlayer(table, deleteId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static String getTableName(int gameChoice) {
        switch (gameChoice) {
            case 1: return "VolleyballPlayers";
            case 2: return "ThrowballPlayers";
            case 3: return "FootballPlayers";
            case 4: return "KhoKhoPlayers";
            default: return null;
        }
    }
}