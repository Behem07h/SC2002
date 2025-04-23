/**
 * Manages the bookings for HDB flat applications.

 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action;

/**
 * This class tracks all bookings (both confirmed and unconfirmed) and provides
 * methods to add and retrieve bookings.
 */
public class WithdrawalRequest{
    /**
     * Unique ID for the HDB flat application.
     */
    private final String applicationId;

    /**
     * Boolean Indicator whether a withdrawal has been requested.
     */
    private boolean isRequested = false;

    /**
     * Current status of the officer's approval for the withdrawal request.
     * Default value is PENDING.
     */
    private OfficerApprovalStatus approvalStatus = OfficerApprovalStatus.PENDING;

    /**
     * Enum representing possible approval statuses for withdrawal requests.
     */
    public enum OfficerApprovalStatus {
        /** Request is pending officer review */
        PENDING,
        /** Request has been approved by an officer */
        APPROVED,
        /** Request has been rejected by an officer */
        REJECTED
    }

    /**
     * Constructs a new withdrawal request for the specified application.
     *
     * @param applicationId the unique identifier of the application
     */
    public WithdrawalRequest(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Submits a withdrawal request for the application.
     * If a withdrawal has already been requested, the method will print a notification
     * and take no further action.
     */
    public void request() {
        if (!isRequested) {
            isRequested = true;
            approvalStatus = OfficerApprovalStatus.PENDING;
            System.out.println("Withdrawal requested for application ID: " + applicationId);
        } else {
            System.out.println("Withdrawal already requested for application ID: " + applicationId);
        }
    }

    /**
     * Approves a pending withdrawal request.
     * This method can only be called if a withdrawal has been requested and
     * the current status is PENDING. Otherwise, an error message is printed.
     */
    public void approve() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.APPROVED;
            System.out.println("Officer approved withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot approve: no pending request or already processed (status="
                               + approvalStatus + ").");
        }
    }


    /**
     * Rejects a pending withdrawal request.
     * This method can only be called if a withdrawal has been requested and
     * the current status is PENDING. Otherwise, an error message is printed.
     */
    public void reject() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.REJECTED;
            System.out.println("Officer rejected withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot reject: no pending request or already processed (status="
                               + approvalStatus + ").");
        }
    }
    /**
     * Returns whether a withdrawal has been requested for this application.
     *
     * @return true if a withdrawal has been requested, false otherwise
     */
    public boolean isRequested() {
        return isRequested;
    }

    /**
     * Returns the current approval status of the withdrawal request.
     *
     * @return the current Status of the request
     */
    public OfficerApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}






