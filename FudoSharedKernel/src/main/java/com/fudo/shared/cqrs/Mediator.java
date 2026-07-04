package com.fudo.shared.cqrs;

public interface Mediator {

    <R> R send(Request<R> request);
}
