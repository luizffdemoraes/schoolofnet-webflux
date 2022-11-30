package com.schoolofnet.webflux.controller;

import com.schoolofnet.webflux.model.Todo;
import com.schoolofnet.webflux.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier("jdbcSchedule")
    private Scheduler jdbcScheduler;

    @GetMapping("/{id}")
    @ResponseBody
    public Mono<Optional<Todo>> findById(@PathVariable("id") Long id) {
        return Mono.just(this.todoRepository.findById(id));
    }

    @GetMapping
    @ResponseBody
    public Flux<Todo> findAll() {
        return Flux.defer(() -> Flux.fromIterable(this.todoRepository.findAll()).subscribeOn(jdbcScheduler));
    }

    @PostMapping
    public Mono<Todo> save(@RequestBody Todo todo) {
        return Mono.fromCallable(() -> this.transactionTemplate.execute(action -> {
            Todo newTodo = this.todoRepository.save(todo);
            return newTodo;
        }));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") Long id) {
        return Mono.fromCallable(() -> this.transactionTemplate.execute(action -> {
            this.todoRepository.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        })).subscribeOn(jdbcScheduler);
    }
}
