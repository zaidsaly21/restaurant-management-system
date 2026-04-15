package solid.isp;

public final class PaymentResult {
    private final boolean success;
    private final String message;

    private PaymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static PaymentResult ok() {
        return new PaymentResult(true, "OK");
    }

    public static PaymentResult fail(String msg) {
        return new PaymentResult(false, (msg == null || msg.isBlank()) ? "Failed" : msg);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}