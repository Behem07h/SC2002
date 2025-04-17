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

    // Proper constructor
    public WithdrawalRequest(String applicationId) {
        this.applicationId = applicationId;
    }

    /** Applicant calls this to initiate a withdrawal. */
    public void request() {
        if (!isRequested) {
            isRequested = true;
            approvalStatus = OfficerApprovalStatus.PENDING;
            System.out.println("Withdrawal requested for application ID: " + applicationId);
        } else {
            System.out.println("Withdrawal request has already been made for application ID: " + applicationId);
        }
    }

    /** Officer calls this to approve a pending withdrawal. */
    public void approve() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.APPROVED;
            System.out.println("Officer approved withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot approve: either no request or already processed (status=" + approvalStatus + ").");
        }
    }

    /** Officer calls this to reject a pending withdrawal. */
    public void reject() {
        if (isRequested && approvalStatus == OfficerApprovalStatus.PENDING) {
            approvalStatus = OfficerApprovalStatus.REJECTED;
            System.out.println("Officer rejected withdrawal for application ID: " + applicationId);
        } else {
            System.out.println("Cannot reject: either no request or already processed (status=" + approvalStatus + ").");
        }
    }

    public boolean isRequested() {
        return isRequested;
    }

    public OfficerApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}


