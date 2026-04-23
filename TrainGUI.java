import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class TrainGUI extends JFrame implements ActionListener {

    JTextField t1, t2, t3, t4, t5, t6;
    JButton addBtn, viewBtn, bookBtn, viewBookBtn, searchBtn, cancelBtn;

    public TrainGUI() {
        setTitle("Train Management System");
        setSize(450, 450);
        setLayout(null);

        JLabel l1 = new JLabel("Train No:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);

        t1 = new JTextField();
        t1.setBounds(130, 20, 200, 25);
        add(t1);

        JLabel l2 = new JLabel("Name:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);

        t2 = new JTextField();
        t2.setBounds(130, 60, 200, 25);
        add(t2);

        JLabel l3 = new JLabel("Source:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);

        t3 = new JTextField();
        t3.setBounds(130, 100, 200, 25);
        add(t3);

        JLabel l4 = new JLabel("Destination:");
        l4.setBounds(20, 140, 100, 25);
        add(l4);

        t4 = new JTextField();
        t4.setBounds(130, 140, 200, 25);
        add(t4);

        JLabel l5 = new JLabel("Seats:");
        l5.setBounds(20, 180, 100, 25);
        add(l5);

        t5 = new JTextField();
        t5.setBounds(130, 180, 200, 25);
        add(t5);

        JLabel l6 = new JLabel("Booking ID:");
        l6.setBounds(20, 220, 100, 25);
        add(l6);

        t6 = new JTextField();
        t6.setBounds(130, 220, 200, 25);
        add(t6);

        addBtn = new JButton("Add Train");
        addBtn.setBounds(20, 260, 120, 30);
        add(addBtn);

        viewBtn = new JButton("View Trains");
        viewBtn.setBounds(160, 260, 150, 30);
        add(viewBtn);

        bookBtn = new JButton("Book Ticket");
        bookBtn.setBounds(20, 300, 120, 30);
        add(bookBtn);

        viewBookBtn = new JButton("View Bookings");
        viewBookBtn.setBounds(160, 300, 150, 30);
        add(viewBookBtn);

        searchBtn = new JButton("Search Train");
        searchBtn.setBounds(20, 340, 120, 30);
        add(searchBtn);

        cancelBtn = new JButton("Cancel Ticket");
        cancelBtn.setBounds(160, 340, 150, 30);
        add(cancelBtn);

        addBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        bookBtn.addActionListener(this);
        viewBookBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/train_db",
                    "root",
                    "admin"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
            return null;
        }
    }

    public void clearFields() {
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t4.setText("");
        t5.setText("");
        t6.setText("");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            Connection con = connect();

            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO trains VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(t1.getText()));
                ps.setString(2, t2.getText());
                ps.setString(3, t3.getText());
                ps.setString(4, t4.getText());
                ps.setInt(5, Integer.parseInt(t5.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Train Added!");
                clearFields();
            }

            // VIEW TRAINS
            else if (e.getSource() == viewBtn) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM trains");

                String data = "";
                while (rs.next()) {
                    data += "Train No: " + rs.getInt(1) + "\n";
                    data += "Name: " + rs.getString(2) + "\n";
                    data += "From: " + rs.getString(3) + "\n";
                    data += "To: " + rs.getString(4) + "\n";
                    data += "Seats: " + rs.getInt(5) + "\n\n";
                }

                JOptionPane.showMessageDialog(this, data);
            }

            else if (e.getSource() == bookBtn) {
                int trainNo = Integer.parseInt(t1.getText());
                int seats = Integer.parseInt(t5.getText());

                PreparedStatement ps1 = con.prepareStatement(
                        "SELECT availableSeats FROM trains WHERE trainNumber=?");
                ps1.setInt(1, trainNo);

                ResultSet rs = ps1.executeQuery();

                if (rs.next()) {
                    int available = rs.getInt(1);

                    if (available >= seats) {
                        PreparedStatement ps2 = con.prepareStatement(
                                "INSERT INTO bookings(passengerName, trainNumber, seatsBooked) VALUES (?, ?, ?)");
                        ps2.setString(1, t2.getText());
                        ps2.setInt(2, trainNo);
                        ps2.setInt(3, seats);
                        ps2.executeUpdate();

                        PreparedStatement ps3 = con.prepareStatement(
                                "UPDATE trains SET availableSeats = availableSeats - ? WHERE trainNumber=?");
                        ps3.setInt(1, seats);
                        ps3.setInt(2, trainNo);
                        ps3.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Ticket Booked!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough seats!");
                    }
                }
            }

            else if (e.getSource() == viewBookBtn) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bookings");

                String data = "";
                while (rs.next()) {
                    data += "ID: " + rs.getInt(1) + "\n";
                    data += "Name: " + rs.getString(2) + "\n";
                    data += "Train: " + rs.getInt(3) + "\n";
                    data += "Seats: " + rs.getInt(4) + "\n\n";
                }

                JOptionPane.showMessageDialog(this, data);
            }

            else if (e.getSource() == searchBtn) {
                int trainNo = Integer.parseInt(t1.getText());

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM trains WHERE trainNumber=?");
                ps.setInt(1, trainNo);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String data = "Train Found:\n";
                    data += "Name: " + rs.getString(2) + "\n";
                    data += "From: " + rs.getString(3) + "\n";
                    data += "To: " + rs.getString(4) + "\n";
                    data += "Seats: " + rs.getInt(5);

                    JOptionPane.showMessageDialog(this, data);
                } else {
                    JOptionPane.showMessageDialog(this, "Train not found!");
                }
            }

            else if (e.getSource() == cancelBtn) {
                int id = Integer.parseInt(t6.getText());

                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM bookings WHERE id=?");
                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Ticket Cancelled!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid ID!");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public static void main(String[] args) {
        new TrainGUI();
    }
}