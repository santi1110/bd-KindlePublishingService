### Project Preparedness Task 4: Changes Ahead!

Let’s take a look at some of the differences in our Unit 4 project’s Coral service from our previous projects.

#### Milestone 1: Logs

Let’s review how to access local logs for our project. These will be extremely useful when an API fails as you are
developing. First we will
[connect to our local RDE container](https://code.amazon.com/packages/ATACurriculum_LearningMaterials/blobs/mainline/--/remote-debugging-and-logging/02-connecting-to-local-rde.md).
The app-name will be `<alias>ATACurriculumKindlePublishingService`. Your logs will be located in a different place than
they were last unit. They will be in the `/apollo/env/ATACurriculumKindlePublishingService/var/output/logs directory`.
Within this directory you should see a file that starts ‘application.log.’ followed by a timestamp. This will be where
you see any log statements you add to your code and any exceptions that occur while your code is executing.

#### Milestone 2: Debugging

Like in previous units, we have provided debug workflows for all mastery tasks. You should configure your IDE debug
configuration to listen on port 5054. You can refer back to your Unit 2 MT01 task for instructions on setting this up.
Once your debug workflow pauses and displays the below output in the console, go ahead and attach your debugger.

```
+ export RDE_APP_NAME=HydraTestApp
+ RDE_APP_NAME=HydraTestApp
+ /home/deceneu/run-hydra-tests
```

In addition to debugging specific workflows, you may find it helpful to use the debugger when you call your APIs from
the Coral explorer. You first need to start your service with the default rde workflow, `rde workflow run`, and when
the workflow has completed, attach your debugger from IntelliJ. Make sure you set a breakpoint, and then call your API
from the Coral explorer. IntelliJ should open and be waiting at the breakpoint.

#### Milestone 3: Interceptors

Coral provides us the opportunity to do pre- and post-processing of a request. Before Coral executes the code
associated with our operation, we can intercept the request. In our project we have setup a `ValidationInterceptor`.
The validation interceptor ensures that the inputs provided to our operation are “valid”. If not, Coral will return an
error immediately, without processing the request. The validation rules are setup in our Coral model. For example, the
`GetBookRequest` is
[required to contain a bookId](https://code.amazon.com/packages/ATACurriculumKindlePublishingServiceModel/blobs/1a7ed6f5be18da8b221d97d162179eff927df12e/--/model/main.xml#L75),
and that
[bookId cannot be an empty string](https://code.amazon.com/packages/ATACurriculumKindlePublishingServiceModel/blobs/1a7ed6f5be18da8b221d97d162179eff927df12e/--/model/main.xml#L73).
Open your Coral explorer and click on the `GetBook` operation. Let’s try to make a request without a bookId. Delete the
bookId from the JSON so the request is just `{}`. Make the request by clicking on the ‘Call GetBook’ button. You should
see a response like:

```
{"__type": "com.amazon.Coral.validate#ValidationException",
 "message": "1 validation error detected: Value null at 'bookId' failed to satisfy constraint: Member must not be null"}
```

This means that we don't need to write code to do these validations, simplifying our implementation.

After the code associated with an operation executes, Coral provides an opportunity to do any last minute processing
before the request is returned to the client. In our project, we have setup an `ExceptionTranslationInterceptor`. The
exception translator interceptor allows us to transform one type of exception into another. In our Coral model, we have
to define the types of exceptions that our operations will throw. If an exception is thrown by our operation that does
not match the types defined in our Coral model, an `InternalFailureException` is returned to the client. This is not
super informative to our client, and so we want to prevent that from happening. For each API, our Coral model has
defined a client exception and a service exception that can be thrown. We will throw a
`KindlePublishingClientException` if the client has caused the error, typically by providing the service with bad
input, and we will throw a `KindlePublishingServiceException` when something has gone wrong internally that we cannot
recover from. Our exception translator provides us the opportunity to make sure these are the only type of exceptions
we throw. We currently have mapped a `BookNotFoundException` to transform into a `KindlePublishingClientException` and
for all other exceptions thrown we have mapped them to a `KindlePublishingServiceException`.

Both interceptors are created in the `InterceptorsModule` class. We make the Coral framework aware of them by
annotating the methods that create each interceptor with `@GlobalInterceptor`.

#### Milestone 4: Activity Classes

**Reviewer: Unit-4-Instructor-Reviewers**

In the sequence diagram from PPT03, the CoralFramework has to instantiate an Activity class and call the correct method
for our API. It finds these with the `@Service` and `@Operation` annotations. Our `GetBookActivity` class is annotated
so the `execute` method will run when the ATACurriculumKindlePublishingService receives a GetBook request. The name
"execute" is recommended by Coral, but not required. If Coral cannot find an annotated method using the request and
response classes defined in the model, it throws an `UnknownOperationException`.

Let’s try creating a new operation and validate that we can call it from our explorer. We’ll tackle one of the APIs we
will be creating in the project. We will be working on `RemoveBookFromCatalog`. Let’s first create a new class for our
API that follows the rules about implementing `Activity` and has the correct @`Service` annotation (This should be the
same service). It is a best practice to name the class the name of the API plus ‘Activity’, so create a
`RemoveBookFromCatalogActivity` class. Make sure to add a Javadoc for the class.

Take a look at `RemoveBookFromCatalog`'s
[model definition](https://code.amazon.com/packages/ATACurriculumKindlePublishingServiceModel/blobs/5eb6daa6c97dcd4a5abe68c0a7632b148b2d94ca/--/model/main.xml#L39).
From here we can see what the defined inputs and outputs of our service will be. Now that we know that the `execute`
method we write should have a `RemoveBookFromCatalogRequest` as an input, and a `RemoveBookFromCatalogResponse` as a
return type, we can write a stub implementation. 'Stubbing' code is helpful to prepare for later changes by writing
the code to a point where our service can handle requests, even if the implementation isn't fully complete, leaving
us to focus on implementing it in a later task.

Write the `execute` method header, ensuring that it is public, annotated properly, and has a Javadoc. The `@Operation`
annotation should contain the name of the operation from the Coral model.

The `RemoveBookFromCatalogResponse` doesn’t actually contain any fields. Typically, we might write this as a void
method, however once you set a contract for an API you are pretty set in stone. If we later decide we need to return
some additional information from the API we won’t be able to. We may break our clients. This is why we always return a
response object that we can later add fields to in case this situation arises. This makes our stub implementation
pretty easy to write. Let’s just return a new, empty response object from our API:

`return new RemoveBookFromCatalogResponse();`

We'll also annotate the constructor with `@Inject` to indicate to Dagger, our dependency injection framework, that it
should create the class for us.

```
@Inject
public RemoveBookFromCatalogActivity() {
}
```

Finally, let’s add a simple unit test to ensure the `execute` method returns a non null response, and then we’ll test
our new API! We’ve provided a [test class example](https://paste.amazon.com/show/hcrites/1582522995) for you to add
to your service to satisfy code coverage checks. Now let’s restart our service by running `rde workflow run`. Once your
workflow has successfully finished, open your [Coral explorer](http://localhost:1184/explorer/index.html). Go ahead and
make a request to your newly implemented API. You should receive an empty, successful response, you’ll see that it has
a 200 response code in the response section. If you got an `UnknownOperationException`, make sure that your
`RemoveBookFromCatalogActivity` class and your `execute` method are both annotated, and the service name and operation
are correctly spelled with the same capitalization as defined in the Coral model.

Once your API responds successfully, create a CR for the changes you have made to your service. The title of your
commit and CR must begin with `[PPT04]`. Submit you CR to the Unit-4-Instructor-Reviewers (TEAM) for review, once the
CR is approved, push this code to your pipeline.

**Exit Checklist:**

* `rde wflow run preparedness-task4` passes
* Your CR with the dummy implementation of RemoveBookFromCatalog has been approved by ATA-Instructors and pushed.
* PPT04 TCTs are passing in your pipeline.
