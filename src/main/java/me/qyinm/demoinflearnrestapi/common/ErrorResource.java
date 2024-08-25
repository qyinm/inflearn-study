package me.qyinm.demoinflearnrestapi.common;

import me.qyinm.demoinflearnrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends EntityModel<Errors> {
    public ErrorResource(Errors content, Link... links) {
        super(content, List.of(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
