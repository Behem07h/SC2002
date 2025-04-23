/**
 * Manages confirmed bookings and their associated receipts.
 * @author Group 1
 * @version 1.0
 * @since 2025-04-23
 */
package org.receipt;


import java.util.ArrayList;
import java.util.List;

/**
 * This class maintains a collection of confirmed bookings, provides methods to
 * confirm new bookings, generate receipts, and retrieve bookings based on various criteria.
 */
public class ReceiptManagement {
    /**
     * List of all bookings that have been confirmed
     */
   private List<BookingReceipt> confirmedBookings;

    /**
     * Constructs a new ReceiptManagement instance.
     * Initializes an empty list to store confirmed bookings.
     */
   public ReceiptManagement() {
       confirmedBookings = new ArrayList<>();
   }

    /**
     * Confirms a booking and generates a receipt.
     * This method marks the booking as confirmed, adds it to the list of confirmed bookings,
     * and generates a receipt for the booking.
     *
     * @param booking the BookingReceipt to confirm
     */
   public void confirmBooking(BookingReceipt booking) {
       booking.confirm();
       confirmedBookings.add(booking);
       System.out.println("Receipt generated:");
       booking.generateReceipt();
   }

    /**
     * Returns all confirmed bookings.
     *
     * @return a list containing all confirmed {@link BookingReceipt} objects
     */
   public List<BookingReceipt> getAllConfirmedBookings() {
       return confirmedBookings;
   }

    /**
     * Finds and returns a booking receipt matching the specified NRIC.
     *
     * @param nric the NRIC to search for
     * @return booking with the matching NRIC, or null if not found
     */
   public BookingReceipt getReceiptByNRIC(String nric) {
       for (BookingReceipt booking : confirmedBookings) {
           if (booking.getApplicantNRIC().equalsIgnoreCase(nric)) {
               return booking;
           }
       }
       return null;
   }
}
