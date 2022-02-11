//package com.amazon.ata.kindlepublishingservice.dagger;
//
//import amazon.platform.config.AppConfig;
//import amazon.platform.config.AppConfigTree;
//import dagger.Module;
//import dagger.Provides;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.Arrays;
//import javax.inject.Named;
//import javax.inject.Singleton;
//
///**
// * Relies on the command line arguments and the system properties to initialize AppConfig.
// */
//@Module
//final class AppConfigModule {
//    private static final Logger log = LogManager.getLogger(AppConfigModule.class);
//
//    @Provides
//    @Singleton
//    static AppConfigTree provideAppConfigTree(@Named("AppArgs") String[] args) {
//        if (AppConfig.isInitialized()) {
//            throw new IllegalStateException(String.format(
//                "AppConfig has already been initialized with appName %s appGroup %s args %s",
//                AppConfig.getApplicationName(),
//                AppConfig.getApplicationGroup(),
//                Arrays.asList(AppConfig.getArguments())));
//        }
//
//        final String appName = System.getProperty("AppConfig.app");
//        final String appGroup = System.getProperty("AppConfig.appgroup");
//        log.info("AppConfig.initialize with appName[{}] appGroup[{}] args[{}]", appName, appGroup, Arrays.asList(args));
//        AppConfig.initialize(appName, appGroup, args);
//
//        return AppConfig.instance();
//    }
//}
