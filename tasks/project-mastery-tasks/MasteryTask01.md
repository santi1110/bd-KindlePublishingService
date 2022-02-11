### Mastery Task 1: Killing me softly


#### Milestone 1: Create class and sequence diagrams

**Reviewer: Project Buddy**

One of the action items from the design review was to add a class diagram with the existing classes in the service.
Create a class diagram of the classes in the following packages and how they relate to each other:

* `com.amazon.ata.kindlepublishingservice.activity` (except ExecuteTctActivity.java)
* `com.amazon.ata.kindlepublishingservice.clients`
* `com.amazon.ata.kindlepublishingservice.dao`
* `com.amazon.ata.kindlepublishingservice.dynamob.models`
* `com.amazon.ata.kindlepublishingservice.exceptions`
* `com.amazon.ata.kindlepublishingservice.metrics` (except MetricsConstants.java)

The diagram does **not** need to include any classes in the following packages:

* `com.amazon.ata.kindlepublishingservice.converters`
* `com.amazon.ata.kindlepublishingservice.dagger`
* `com.amazon.ata.kindlepublishingservice.publishing`

The DynamoDB model classes in the diagram **must** include all fields and types used to model the DynamoDB table, and
which fields will represent the partition and (if any) sort key. You do not need to provide any other annotations, but
be sure to indicate the Java type. In each provided model, we’ve chosen to use an enum to represent the value of an
attribute. You’ll see this annotation: `@DynamoDBTypeConvertedEnum` on the getter of the attribute. This tells the
DynamoDBMapper to convert the enum to a string value when storing it in the table, but allows your Java code to
restrict the attribute to a set of allowed values. Use the enum type in your diagram.

Use the `src/resources/mastery-task1-kindle-publishing-CD.puml` to document the class diagram.

Next, create a sequence diagram for the API we'll be working on in milestone 2, `RemoveBookFromCatalog`. You can use
the existing sequence diagrams in the design document as a starter. Include any error handling the APIs are expected to
perform, as well as their interactions with other classes.

Update `src/resources/mastery-task1-remove-book-SD.puml` with a sequence diagram for your planned implementation of the
`RemoveBookFromCatalog` operation.

**Recall:** [We can use PlantUML’s `alt` syntax](http://wiki.plantuml.net/site/sequence-diagram#grouping_message) to
represent if/else cases for validation.

**Recall:** We can add the `@DynamoDBHashKey` and `@DynamoDBRangeKey` annotations to the class diagram. Here’s an
[example format](https://plantuml.corp.amazon.com/plantuml/form/encoded.html#encoded=SoWkIImgAStDuKhEIImkLd3ApyzMgEPoSAdCIypDTt7oI2pEy4wjL4W2YdkcA5Wf19SKPUQb8nG49UQbfu9KbAKM5MVcvm6LUEQLfAQd5d7LSZcavgK0pGO0).

Once done, you can remove the placeholder `<MT01.MILESTONE 1>` tags in the
[design document](https://code.amazon.com/packages/<alias>ATACurriculumKindlePublishingService/blobs/heads/mainline/--/DESIGN_DOCUMENT.md).
Run `rde wflow run tct-task1-design` and ensure it passes, create a commit and submit a CR with the diagrams to your
project buddy. The title of your commit and CR must begin with `[MT01][Design]`.


#### Milestone 2: Implement RemoveBookFromCatalog

**Reviewer: Project Buddy**

We already got a head start on this in PPT04. You’ll need to now add some logic to do a soft delete. We don’t want to
lose previous versions of the book that we have sold to customers. Instead, we’ll mark the current version as inactive
so that it can never be returned by the `GetBook` operation, essentially deleted.

We’ll need to update our `CatalogDao` to implement this “delete” functionality and use that in our `Activity` class.

When writing unit tests for your new logic in `CatalogDao`, we encourage you to use
[ArgumentCaptor](https://site.mockito.org/javadoc/current/org/mockito/ArgumentCaptor.html)s. To see one in action in
the project take a look at the `getBookFromCatalog_oneVersion_returnVersion1` unit test in the
[CatalogDaoTest](https://code.amazon.com/packages/<alias>ATACurriculumKindlePublishingService/blobs/mainline/--/tst/com/amazon/ata/kindlepublishingservice/dao/CatalogDaoTest.java)
class.

After running `rde wflow run` to rebuild your service, you can use the Coral explorer to test
your implemented API.

We’ve added a local Code Coverage Police check to your project’s build process. This will mimic what the check in your
pipeline does, helping you identify code coverage drops locally, rather than running into it after you push your code.
Unfortunately, the check runs only against code that is committed locally. So make sure you are running your tct
workflow after you have committed your code to verify this is passing. If it fails you will see something like the
following:

```
[CCP] Coverage for current version local.version.0.0 (95.8641%) is less than previous version V2Cohort1.240.0 (96.0000%).
```

Take a look at your coverage report and see where you can add coverage!

We’ve generated some catalog data and put it in your CatalogItemVersions DynamoDb table. You can find a book there to
remove, or feel free to add any additional test data.

Run `rde wflow run tct-task1` to make sure all tests for this task are passing. Once done, submit a CR with all the
code changes to your project buddy. The title of your commit and CR must begin with `[MT01]`, once the CR is approved,
push this code to your pipeline.

**Exit Checklist:**

* You’ve implemented `RemoveBookFromCatalog`’s functionality
* Your CR adding class and sequence diagrams is approved by your project buddy and pushed
* `rde wflow run tct-task1` passes
* Your CR implementing `RemoveBookFromCatalog` is approved by your project buddy and pushed
* Mastery Task 1 TCTs are passing in your pipeline
