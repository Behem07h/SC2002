/**
 * Manages all bookings, including both confirmed and unconfirmed ones.
 * @author Group 1
 * @version 1.0
 * @since 2025-04-23
 */
package org.receipt;

import java.util.ArrayList;
import java.util.List;

/**
 * This class maintains a collection of all bookings and provides methods to
 * add new bookings and retrieve existing bookings based on various criteria.
 */
public class BookingManagement {
    /**
     * List of all bookings, both confirmed and unconfirmed
     */
    private List <BookingReceipt> bookings;


    /**
     * Constructs a new BookingManagement instance.
     * Initializes an empty list to store all bookings.
     */
    public BookingManagement() {
        bookings = new ArrayList<>();
    }

    /**
     * Adds a new booking to the management system.
     * This method adds the specified booking to the list of all bookings
     * and prints a confirmation message.
     * @param booking the BookingReceipt to add
     */
    public void addBooking(BookingReceipt booking) {
        bookings.add(booking);
        System.out.println("Booking added successfully for " + booking.getApplicantName());
    }

    /**
     * Returns all bookings in the system.
     *
     * @return a list containing all BookingReceipt objects
     */
    public List <BookingReceipt> getAllBookings() {
        return bookings;
    }

    /**
     * Finds and returns a booking matching the specified NRIC.
     *
     * @param nric the NRIC to search for
     * @return the BookingReceipt with the matching NRIC, or null if not found
     */
    public BookingReceipt getBookingByNRIC(String nric) {
        for (BookingReceipt booking : bookings) {
            if (booking.getApplicantNRIC().equalsIgnoreCase(nric)) {
                return booking;
            }
        }
        return null;
    }
}