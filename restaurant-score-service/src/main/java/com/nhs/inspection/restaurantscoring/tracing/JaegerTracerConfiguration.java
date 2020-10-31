package com.nhs.inspection.restaurantscoring.tracing;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.reporters.InMemoryReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.spi.Sampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerTracerConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        final Reporter reporter = new InMemoryReporter();
        final Sampler sampler = new ConstSampler(false);
        return new JaegerTracer.Builder(applicationName)
                .withReporter(reporter)
                .withSampler(sampler)
                .build();
    }
}