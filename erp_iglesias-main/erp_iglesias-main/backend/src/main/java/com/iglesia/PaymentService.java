package com.iglesia;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final OfferingRepository offeringRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          EnrollmentRepository enrollmentRepository,
                          OfferingRepository offeringRepository) {
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.offeringRepository = offeringRepository;
    }

    public List<PaymentResponseDto> list(PaymentStatus status) {
        List<Payment> payments = status == null
                ? paymentRepository.findAll()
                : paymentRepository.findAllByStatus(status);

        return payments.stream().map(PaymentResponseDto::from).toList();
    }

    public PaymentResponseDto confirm(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        payment.setStatus(PaymentStatus.CONFIRMADO);
        paymentRepository.save(payment);

        if (payment.getType() == PaymentType.INSCRIPCION_CURSO) {
            Enrollment enrollment = enrollmentRepository.findById(payment.getReferenceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscripción no encontrada"));

            enrollment.setStatus(EnrollmentStatus.PAGADA);
            enrollmentRepository.save(enrollment);

        } else if (payment.getType() == PaymentType.OFRENDA) {
            Offering offering = offeringRepository.findById(payment.getReferenceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ofrenda no encontrada"));

            offering.setStatus(OfferingStatus.REGISTRADA);
            offeringRepository.save(offering);
        }

        return PaymentResponseDto.from(payment);
    }

    public PaymentResponseDto fail(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        if (payment.getStatus() == PaymentStatus.CONFIRMADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pago ya fue confirmado");
        }

        payment.setAttempts(payment.getAttempts() + 1);
        payment.setStatus(PaymentStatus.FALLIDO);
        paymentRepository.save(payment);

        return PaymentResponseDto.from(payment);
    }

    public PaymentResponseDto retry(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        if (payment.getStatus() != PaymentStatus.FALLIDO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se reintenta un pago fallido");
        }

        if (payment.getAttempts() >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se superó el máximo de reintentos");
        }

        payment.setStatus(PaymentStatus.INICIADO);
        paymentRepository.save(payment);

        return PaymentResponseDto.from(payment);
    }
}