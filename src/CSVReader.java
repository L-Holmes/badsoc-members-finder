import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * https://www.baeldung.com/java-csv-file-array
 */

public class CSVReader {
    public  String COMMA_DELIMITER = ",";

    public List<List<String>> readCSV(String csvPathName) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(csvPathName));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
            return records;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public static void main(String[] args)
    {
        CSVReader reader = new CSVReader();
        String bookingsCSV = "static/bookings.csv";
        String columnIndexesCSV = "static/input_col_info.csv";
        String membersCSV = "static/members.csv";


        List<List<String>> bookings = reader.readCSV(bookingsCSV);
        System.out.println("bookings: ");
        for(List<String> entry : bookings){
            System.out.println("------------");
            for(String rec : entry){
                System.out.print(rec + " | ");
            }
        }
        System.out.println();

        List<List<String>> columnIndexes = reader.readCSV(columnIndexesCSV);
        System.out.println("col indexes: ");
        for(List<String> entry : bookings){
            System.out.println("------------");
            for(String rec : entry){
                System.out.print(rec + " | ");
            }
        }

        List<List<String>> members = reader.readCSV(membersCSV);
        System.out.println("members: ");
        for(List<String> entry : members){
            System.out.println("------------");
            for(String rec : entry){
                System.out.print(rec + " | ");
            }
        }
        System.out.println();
    }
}
