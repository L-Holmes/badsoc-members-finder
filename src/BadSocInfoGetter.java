import java.util.List;

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

        //get bookings
        //cut down to relevant columns
        List<List<String>> bookings = getBookings(reader);
        for(List<String> entry : bookings){
            String book_name = entry.get(bookings_name_index);
            String book_session = entry.get(bookings_session_index);
            System.out.println("----------------");
            System.out.println(book_name + " ~ "+ book_session);
        }

        //get members
        //cut down to relevant columns
        List<List<String>> members = getMembers(reader);
        for(List<String> entry : members){
            String members_forename = entry.get(members_forename_col_index);
            String members_surname = entry.get(members_surname_col_index);
            System.out.println("----------------");
            System.out.println(members_forename + " ~ "+ members_surname);
        }
    }

    public static void main(String[] args)
    {
        new BadSocInfoGetter().getAllInfo();
    }
}
