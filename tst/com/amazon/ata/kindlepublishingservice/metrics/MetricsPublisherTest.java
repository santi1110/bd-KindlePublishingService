//package com.amazon.ata.kindlepublishingservice.metrics;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//import com.amazon.coral.metrics.Metrics;
//import com.amazon.coral.service.Context;
//import com.amazon.coralx.threadlocal.ThreadLocalContext;
//import javax.measure.unit.SI;
//import javax.measure.unit.Unit;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//public class MetricsPublisherTest {
//
//    @Mock
//    Metrics metrics;
//
//    @Mock
//    Context context;
//
//    @InjectMocks
//    private MetricsPublisher metricsPublisher;
//
//    @BeforeEach
//    public void setup() {
//        initMocks(this);
//    }
//
//    @Test
//    public void addCount_validRequest_publishesMetric() {
//
//        // GIVEN
//        ThreadLocalContext.get().set(context);
//        when(context.getMetrics()).thenReturn(metrics);
//        doNothing().when(metrics).addCount(any(String.class), any(double.class), any(Unit.class));
//
//        // WHEN
//        metricsPublisher.addCount("TestMetric", 1, Unit.ONE);
//
//        // THEN
//        verify(metrics).addCount("TestMetric", 1, Unit.ONE);
//
//    }
//
//    @Test
//    public void addTime_validRequest_publishesMetric() {
//
//        // GIVEN
//        ThreadLocalContext.get().set(context);
//        when(context.getMetrics()).thenReturn(metrics);
//        doNothing().when(metrics).addTime(any(String.class), any(double.class), any(Unit.class));
//
//        // WHEN
//        metricsPublisher.addTime("TestMetric", 123.0, SI.MILLI(SI.SECOND));
//
//        // THEN
//        verify(metrics).addTime("TestMetric", 123.0, SI.MILLI(SI.SECOND));
//    }
//}
