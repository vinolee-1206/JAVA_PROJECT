import java.sql.Connection;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
            Scanner sc = new Scanner(System.in)) {

            PlayerService service = new PlayerService(con, sc);

            while (true) {
                System.out.println("\n=== SPORTS MANAGEMENT MENU ===");
                System.out.println("1. Volleyball Players");
                System.out.println("2. Throwball Players");
                System.out.println("3. Football Players");
                System.out.println("4. Kho Kho Players");
                System.out.println("5. Show All Players (All Games)");
                System.out.println("6. Exit");
                System.out.print("Choose a game: ");

                int gameChoice = Integer.parseInt(sc.nextLine());

                if (gameChoice == 6) {
                    System.out.println("Exiting program...");
                    break;
                }

                if (gameChoice == 5) {
                    new PlayerDAO(con).viewAllPlayersAcrossGames();
                    continue;
                }

                String tableName = PlayerService.getTableName(gameChoice);
                if (tableName == null) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }

                service.handleCRUD(tableName, gameChoice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}