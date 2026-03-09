package com.iglesia;

public record PaymentResponseDto(
        Long id,
        String type,
        String status,
        String amount,
        int attempts,
        Long referenceId
) {
    public static PaymentResponseDto from(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getType().name(),
                payment.getStatus().name(),
                payment.getAmount().toPlainString(),
                payment.getAttempts(),
                payment.getReferenceId()
        );
    }
}