# Prompts Stage 2

Modelo usado en todos los prompts: `claude-sonnet-5-5` (Claude Sonnet 5.5, claude.ai).

| Number | Model | Exact prompt text | Files changed |
|---|---|---|---|
| 1 | claude-sonnet-5-5 | muy bien ahora empecemos con el paso 2  | LoanService.java, ReservationService.java, MemberService.java, BookController.java, LoanController.java, ReservationController.java, App.java, DueDateDuplicationTest.java, TransactionBugTest.java, AccruedFeeTest.java (nuevo), DatabaseSeedTest.java (nuevo), docs/class-diagram.puml, docs/prompts-stage-2.md |
| 2 | copilot | como puedes ver mi codigo tiene errores,quiero que los corrijas sin que cambie la funcionalidad,dime el tipo de error y como se arregla | NotFoundException.java  , LoanPolicy.java , LoanDao.java, ReservationDao.java, Loan.java , Database.java |
| 3 | copilot | agregale el javadocs a todo lo que falte por favor | - | 