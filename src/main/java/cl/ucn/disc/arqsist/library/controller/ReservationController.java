package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.service.ReservationService;
import io.javalin.config.JavalinConfig;


/**
 * Registers HTTP routes for reservation operations.
 */
public final class ReservationController {

    /** Service that performs reservation operations. */
    private final ReservationService service;

    /**
     * Creates a controller backed by the supplied reservation service.
     *
     * @param service service used by the reservation routes.
     */
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Registers the reservation listing, creation, and fulfillment routes.
     *
     * @param config Javalin configuration receiving the routes.
     */
    public void register(JavalinConfig config) {
        config.routes.post("/reservations", ctx -> {
            int memberId = Integer.parseInt(ctx.queryParam("memberId"));
            int bookId = Integer.parseInt(ctx.queryParam("bookId"));
            ctx.json(service.reserve(bookId, memberId));
        });
        config.routes.get("/reservations", ctx -> ctx.json(service.findAll()));
        config.routes.post("/reservations/{id}/fulfill", ctx -> ctx.json(service.fulfill(Integer.parseInt(ctx.pathParam("id")))));
    }
}
