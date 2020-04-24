package datadog.opentracing.resolver;

import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import datadog.trace.core.DDTracer;
import datadog.trace.api.Config;
import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(TracerResolver.class)
public class DDTracerResolver extends TracerResolver {

  @VisibleForTesting
  Tracer resolve(final Config config) {
    if (config.isTraceResolverEnabled()) {
      log.info("Creating DDTracer with DDTracerResolver");
      return DDTracer.builder().config(config).build();
    } else {
      log.info("DDTracerResolver disabled");
      return null;
    }
  }

  @Override
  protected Tracer resolve() {
    return resolve(Config.get());
  }
}
