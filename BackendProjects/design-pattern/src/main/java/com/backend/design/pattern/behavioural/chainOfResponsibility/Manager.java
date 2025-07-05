package com.backend.design.pattern.behavioural.chainOfResponsibility;

public class Manager extends Approver {

    @Override
    public void approveRequest(int days) {
        if (days <= 3) {
            System.out.println("Manager can approve your request , No need to go forward to take approval");
            return;
        } else if (super._approver != null) {
            System.out.println("Manager is not able to approve the request, so delegating req to his Manager");
            _approver.approveRequest(days);
        }
    }
}
