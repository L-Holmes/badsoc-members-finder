public class BookingDetails {
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
