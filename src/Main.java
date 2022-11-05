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


    public static void main(String args[]){
        MemberFinder memberFinder = new MemberFinder();
        memberFinder.produceBookingsList();
    }
}
