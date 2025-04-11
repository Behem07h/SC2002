package org.action.registration;

public class RegistrationCriteria {

    private boolean noIntention;
    private boolean notHDBofficer;

    public RegistrationCriteria(boolean noIntention, boolean notHDBofficer){
        this.noIntention = noIntention;
        this.notHDBofficer = notHDBofficer;
    }

    public boolean noIntention(){
        return noIntention;
    }

    public boolean notHDBofficer(){
        return notHDBofficer;
    }
}
