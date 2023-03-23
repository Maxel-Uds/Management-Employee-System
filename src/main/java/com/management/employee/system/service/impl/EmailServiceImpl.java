package com.management.employee.system.service.impl;

import com.management.employee.system.model.Company;
import com.management.employee.system.model.ModelAndView;
import com.management.employee.system.model.Owner;
import com.management.employee.system.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.HashMap;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final String from;
    private final SesClient client;
    private final ITemplateEngine engine;
    private final String welcomeOwnerSubject;

    @Autowired
    public EmailServiceImpl(ITemplateEngine engine, SesClient client,
                        @Value("${aws.ses.from}") String from,
                        @Value("${aws.ses.welcome-owner.subject}") String welcomeOwnerSubject) {
        this.from = from;
        this.engine = engine;
        this.client = client;
        this.welcomeOwnerSubject = welcomeOwnerSubject;
    }

    @Override
    public Mono<Void> sendWelcomeMailToOwner(Owner owner, Company company) {
        ModelAndView resp = ModelAndView.builder()
                .view("welcomeOwner")
                .model(new HashMap<>() {{
                    put("name", owner.getName());
                    put("username", owner.getUsername());
                    put("password", owner.getPassword());
                    put("companyName", company.getName());
                }})
                .build();

        Context context = new Context(resp.getLocale(), resp.getModel());
        String html = engine.process(resp.getView(), context);

        log.info("==== Sending Welcome Owner Email to: [{}] ====", owner.getEmail());
        return this.sendEmailAsHtml(owner.getEmail(), html, welcomeOwnerSubject);
    }

    private Mono<Void> sendEmailAsHtml(String to, String htmlMessage, String subject) {
        var body = Body.builder().html(content(htmlMessage)).build();
        var message = Message.builder()
                .subject(content(subject))
                .body(body)
                .build();

        var request = SendEmailRequest.builder().source(this.from)
                .destination(Destination.builder().toAddresses(to).build())
                .message(message)
                .build();

        return Mono.just(client.sendEmail(request)).then();
    }

    private Content content(String data) {
        return Content.builder().charset("UTF-8").data(data).build();
    }
}
