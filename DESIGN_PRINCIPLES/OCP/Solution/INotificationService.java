package DESIGN_PRINCIPLES.OCP.Problem;

public interface INotificationService {

    public void sendOtp(String medium);
    public void sendTransationReport(String medium);

}
