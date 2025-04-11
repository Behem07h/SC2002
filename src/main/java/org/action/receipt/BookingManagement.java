package org.action.receipt;


import java.util.ArrayList;
import java.util.List;


public class BookingManagement {
   private List <BookingReceipt> bookings;


   public BookingManagement() {
       bookings = new ArrayList<>();
   }   


   public void addBooking(BookingReceipt booking) {
       bookings.add(booking);
       System.out.println("Booking added successfully for " + booking.getApplicantName());
   }


   public List <BookingReceipt> getAllBookings() {
       return bookings;
   }


   public BookingReceipt getBookingByNRIC(String nric) {
       for (BookingReceipt booking : bookings) {
           if (booking.getApplicantNRIC().equalsIgnoreCase(nric)) {
               return booking;
           }
       }
       return null;
   }
}
