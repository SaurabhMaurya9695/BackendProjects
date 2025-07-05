package com.backend.design.pattern.behavioural.chainOfResponsibility;

public abstract class Approver {

    public Approver _approver;

    public void setApprover(Approver approver) {
        this._approver = approver;
    }

    public abstract void approveRequest(int days);
}
