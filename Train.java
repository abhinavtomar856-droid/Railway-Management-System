public class Train {
    int trainNumber;
    String trainName;
    String source;
    String destination;
    int availableSeats;

    public Train(int trainNumber, String trainName, String source, String destination, int availableSeats) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public void showTrain() {
        System.out.println("Train Number: " + trainNumber);
        System.out.println("Train Name: " + trainName);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("---------------------------");
    }
}