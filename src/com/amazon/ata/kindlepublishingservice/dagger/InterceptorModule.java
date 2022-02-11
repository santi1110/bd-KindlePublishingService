//package com.amazon.ata.kindlepublishingservice.dagger;
//
//import com.amazon.ata.kindlepublishingservice.KindlePublishingClientException;
//import com.amazon.ata.kindlepublishingservice.KindlePublishingServiceException;
//import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
//import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
//
//import com.amazon.coral.dagger.annotations.GlobalInterceptor;
//import com.amazon.coral.dagger.annotations.GlobalInterceptorOrder;
//import com.amazon.coral.validate.ValidationInterceptor;
//import com.amazon.coralx.exception.DefaultExceptionTranslationDefinition;
//import com.amazon.coralx.exception.ExceptionTranslationDefinition;
//import com.amazon.coralx.exception.ExceptionTranslationInterceptor;
//import com.amazon.coralx.exception.LogLevel;
//import com.amazon.coralx.threadlocal.ThreadLocalContextActivityInterceptor;
//import com.google.common.collect.ImmutableList;
//import dagger.Module;
//import dagger.Provides;
//
//import java.util.List;
//import javax.inject.Singleton;
//
//@Module
//final class InterceptorModule {
//
//    @Provides
//    @Singleton
//    @GlobalInterceptor
//    @GlobalInterceptorOrder(1)
//    static ValidationInterceptor provideValidationInterceptor() {
//        return new ValidationInterceptor();
//    }
//
//    @Provides
//    @Singleton
//    @GlobalInterceptor
//    @GlobalInterceptorOrder(2)
//    static ExceptionTranslationInterceptor provideExceptionTranslationInterceptor() {
//        List<ExceptionTranslationDefinition> defaultTranslations = ImmutableList.of(
//            new DefaultExceptionTranslationDefinition().withSource(BookNotFoundException.class)
//                .withTarget(KindlePublishingClientException.class)
//                .withLogLevel(LogLevel.WARN),
//            new DefaultExceptionTranslationDefinition().withSource(PublishingStatusNotFoundException.class)
//                .withTarget(KindlePublishingClientException.class)
//                .withLogLevel(LogLevel.WARN),
//            new DefaultExceptionTranslationDefinition().withSource(Exception.class)
//                .withTarget(KindlePublishingServiceException.class)
//                .withLogLevel(LogLevel.FATAL)
//        );
//        ExceptionTranslationInterceptor interceptor = new ExceptionTranslationInterceptor();
//        interceptor.setDefaultTranslations(defaultTranslations);
//        return interceptor;
//    }
//
//    @Provides
//    @Singleton
//    @GlobalInterceptor
//    @GlobalInterceptorOrder(3)
//    static ThreadLocalContextActivityInterceptor provideThreadLocalContextActivityInterceptor() {
//        return new ThreadLocalContextActivityInterceptor();
//    }
//
//}
