# Recommendation Service Cache Design

## Cache Entries

### Key Type
*What is the type of the key of the cache entry?*
`BookGenre`

### Value Type
*What is the type of the value of the cache entry?*
`List<BookRecommendation>`

### Nulls and Errors
*What value will be stored in the cache if a null or error is encountered?*
In the case of nulls and errors, an empty list will be stored in the cache.

## Eviction

### Maximum Size
*What is the maximum number of cache entries that should be stored in the cache? How did you decide this value?*
`BookGenre.values().length + 1`
Since `BookGenre` is an enum with 13 items, we know we should only have 13 cache entries. We will need one additional
cache entry for a null genre.

### Time to Live
*This should include both the time and whether this time should be from the last time the cache entry was used or
when it was originally added to the cache. How did you decided this value?*
Expire 10 minutes after write
Since these are recommendations, we want to make sure to keep these pretty fresh and not miss any trending books. Since
there aren't that many genres, we should get a good cache hit rate. If we expired 10 minutes after access, it is likely
these cache entries would never actually expire and we would never show new recommendations.
