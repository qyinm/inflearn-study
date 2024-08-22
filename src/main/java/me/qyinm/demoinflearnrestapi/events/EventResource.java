package me.qyinm.demoinflearnrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
// Spring Hateoas Update ResourceSupport -> RepresentationModel
// ObjectMapper -> BeanSerializer
//public class EventResource extends RepresentationModel<EventResource> {
//    //event : {} unwrapping
//    @JsonUnwrapped
//    private Event event;
//
//    public EventResource(Event event) {
//        this.event = event;
//    }
//
//    public Event getEvent() {
//        return event;
//    }
//}

public class EventResource extends EntityModel<Event> {
    public EventResource(Event event, Link... links) {
        super(content, List.of(links));

        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}