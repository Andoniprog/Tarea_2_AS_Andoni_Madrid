package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import io.javalin.config.JavalinConfig;

/**
 * Registers HTTP routes for member operations.
 */
public final class MemberController {

    /** Service that performs member operations. */
    private final MemberService service;

    /**
     * Creates a controller backed by the supplied member service.
     *
     * @param service service used by the member routes.
     */
    public MemberController(MemberService service) {
        this.service = service;
    }

    /**
     * Registers the member listing and registration routes.
     *
     * @param config Javalin configuration receiving the routes.
     */
    public void register(JavalinConfig config) {
        config.routes.get("/members", ctx -> ctx.json(service.findAll()));
        config.routes.post("/members", ctx -> ctx.json(service.register(ctx.bodyAsClass(Member.class))));
    }
}
