package com.backend.design.pattern.behavioural.chainOfResponsibility;

public class SeniorManager extends Approver {

    @Override
    public void approveRequest(int days) {
        if (days <= 7) {
            System.out.println("SeniorManager can approve your request , No need to go forward to take approval");
            return;
        } else if (super._approver != null) {
            System.out.println("SeniorManager is not able to approve the request, so delegating req to his Manager");
            _approver.approveRequest(days);
        }
    }
}
