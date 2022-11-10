public class Main {
    /*
    ------------------------------------------------------
    Location of output:
    out/output_files/out.txt
    ------------------------------------------------------
    Format for additional members:
    <forename,surname,
     forename_2,surname_2, etc. >

     if no surname:
     <forename,,
      forename_2,surname_2, etc.>
    ------------------------------------------------------
     Format for input col info:
     <column id name>, <index of that column- starts from 0>
    ------------------------------------------------------
     */

    /*
    TODO:
    * handle people shortening their names. e.g. Dan -> Daniel (and vice versa)
     */
    public static void main(String[] args){

        FileChooser fileChooser = new FileChooser();
        /*
        String bookingsCSVPath = fileChooser.chooseCSVFile().getPath();
        String membersCSVPath = fileChooser.chooseCSVFile().getPath();
        String additionalMembersCSVPath =  fileChooser.chooseCSVFile().getPath();
         */

        //System.out.println("got a bookings path of: "+ bookingsCSVPath);

        //new BadSocInfoGetter(bookingsCSVPath, membersCSVPath, additionalMembersCSVPath).getAllInfo();
        new BadSocInfoGetter().getAllInfo();
    }
}
