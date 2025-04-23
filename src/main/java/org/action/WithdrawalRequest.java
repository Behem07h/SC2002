/**
 * Manages the bookings for HDB flat applications.
 * This class tracks all bookings (both confirmed and unconfirmed) and provides
 * methods to add and retrieve bookings.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action;

public class WithdrawalRequest{
    private final String applicationId;
    private boolean isRequested = false;
    private OfficerApprovalStatus approvalStatus = OfficerApprovalStatus.PENDING;

    public enum OfficerApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public WithdrawalRequest(String applicationId) {
        this.applicationId = applicationId;
    }

    public void request() {
        if (!isRequested) {
            isRequested = true;
            approvalStatus = OfficerApprovalStatus.PENDING;
            System.out.println("Withdrawal requested for application ID: " + applicationId);
        } else {
            System.out.println("Withdrawal already requested for application ID: " + applicationId);
        }
    }

    public void approve() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.APPROVED;
            System.out.println("Officer approved withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot approve: no pending request or already processed (status="
                               + approvalStatus + ").");
        }
    }

    public void reject() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.REJECTED;
            System.out.println("Officer rejected withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot reject: no pending request or already processed (status="
                               + approvalStatus + ").");
        }
    }

    public boolean isRequested() {
        return isRequested;
    }

    public OfficerApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}






