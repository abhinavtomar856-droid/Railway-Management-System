import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== RAILWAY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Train");
            System.out.println("2. View Trains");
            System.out.println("3. Book Ticket");
            System.out.println("4. View Bookings");
            System.out.println("5. Search Train");
            System.out.println("6. Cancel Ticket");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            try {
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Train_db",
                        "root",
                        "admin");

                // 1. ADD TRAIN
                if (choice == 1) {
                    System.out.print("Train Number: ");
                    int num = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Train Name: ");
                    String name = sc.nextLine();

                    System.out.print("Source: ");
                    String source = sc.nextLine();

                    System.out.print("Destination: ");
                    String dest = sc.nextLine();

                    System.out.print("Seats: ");
                    int seats = sc.nextInt();
                    sc.nextLine();

                    String query = "INSERT INTO trains VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);

                    ps.setInt(1, num);
                    ps.setString(2, name);
                    ps.setString(3, source);
                    ps.setString(4, dest);
                    ps.setInt(5, seats);

                    ps.executeUpdate();
                    System.out.println("Train added!");
                }

                // 2. VIEW TRAINS
                else if (choice == 2) {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM trains");

                    while (rs.next()) {
                        System.out.println("Train No: " + rs.getInt(1));
                        System.out.println("Name: " + rs.getString(2));
                        System.out.println("Source: " + rs.getString(3));
                        System.out.println("Destination: " + rs.getString(4));
                        System.out.println("Seats: " + rs.getInt(5));
                        System.out.println("----------------------");
                    }
                }

                // 3. BOOK TICKET
                else if (choice == 3) {
                    System.out.print("Passenger Name: ");
                    String pname = sc.nextLine();

                    System.out.print("Train Number: ");
                    int tno = sc.nextInt();

                    System.out.print("Seats: ");
                    int seats = sc.nextInt();
                    sc.nextLine();

                    PreparedStatement ps1 = con.prepareStatement(
                            "SELECT availableSeats FROM trains WHERE trainNumber=?");
                    ps1.setInt(1, tno);

                    ResultSet rs = ps1.executeQuery();

                    if (rs.next()) {
                        int available = rs.getInt(1);

                        if (available >= seats) {

                            PreparedStatement ps2 = con.prepareStatement(
                                    "INSERT INTO bookings(passengerName, trainNumber, seatsBooked) VALUES (?, ?, ?)");
                            ps2.setString(1, pname);
                            ps2.setInt(2, tno);
                            ps2.setInt(3, seats);
                            ps2.executeUpdate();

                            PreparedStatement ps3 = con.prepareStatement(
                                    "UPDATE trains SET availableSeats = availableSeats - ? WHERE trainNumber=?");
                            ps3.setInt(1, seats);
                            ps3.setInt(2, tno);
                            ps3.executeUpdate();

                            System.out.println("Ticket booked!");
                        } else {
                            System.out.println("Not enough seats.");
                        }
                    } else {
                        System.out.println("Train not found.");
                    }
                }

                // 4. VIEW BOOKINGS
                else if (choice == 4) {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM bookings");

                    while (rs.next()) {
                        System.out.println("Booking ID: " + rs.getInt(1));
                        System.out.println("Name: " + rs.getString(2));
                        System.out.println("Train No: " + rs.getInt(3));
                        System.out.println("Seats: " + rs.getInt(4));
                        System.out.println("-------------------");
                    }
                }

                // 5. SEARCH TRAIN
                else if (choice == 5) {
                    System.out.print("Enter Train Number: ");
                    int tno = sc.nextInt();

                    PreparedStatement ps = con.prepareStatement(
                            "SELECT * FROM trains WHERE trainNumber=?");
                    ps.setInt(1, tno);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        System.out.println("Train Found:");
                        System.out.println("Name: " + rs.getString(2));
                        System.out.println("Source: " + rs.getString(3));
                        System.out.println("Destination: " + rs.getString(4));
                        System.out.println("Seats: " + rs.getInt(5));
                    } else {
                        System.out.println("Train not found!");
                    }
                }

                // 6. CANCEL TICKET
                else if (choice == 6) {
                    System.out.print("Enter Booking ID to cancel: ");
                    int id = sc.nextInt();

                    PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM bookings WHERE id=?");
                    ps.setInt(1, id);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Ticket Cancelled!");
                    } else {
                        System.out.println("Invalid ID!");
                    }
                }

                con.close();
            }

            catch (Exception e) {
                System.out.println(e);
            }

        } while (choice != 7);

        sc.close();
    }
}