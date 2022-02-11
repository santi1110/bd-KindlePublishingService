### Mastery Task 7: Mary had a little lambda

**Reviewer: Unit-4-Instructor-Reviewers**

Your teammate just mentioned in stand-up that he wrote a class called `CoralConverterUtil`
with a method in it that sounded like it might be useful. They added it to the `ATACurriculum_JavaProjectResources`
package so you already have access to the class from your service.

The method they mentioned is a static method called `convertList`. They said it takes in a list of items to convert and returns a new list of the Coral models, all you have to do is provide it with a `Function`. The `Function` should tell the `convertList` method how to convert each item in the list. This seems pretty cool because your service is converting a couple lists into their Coral model equivalents.

Update the conversion logic you have for converting the following lists to use the `convertList` method:

* `List<PublishingStatusItem>` to `List<PublishingStatusRecord>`
* `List<BookRecommendation>` to `List<com.amazon.ata.kindlepublishingservice.BookRecommendation>`

Refactor your code to use the shared `convertList` method.

Run `rde wflow run tct-task7` to validate your changes and submit a CR to Unit-4-Instructor-Reviewers (TEAM). The
title of your commit and CR must begin with `[MT07]`. Once the CR is approved, push the code to your pipeline.

**Doneness Checklist:**

* You’ve updated your coral conversion logic to utilize the shared `convertList` method
* `rde wflow run tct-task7` passes
* Your CR is approved by an ATA instructor and pushed

