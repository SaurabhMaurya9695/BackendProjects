package com.backend.design.pattern.behavioural.chainOfResponsibility;

public class Director extends Approver {

    @Override
    public void approveRequest(int days) {
        if (days <= 10) {
            System.out.println("Director can approve your request , No need to go forward to take approval");
            return;
        } else if (super._approver != null) {
            System.out.println("Director is not able to approve the request, so delegating req to his Manager");
            _approver.approveRequest(days);
        } else {
            System.out.println("Leave Request denied , asking for so many days ");
        }
    }
}
