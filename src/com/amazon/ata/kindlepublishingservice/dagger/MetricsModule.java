//package com.amazon.ata.kindlepublishingservice.dagger;
//
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsConstants;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
//
//import amazon.platform.config.AppConfigTree;
//import com.amazon.aws.cloudwatch.reporter.CloudWatchReporterFactory;
//import com.amazon.aws.cloudwatch.reporter.DimensionNameFilter;
//import com.amazon.aws.cloudwatch.reporter.MetricNameFilter;
//import com.amazon.aws.cloudwatch.reporter.ReporterFilterChain;
//import com.amazon.coral.metrics.MetricsFactory;
//import com.amazon.coral.metrics.helper.MetricsHelper;
//import com.amazon.coral.metrics.helper.QuerylogHelper;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
//import com.google.common.collect.ImmutableList;
//import dagger.Module;
//import dagger.Provides;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//import javax.inject.Singleton;
//
///**
// * Module for metric publishing.
// */
//@Module(
//    includes = {
//        AppConfigModule.class,
//    }
//)
//final class MetricsModule {
//
//    // Whitelist of metric names. When adding a new custom metric, its name must be included here in order for it to
//    // appear in CloudWatch.
//    private static final Set<String> metricNames = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
//        // Top Level Metrics
//        "Time",
//        // ServiceMetrics Group Metrics
//        "Fault",
//        "Error",
//        "Failure",
//        "Remote",
//        "TransmuterTime",
//        "OutstandingRequests",
//        "Timeout:Critical",
//        // JMX Metrics
//        "FileDescriptorUse",
//        "Threads",
//        "HeapMemoryUse",
//        "HeapMemoryAfterGCUse",
//        "NonHeapMemoryUse",
//        "GarbageCollection",
//        // client metrics
//        MetricsConstants.GET_BOOK_RECOMMENDATIONS_TIME,
//        MetricsConstants.BOOK_RECOMMENDATIONS_CACHE_QUERY_COUNT,
//        MetricsConstants.GET_BOOK_RECOMMENDATIONS_COUNT
//    )));
//
//    private static final Logger log = LogManager.getLogger(MetricsModule.class);
//
//    private static final Set<String> dimensionNames = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
//        "ServiceName",
//        "Program",
//        "Service",
//        "Operation")));
//
//    @Provides
//    @Singleton
//    static AmazonCloudWatchAsyncClient provideAmazonCloudWatch(AppConfigTree appConfig) {
//        final AmazonCloudWatchAsyncClient cloudwatch = new AmazonCloudWatchAsyncClient();
//        final Region region;
//        Region ec2Region = Regions.getCurrentRegion();
//        if (ec2Region == null) {
//            // In ECS containers, AWS_REGION is set
//            String envRegion = System.getenv("AWS_REGION");
//            if (envRegion != null) {
//                ec2Region = Region.getRegion(Regions.fromName(envRegion));
//            }
//        }
//        if (ec2Region == null) {
//            if (!"test".equals(appConfig.getDomain())) {
//                throw new RuntimeException("Couldn't identify region for CloudWatch logging from EC2");
//            } else {
//                log.warn("Could not identify the region we are running in. " +
//                    "Domain is test; using us-west-2");
//                region = Region.getRegion(Regions.US_WEST_2);
//            }
//        } else {
//            region = ec2Region;
//        }
//        cloudwatch.setRegion(region);
//
//        return cloudwatch;
//    }
//
//    @Provides
//    @Singleton
//    static MetricsFactory provideMetricsFactory(AmazonCloudWatchAsyncClient cloudwatch, AppConfigTree appConfig) {
//        MetricsHelper metricsFactory = new MetricsHelper();
//        CloudWatchReporterFactory cloudWatchReporterFactory = new CloudWatchReporterFactory();
//        cloudWatchReporterFactory.setCloudWatchAsyncClient(cloudwatch);
//        cloudWatchReporterFactory.setNamespace("atacurriculumkindlepublishingservice");
//        cloudWatchReporterFactory.setReporterFilter(getReporterFilterChain());
//
//        QuerylogHelper queryLogHelper = new QuerylogHelper();
//        queryLogHelper.setFilename(System.getenv("ENVROOT") + "/var/output/logs/service_log");
//        metricsFactory.setReporters(ImmutableList.of(
//            queryLogHelper,
//            cloudWatchReporterFactory));
//        metricsFactory.setProgram("atacurriculumkindlepublishingservice");
//        metricsFactory.setMarketplace(String.format("atacurriculumkindlepublishingservice:%s:%s", appConfig.getDomain(),
//            appConfig.getRealm().toString()));
//        return metricsFactory;
//    }
//
//    @Provides
//    @Singleton
//    static MetricsPublisher provideMetricsPublisher() {
//        return new MetricsPublisher();
//    }
//
//    private static ReporterFilterChain getReporterFilterChain() {
//        final ReporterFilterChain chain = new ReporterFilterChain();
//
//        DimensionNameFilter dimensionNameFilter = new DimensionNameFilter();
//        dimensionNameFilter.setDimensionNames(dimensionNames);
//
//        MetricNameFilter metricNameFilter = new MetricNameFilter();
//        metricNameFilter.setMetricNames(metricNames);
//
//        chain.setFilters(com.google.common.collect.ImmutableList.of(dimensionNameFilter, metricNameFilter));
//
//        return chain;
//    }
//
//}
