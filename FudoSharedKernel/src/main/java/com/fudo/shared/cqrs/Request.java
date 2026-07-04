package com.fudo.shared.cqrs;

/**
 * Equivalent of MediatR's {@code IRequest<TResponse>} — used uniformly for both
 * commands and queries, matching how the original .NET handlers were modeled.
 */
public interface Request<R> {
}
