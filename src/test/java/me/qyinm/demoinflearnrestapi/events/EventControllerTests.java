package me.qyinm.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.qyinm.demoinflearnrestapi.common.RestDocsConfiguration;
import me.qyinm.demoinflearnrestapi.common.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private Environment environment;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 8, 12, 8, 1))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 8, 13, 8, 1))
                .beginEventDateTime(LocalDateTime.of(2024, 8, 14, 8, 1))
                .endEventDateTime(LocalDateTime.of(2024, 8, 15, 8, 1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 2번출구")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        // Prefix Relaxed will not fully describe e.g. relaxedResponseFields
                        // if you want to use responseFields, use with 'subsectionWithPath' that seems to document their subsections
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
//                                subsectionWithPath("_links").description("it's links complying HATEOAS SPEC")
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우 에러가 발생하는 테스트")
    void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 8, 12, 8, 1))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 8, 13, 8, 1))
                .beginEventDateTime(LocalDateTime.of(2024, 8, 14, 8, 1))
                .endEventDateTime(LocalDateTime.of(2024, 8, 15, 8, 1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 2번출구")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    // @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트") it is working only with JUnit 4, use @DisplayName instead in JUnit 5
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 8, 12, 8, 1))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 8, 11, 8, 1))
                .beginEventDateTime(LocalDateTime.of(2024, 8, 14, 8, 1))
                .endEventDateTime(LocalDateTime.of(2024, 8, 13, 8, 1))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 2번출구")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());

    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When & Then
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events",
                        links(halLinks(),
                                linkWithRel("first").description("link to first page"),
                                linkWithRel("prev").description("link to prev page"),
                                linkWithRel("self").description("link to self page"),
                                linkWithRel("next").description("link to next page"),
                                linkWithRel("last").description("link to last page"),
                                linkWithRel("profile").description("link to profile page")
                        ),
                        queryParameters(
                                parameterWithName("page").description("page number"),
                                parameterWithName("size").description("size of content"),
                                parameterWithName("sort").description("sort of content")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("page.size").description("size of page"),
                                fieldWithPath("page.totalElements").description("total elements of page"),
                                fieldWithPath("page.totalPages").description("total pages of this query"),
                                fieldWithPath("page.number").description("number of this page"),
                                fieldWithPath("_embedded.eventList").description("content of event list in this page"),
                                fieldWithPath("_embedded.eventList[].id").description("id of new event"),
                                fieldWithPath("_embedded.eventList[].name").description("Name of new event"),
                                fieldWithPath("_embedded.eventList[].description").description("description of new event"),
                                fieldWithPath("_embedded.eventList[].beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("_embedded.eventList[].closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("_embedded.eventList[].beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("_embedded.eventList[].endEventDateTime").description("date time of close of new event"),
                                fieldWithPath("_embedded.eventList[].location").description("location of new event"),
                                fieldWithPath("_embedded.eventList[].basePrice").description("basePrice of new event"),
                                fieldWithPath("_embedded.eventList[].maxPrice").description("maxPrice of new event"),
                                fieldWithPath("_embedded.eventList[].limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("_embedded.eventList[].free").description("it tells if this event is free or not"),
                                fieldWithPath("_embedded.eventList[].offline").description("it tells if this event is offline or not"),
                                fieldWithPath("_embedded.eventList[].eventStatus").description("event status"),
                                fieldWithPath("_embedded.eventList[]._links.self.href").description("link to event self"),
                                fieldWithPath("_links.first.href").description("link to page first"),
                                fieldWithPath("_links.prev.href").description("link to page first"),
                                fieldWithPath("_links.first.href").description("link to page first"),
                                fieldWithPath("_links.prev.href").description("link to page prev"),
                                fieldWithPath("_links.self.href").description("link to page self"),
                                fieldWithPath("_links.next.href").description("link to page next"),
                                fieldWithPath("_links.last.href").description("link to page last"),
                                fieldWithPath("_links.profile.href").description("link to page profile")
                        )
                ));
    }

    @Test
    @DisplayName("기존의 이벤트 하나 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event",
                        links(halLinks(),
                                linkWithRel("self").description("self link"),
                                linkWithRel("profile").description("profile link")
                        ),
                        pathParameters(
                                parameterWithName("id").description("id of event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @Test
    @DisplayName("없는 이벤트는 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/11111"))
                .andExpect(status().isNotFound());
    }
    /* My Code
    @Test
    @DisplayName("이벤트 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = generateEvent(1);
        EventDto eventDto = event.ToEventDto();
        String updateName = "update name";
        eventDto.setName(updateName);
        // When & Then
        this.mockMvc.perform(patch("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(updateName))
                .andExpect(jsonPath("_links").exists());
    }

    @Test
    @DisplayName("없는 이벤트 수정하기")
    public void updateEvent_NotFound() throws Exception {
        // Given
        Event event = generateEvent(1);
        EventDto eventDto = event.ToEventDto();
        String updateName = "update name";
        eventDto.setName(updateName);
        // When & Then
        this.mockMvc.perform(patch("/api/events/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("입력받을 수 없는 값 이벤트 수정하기")
    public void updateEvent_Wrong_Input() throws Exception {
        // Given
        Event event = generateEvent(1);
        EventDto eventDto = event.ToEventDto();
        eventDto.setBasePrice(99999999);
        eventDto.setMaxPrice(1);
        // When & Then
        this.mockMvc.perform(patch("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값 NULL 이벤트 수정하기")
    public void updateEvent_NULL_Input() throws Exception {
        // Given
        Event event = generateEvent(1);
        EventDto eventDto = event.ToEventDto();
        eventDto.setName(null);
        // When & Then
        this.mockMvc.perform(patch("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
     */
    // whiteship code
    @Test
    @DisplayName("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Name";
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event",
                        links(halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("id").description("updating event id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of updated event"),
                                fieldWithPath("description").description("description of updated event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of updated event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of updated event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of updated event"),
                                fieldWithPath("endEventDateTime").description("date time of close of updated event"),
                                fieldWithPath("location").description("location of updated event"),
                                fieldWithPath("basePrice").description("basePrice of updated event"),
                                fieldWithPath("maxPrice").description("maxPrice of updated event"),
                                fieldWithPath("limitOfEnrollment").description("limit of updated event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("event id"),
                                fieldWithPath("name").description("Name of updated event"),
                                fieldWithPath("description").description("description of updated event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of updated event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of updated event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of updated event"),
                                fieldWithPath("endEventDateTime").description("date time of close of updated event"),
                                fieldWithPath("location").description("location of updated event"),
                                fieldWithPath("basePrice").description("basePrice of updated event"),
                                fieldWithPath("maxPrice").description("maxPrice of updated event"),
                                fieldWithPath("limitOfEnrollment").description("limit of updated event"),
                                fieldWithPath("offline").description("updated event offline"),
                                fieldWithPath("free").description("updated event free"),
                                fieldWithPath("eventStatus").description("updated event status"),
                                fieldWithPath("_links.self.href").description("self link of updated event"),
                                fieldWithPath("_links.profile.href").description("profile link of updated event")
                        )
                ))
        ;

    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);

        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 이벤트 수정 실패")
    public void updateEvent400Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(9999);
        eventDto.setMaxPrice(10);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 8, 12, 8, 1))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 8, 13, 8, 1))
                .beginEventDateTime(LocalDateTime.of(2024, 8, 14, 8, 1))
                .endEventDateTime(LocalDateTime.of(2024, 8, 15, 8, 1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 2번출구")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }
}