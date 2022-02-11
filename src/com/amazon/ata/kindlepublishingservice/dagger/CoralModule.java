//package com.amazon.ata.kindlepublishingservice.dagger;
//
//import com.amazon.ata.kindlepublishingservice.health.ShallowHealthCheck;
//import com.amazon.ata.test.tct.instrospection.ExecuteTctSuiteHelper;
//
//import com.amazon.coral.metrics.MetricsFactory;
//import com.amazon.coral.service.ChainComponent;
//import com.amazon.coral.service.HttpHandler;
//import com.amazon.coral.service.HttpRpcHandler;
//import com.amazon.coral.service.Log4jAwareRequestIdHandler;
//import com.amazon.coral.service.Orchestrator;
//import com.amazon.coral.service.PingHandler;
//import com.amazon.coral.service.ServiceHandler;
//import com.amazon.coral.service.bobcat.BobcatEndpointConfig;
//import com.amazon.coral.service.bobcat.BobcatServer;
//import com.amazon.coral.service.helper.ChainHelper;
//import com.amazon.coral.service.helper.OrchestratorHelper;
//import com.amazon.coral.service.http.ContentHandler;
//import com.amazon.coral.service.http.CrossOriginHandler;
//import dagger.Module;
//import dagger.Provides;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.inject.Singleton;
//
///**
// * Configures and provides classes for injection.
// */
//@Module(
//    includes = {
//        AppConfigModule.class,
//        MetricsModule.class,
//        InterceptorModule.class,
//        DataAccessModule.class,
//        PublishingModule.class,
//        ClientsModule.class
//    }
//)
//abstract class CoralModule {
//    private static final Logger log = LogManager.getLogger(CoralModule.class);
//
//    @Provides
//    @Singleton
//    static ExecuteTctSuiteHelper provideExecuteTctSuiteHelper() {
//        return new ExecuteTctSuiteHelper();
//    }
//
//    @Provides
//    @Singleton
//    static BobcatServer provideBobcatServer(Orchestrator coral, MetricsFactory metricsFactory) {
//        BobcatEndpointConfig endpointConfig = new BobcatEndpointConfig();
//        endpointConfig.setMetricsFactory(metricsFactory);
//        endpointConfig.setOrchestrator(coral);
//        endpointConfig.setNumThreads(32);
//
//        endpointConfig.setSslMode("retail.internal");
//        endpointConfig.setSocketTimeout(63000);
//        try {
//            endpointConfig.setUri("http://0.0.0.0:8080, https://0.0.0.0:8443");
//        } catch (URISyntaxException e) {
//            throw new RuntimeException("Failed to configure BobcatServer", e);
//        }
//        return new BobcatServer(endpointConfig);
//    }
//
//    @Provides
//    @Singleton
//    static Orchestrator provideOrchestrator(GeneratedDaggerActivityHandler activityHandler) {
//
//        List<ChainComponent> handlerChain = new ArrayList<>();
//        handlerChain.add(new Log4jAwareRequestIdHandler());
//        handlerChain.add(new HttpHandler());
//        handlerChain.add(new CrossOriginHandler());
//        handlerChain.add(new PingHandler(new ShallowHealthCheck()));
//
//        ContentHandler contentHandler = new ContentHandler();
//
//        handlerChain.add(contentHandler);
//        handlerChain.add(new ServiceHandler("ATACurriculumKindlePublishingService"));
//
//        handlerChain.add(new HttpRpcHandler());
//
//        handlerChain.add(activityHandler);
//
//        ChainHelper chainHelper = new ChainHelper();
//        chainHelper.setHandlers(handlerChain);
//        Orchestrator coral = new OrchestratorHelper(chainHelper, 30000);
//        return coral;
//    }
//
//}
