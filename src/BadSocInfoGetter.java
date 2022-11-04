import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

class BookingDetails
{
    private String name = "[None]";
    private boolean isMember = false;
    private String bookingSelected = "[None]";

    public BookingDetails(String name, String bookingSelected, boolean isMember){
        this.name = name;
        this.isMember = isMember;
        this.bookingSelected = bookingSelected;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public String getBookingSelected() {
        return bookingSelected;
    }

    public void setBookingSelected(String bookingSelected) {
        this.bookingSelected = bookingSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class BadSocInfoGetter {

    public List<List<String>> getBookings(CSVReader reader)
    {
        //
        String bookingsCSV = "static/bookings.csv";
        List<List<String>> bookings = reader.readCSV(bookingsCSV);
        return bookings;
    }

    public List<List<String>> getIndexInfo(CSVReader reader)
    {
        //
        String columnIndexesCSV = "static/input_col_info.csv";
        List<List<String>> columnIndexes = reader.readCSV(columnIndexesCSV);
        return columnIndexes;
    }

    public List<List<String>> getMembers(CSVReader reader)
    {
        //
        String membersCSV = "static/members.csv";
        List<List<String>> members = reader.readCSV(membersCSV);
        return members;
    }


    public void getAllInfo()
    {
        CSVReader reader = new CSVReader();

        //get index info
        // get columns from index info
        List<List<String>> columnIndexes = getIndexInfo(reader);

        System.out.println("col indexes: ");
        //set default values
        int bookings_name_index = 0;
        int bookings_session_index = 0;
        int members_forename_col_index = 0;
        int members_surname_col_index = 0;

        for(List<String> entry : columnIndexes){
            System.out.println("------------");
            //must have 2 entries; name and the index
            if(entry.size() != 2) continue;

            String colID = entry.get(0).strip();
            int index = Integer.parseInt(entry.get(1).strip());

            if(colID.equals("bookings_name_index")) bookings_name_index = index;
            else if(colID.equals("bookings_session_index")) bookings_session_index = index;
            else if(colID.equals("members_forename_col_index")) members_forename_col_index = index;
            else if(colID.equals("members_surname_col_index")) members_surname_col_index = index;
        }


        HashMap<String, BookingDetails> nameToSessionAllBookees = new HashMap<>();
        HashMap<String, ArrayList<BookingDetails>> sessionToBookees = new HashMap<>();

        //get bookings
        //cut down to relevant columns
        List<List<String>> bookings = getBookings(reader);


        for(List<String> entry : bookings){
            //get info
            String book_name = entry.get(bookings_name_index).strip().toLowerCase();
            String book_session = entry.get(bookings_session_index).strip();

            //create booking entry
            BookingDetails bookingEntry = new BookingDetails(book_name, book_session, false);

            //update name to bookee
            nameToSessionAllBookees.put(book_name,  bookingEntry);

            //update session to bookees
            if(sessionToBookees.containsKey(book_session)){
                ArrayList<BookingDetails> existingDetails = sessionToBookees.get(book_session);
                existingDetails.add(bookingEntry);
                sessionToBookees.put(book_session, existingDetails);
            }
            else{
                ArrayList<BookingDetails> bookeeDetails = new ArrayList<>();
                bookeeDetails.add(bookingEntry);
                sessionToBookees.put(book_session, bookeeDetails);
            }
        }

        //get members
        //cut down to relevant columns
        List<List<String>> members = getMembers(reader);
        ArrayList<String> membersNames = new ArrayList<>();
        for(List<String> entry : members){
            String members_forename = entry.get(members_forename_col_index).strip().toLowerCase();
            String members_surname = entry.get(members_surname_col_index).strip().toLowerCase();
            String fullname = members_forename + " " + members_surname;
            membersNames.add(fullname);

            if(nameToSessionAllBookees.containsKey(fullname)){
                System.out.println("The bookee: "+ fullname+ " has been found!!!");
                BookingDetails existingDetails = nameToSessionAllBookees.get(fullname);
                existingDetails.setMember(true);
                nameToSessionAllBookees.put(fullname, existingDetails);
            }
        }

        for (String name : nameToSessionAllBookees.keySet()) {
            BookingDetails existingDetails = nameToSessionAllBookees.get(name);
            String booking = existingDetails.getBookingSelected();
            boolean isMember = existingDetails.isMember();
            System.out.println(name + " | " + booking + " | " + isMember);
        }

        //nameToSessionAllBookees
        //sessionToBookees


        //sort alphabetically
        for(String session : sessionToBookees.keySet()){
            ArrayList<BookingDetails> bookeesForThatSession = sessionToBookees.get(session);

            System.out.println("\nFor "+session+":");

            for(int i = 0; i < bookeesForThatSession.size(); i++){
                BookingDetails bookingDetails = bookeesForThatSession.get(i);
                System.out.print("("+(i+1)+") "+bookingDetails.getName());
                for(int j = bookingDetails.getName().length(); j < 50; j++) System.out.print("");
                System.out.print(" | " + bookingDetails.isMember()+ "\n");
            }
        }
    }

    public static void main(String[] args)
    {
        new BadSocInfoGetter().getAllInfo();
    }
}
