package com.fudo.shared.cqrs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Minimal in-process mediator that plays the role MediatR played in the .NET
 * services: a controller calls {@code mediator.send(new SomeCommand(...))} and
 * this resolves the single {@link RequestHandler} bean registered for that
 * request's concrete class.
 */
@Component
public class SimpleMediator implements Mediator, InitializingBean {

    private final ApplicationContext applicationContext;
    private final Map<Class<?>, RequestHandler<?, ?>> handlersByRequestType = new HashMap<>();

    public SimpleMediator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        applicationContext.getBeansOfType(RequestHandler.class).values().forEach(handler -> {
            Class<?> requestClass = ResolvableType.forClass(RequestHandler.class, handler.getClass())
                    .getGeneric(0)
                    .resolve();
            if (requestClass == null) {
                throw new IllegalStateException(
                        "Unable to resolve request type for handler " + handler.getClass().getName());
            }
            RequestHandler<?, ?> existing = handlersByRequestType.put(requestClass, handler);
            if (existing != null) {
                throw new IllegalStateException("Duplicate handler registered for request type "
                        + requestClass.getName() + ": " + existing.getClass().getName()
                        + " and " + handler.getClass().getName());
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(Request<R> request) {
        RequestHandler<Request<R>, R> handler =
                (RequestHandler<Request<R>, R>) handlersByRequestType.get(request.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for request type " + request.getClass().getName());
        }
        return handler.handle(request);
    }
}
