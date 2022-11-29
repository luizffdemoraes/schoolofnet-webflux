package com.schoolofnet.webflux.controller;

import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping
    @ResponseBody
    public Publisher<String> sayHello() {
        return Mono.just("Hello World from Spring Webflux by School of net");
    }
}

