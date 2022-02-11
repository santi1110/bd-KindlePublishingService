//package com.amazon.ata.kindlepublishingservice.metrics;
//
//import com.amazon.coralx.threadlocal.ThreadLocalContext;
//
//import javax.measure.Unit;
//
///**
// * Class that includes methods for publishing metrics.
// */
//public class MetricsPublisher {
//
//    /**
//     * Publish count metric.
//     *
//     * @param name of count metric.
//     * @param value of count metric.
//     * @param unit of count metric.
//     */
//    public void addCount(String name, double value, Unit<?> unit) {
//        ThreadLocalContext.get().get().getMetrics().addCount(name, value, unit);
//    }
//
//    /**
//     * Publish time metric.
//     *
//     * @param name of time metric.
//     * @param value of time metric.
//     * @param unit of time metric.
//     */
//    public void addTime(String name, double value, Unit<?> unit) {
//        ThreadLocalContext.get().get().getMetrics().addTime(name, value, unit);
//    }
//}
