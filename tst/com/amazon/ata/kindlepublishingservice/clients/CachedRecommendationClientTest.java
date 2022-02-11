package com.amazon.ata.kindlepublishingservice.clients;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.clients.CachedRecommendationClient;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationClient;
import com.amazon.ata.kindlepublishingservice.metrics.MetricsConstants;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
//
//import javax.measure.unit.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CachedRecommendationClientTest {

    @Mock
    private RecommendationClient delegateRecommendationClient;

//    @Mock
//    private MetricsPublisher metricsPublisher;

    @InjectMocks
    private CachedRecommendationClient cachedRecommendationDAO;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void getBookRecommendations_cacheMiss_callsServiceClient() throws Exception {
        // GIVEN
        List<BookRecommendation> bookRecommendations = new ArrayList<>();
        BookRecommendation bookRec1 = new BookRecommendation("Book Title 1", "Author 1", "ASIN1");
        BookRecommendation bookRec2 = new BookRecommendation("Book Title 2", "Author 2", "ASIN2");
        bookRecommendations.add(bookRec1);
        bookRecommendations.add(bookRec2);
        when(delegateRecommendationClient.getBookRecommendations(BookGenre.COOKING)).thenReturn(bookRecommendations);

        // WHEN
        List<BookRecommendation> result = cachedRecommendationDAO.getBookRecommendations(BookGenre.COOKING);

        // THEN
        assertEquals(bookRecommendations, result, "Expected result to match value returned by " +
                "RecommendationsService");
        verify(delegateRecommendationClient).getBookRecommendations(BookGenre.COOKING);
//        verify(metricsPublisher).addCount(eq(MetricsConstants.BOOK_RECOMMENDATIONS_CACHE_QUERY_COUNT),
//            anyDouble(), any(Unit.class));
    }

    @Test
    public void getBookRecommendations_cacheHit_retrievesFromCache() throws Exception {
        // GIVEN
        List<BookRecommendation> bookRecommendations = new ArrayList<>();
        BookRecommendation bookRec1 = new BookRecommendation("Book Title 1", "Author 1", "ASIN1");
        BookRecommendation bookRec2 = new BookRecommendation("Book Title 2", "Author 2", "ASIN2");
        bookRecommendations.add(bookRec1);
        bookRecommendations.add(bookRec2);
        when(delegateRecommendationClient.getBookRecommendations(BookGenre.FANTASY)).thenReturn(bookRecommendations);

        // WHEN
        List<BookRecommendation> firstResult = cachedRecommendationDAO.getBookRecommendations(BookGenre.FANTASY);
        List<BookRecommendation> secondResult = cachedRecommendationDAO.getBookRecommendations(BookGenre.FANTASY);

        // THEN
        assertEquals(bookRecommendations, firstResult, "Expected result from first to match value returned by " +
                    "Recommendations Service");
        assertEquals(bookRecommendations, secondResult, "Expected result to match value returned by cache");
        // should only be called once since second call should be retrieving from the cache.
        verify(delegateRecommendationClient).getBookRecommendations(BookGenre.FANTASY);
        verifyNoMoreInteractions(delegateRecommendationClient);
//        verify(metricsPublisher, times(2)).addCount(eq(MetricsConstants.BOOK_RECOMMENDATIONS_CACHE_QUERY_COUNT),
//            anyDouble(), any(Unit.class));
    }

}
