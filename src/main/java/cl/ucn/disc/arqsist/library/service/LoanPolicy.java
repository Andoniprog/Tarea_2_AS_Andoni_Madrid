package cl.ucn.disc.arqsist.library.service;

import java.time.LocalDate;

public final class LoanPolicy {

    public static final double FEE_PER_DAY = 1.0;
    private static final long LOAN_PERIOD_DAYS = 14;

    private LoanPolicy() {
    }

    public static LocalDate dueDate(LocalDate loanDate) {
        return loanDate.plusDays(LOAN_PERIOD_DAYS);
    }
}
