package com.example.legal_meterology.certificate;
public class CertificateResult {

    private boolean success;
    private String message;

    private String certificateId;
    private String certificatePath;
    private String qrCodePath;

    public CertificateResult() {
    }

    public CertificateResult(
            boolean success,
            String message,
            String certificateId,
            String certificatePath,
            String qrCodePath) {

        this.success = success;
        this.message = message;
        this.certificateId = certificateId;
        this.certificatePath = certificatePath;
        this.qrCodePath = qrCodePath;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }
}