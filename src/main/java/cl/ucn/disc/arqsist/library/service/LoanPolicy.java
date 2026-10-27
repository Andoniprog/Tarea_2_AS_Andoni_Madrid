package cl.ucn.disc.arqsist.library.service;

import java.time.LocalDate;

/**
 * Defines the standard loan duration and daily overdue fee.
 */
public final class LoanPolicy {

    /** Monetary fee charged for each day past the due date. */
    public static final double FEE_PER_DAY = 1.0;

    /** Standard duration of a loan in days. */
    private static final long LOAN_PERIOD_DAYS = 14;

    /** Prevents instances of this policy utility class. */
    private LoanPolicy() {
    }

    /**
     * Calculates the due date for a loan.
     *
     * @param loanDate date on which the loan begins.
     * @return the date 14 days after the loan date.
     */
    public static LocalDate dueDate(LocalDate loanDate) {
        return loanDate.plusDays(LOAN_PERIOD_DAYS);
    }
}
