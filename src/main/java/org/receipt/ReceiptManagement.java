package org.receipt;


import java.util.ArrayList;
import java.util.List;


public class ReceiptManagement {
   private List<BookingReceipt> confirmedBookings;


   public ReceiptManagement() {
       confirmedBookings = new ArrayList<>();
   }


   public void confirmBooking(BookingReceipt booking) {
       booking.confirm();
       confirmedBookings.add(booking);
       System.out.println("Receipt generated:");
       booking.generateReceipt();
   }


   public List<BookingReceipt> getAllConfirmedBookings() {
       return confirmedBookings;
   }


   public BookingReceipt getReceiptByNRIC(String nric) {
       for (BookingReceipt booking : confirmedBookings) {
           if (booking.getApplicantNRIC().equalsIgnoreCase(nric)) {
               return booking;
           }
       }
       return null;
   }
}
