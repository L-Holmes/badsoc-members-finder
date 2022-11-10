import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class BadSocInfoGetter {
    private String bookingsCSVPath = "static/bookings.csv";
    private String membersCSVPath = "static/members.csv";
    private String additionalMembersCSVPath = "static/additional_members.csv";
    private String commonNameVariantsPath = "static/common_name_shortenings.csv";

    public BadSocInfoGetter()
    {}

    public BadSocInfoGetter(String bookingsCSVPath, String membersCSVPath, String additionalMembersCSVPath)
    {
        this.bookingsCSVPath = bookingsCSVPath;
        this.membersCSVPath = membersCSVPath;
        this.additionalMembersCSVPath = additionalMembersCSVPath;
    }

    public List<List<String>> getBookings(CSVReader reader)
    {
        List<List<String>> bookings = reader.readCSV(bookingsCSVPath);

        //remove the first entry as this is the headings
        bookings.remove(0);

        return bookings;
    }

    public List<List<String>> getIndexInfo(CSVReader reader)
    {
        String columnIndexesCSV = "static/input_col_info.csv";
        return reader.readCSV(columnIndexesCSV);
    }

    public List<List<String>> getMembers(CSVReader reader)
    {
        return reader.readCSV(membersCSVPath);
    }

    public List<List<String>> getAdditionalMembers(CSVReader reader)
    {
        return reader.readCSV(additionalMembersCSVPath);
    }

    public List<List<String>> getCommonNameVariants(CSVReader reader)
    {
        return reader.readCSV(commonNameVariantsPath);
    }

    public void getAllInfo()
    {
        CSVReader reader = new CSVReader();

        //get common name variants
        List<List<String>> commonNames = getCommonNameVariants(reader);

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
            String book_name = entry.get(bookings_name_index).strip().toLowerCase().replace("\"", "").strip();
            String book_session = entry.get(bookings_session_index).strip();

            //create booking entry
            BookingDetails bookingEntry = new BookingDetails(book_name, book_session, false);

            //update name to bookee
            System.out.println("Adding book name: |" + book_name + "|");
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
        List<List<String>> members = getMembers(reader);
        List<List<String>> additionalMembers = getAdditionalMembers(reader);

        //update the memberships
        nameToSessionAllBookees = updateMembership(members, nameToSessionAllBookees, members_forename_col_index, members_surname_col_index, commonNames);
        nameToSessionAllBookees = updateMembership(additionalMembers, nameToSessionAllBookees, 0, 1, commonNames);

        //write to text file
        try {
            writeToTextFile(sessionToBookees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, BookingDetails> updateMembership(List<List<String>> members, HashMap<String, BookingDetails> nameToSessionAllBookees, int members_forename_col_index, int members_surname_col_index, List<List<String>> commonNameVariants) {
        //check if they member,
        //if not, check if their first name is in the common names list, and check for all variants of their name

        //for array in other names
        //  if forename in array:
        //      for name in array (that is not name):
        //          check if member


        //iterate through members
        for (List<String> entry : members) {

            //get the member's full name
            String members_forename = entry.get(members_forename_col_index).strip().toLowerCase();
            String members_surname = entry.get(members_surname_col_index).strip().toLowerCase();
            String fullname = (members_forename + " " + members_surname).strip();

            //check if that members is in the list of bookings, and if so, update that booking to be a member
            if (nameToSessionAllBookees.containsKey(fullname)) {
                nameToSessionAllBookees = updateBookingToBeAMember(fullname, nameToSessionAllBookees);
            }
            else{
                //if that member was not in the list
                //check if the member was in the list of bookings, but a different variant of that name was used

                //Check all list of variants to see if that member name has a variant
                for (List<String> nameVariants : commonNameVariants) {
                    for(String checkName : nameVariants){
                        checkName = checkName.toLowerCase().strip();

                        //if the name has a variant
                        if(checkName.equals(members_forename)){
                            System.out.println("variant for the forename!" + fullname);

                            //go and check each of its variants
                            for(String variationOfGivenForenameName : nameVariants){
                                if(variationOfGivenForenameName.equals(members_forename)) continue;

                                //check if the variant appears in the bookings (and update that booking to be a member if it does)
                                String variantFullname = (variationOfGivenForenameName.toLowerCase().strip() + " " + members_surname).strip();
                                if (nameToSessionAllBookees.containsKey(variantFullname)) {
                                    System.out.println(" :0 found variant! updating! "+ variantFullname);
                                    nameToSessionAllBookees = updateBookingToBeAMember(variantFullname, nameToSessionAllBookees);
                                    break;
                                }
                                else{
                                    System.out.println(" :( does not contain variant name; "+ variantFullname);
                                }
                            }
                        }
                    }
                }


                System.out.println("Not found: |" + fullname + "|!!!");
                for (String name : nameToSessionAllBookees.keySet()) {
                    System.out.println("|" + name + "| != |" + fullname + "|");
                }
            }
        }

        return nameToSessionAllBookees;
    }


    private HashMap<String, BookingDetails> updateBookingToBeAMember(String fullname, HashMap<String, BookingDetails> nameToSessionAllBookees)
    {
        System.out.println("The bookee: |"+ fullname+ "| has been found!!!");
        BookingDetails existingDetails = nameToSessionAllBookees.get(fullname);
        existingDetails.setMember(true);
        nameToSessionAllBookees.put(fullname, existingDetails);
        return nameToSessionAllBookees;
    }

    public static void writeToTextFile(HashMap<String, ArrayList<BookingDetails>> sessionToBookees) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("out/output_files/out.txt"));

        for(String session : sessionToBookees.keySet()){
            ArrayList<BookingDetails> bookeesForThatSession = sessionToBookees.get(session);

            pw.write("For "+session+":\n");

            for(int i = 0; i < bookeesForThatSession.size(); i++){
                BookingDetails bookingDetails = bookeesForThatSession.get(i);


                StringBuilder outEntryText = new StringBuilder("(");
                if(i < 9) outEntryText.append("0");
                outEntryText.append(i + 1).append(") ");
                outEntryText.append(bookingDetails.getName());
                outEntryText.append(" ".repeat(Math.max(0, 25 - bookingDetails.getName().length())));
                outEntryText.append(" | ").append(bookingDetails.isMember());
                outEntryText.append("\n");
                pw.write(outEntryText.toString());
            }
            pw.write("\n");
        }

        pw.close();
    }


}
