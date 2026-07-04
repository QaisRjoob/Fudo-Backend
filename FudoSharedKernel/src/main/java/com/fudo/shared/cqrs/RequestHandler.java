package com.fudo.shared.cqrs;

/**
 * Equivalent of MediatR's {@code IRequestHandler<TRequest, TResponse>}.
 * Implementations are plain {@code @Component} beans; {@link SimpleMediator}
 * resolves the right one for a given request by its generic type argument.
 */
public interface RequestHandler<T extends Request<R>, R> {

    R handle(T request);
}
