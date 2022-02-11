# Unit 4 Project: Kindle Publishing Service

## Preliminaries: The More Things Change

We've been using the same approach for [how ATA projects
work](https://mlu.instructure.com/courses/460/pages/how-unit-projects-work-at-ata) since Unit 1, and
we're sticking with it for Unit 4. There will still be PPTs and MTs, checked with TCTs. (If you want
to refresh your knowledge of these acronyms, refer to the [Unit 2 project README](https://code.amazon.com/packages/ATACurriculumSustainabilityShipmentServiceLambda/blobs/mainline/--/README.md).)
Project Buddies, Office Hours, and the CQA will still be available to help with your questions.
We'll still be using Hydra, and our logs will still be in Cloudwatch. We'll also be using the same
[Git development workflow](https://w.amazon.com/bin/view/Amazon_Technical_Academy/Internal/HowTos/Development_Workflow/).

In Unit 4, we'll start in a similar place to Unit 3, creating a lot from scratch and working though
a design document.

This project is gonna be LIT(erary)!

### Ambiguity, Complexity, and Scope

Ambiguity will be increasing from Unit 3’s project as you make your way through the Unit 4 project
mastery tasks. We’ll assume you’ll be using the design document to understand the requirements and
how to implement each API, so the tasks will contain fewer details. After walking you through
something once, we won’t be mentioning it again in each subsequent task. For example, we used Dagger
in Unit 3, and will again in Unit 4. We’ll walk you through it a bit in mastery task 2, but then
you’ll be responsible for knowing that this is something you’ll need to think about and update in
each subsequent task.

We’ll also be increasing complexity. We'll be working with threads for the first time, and write a
new style of API to take advantage of them. We'll also be integrating caching into our project.

Scope will increase slightly. As in last unit, we'll be implementing the entire service, but now
you'll be responsible for writing the Activity classes as well.

You'll be surrounded by other participants in the same situation as you, so remember to collaborate:
rely on each other for assistance, and share your own knowledge.

## Unit 4 Project Progress and Tracking

### Doneness checklist

You're done with this project when:

☐ You can access your service through the coral explorer (PPT03)

☐ You can configure a new Activity in your service package (PPT04)

☐ Your project buddy has reviewed your design changes for MT01

☐ A peer has reviewed your design changes for MT05 

☐ All Task Completion Tests are passing

☐ You've submitted Project Reflection response in Canvas

☐ You've completed Participant End-of-Unit Survey (a short survey to help us improve the project
and the project process for next time! Optional, but highly encouraged)


## The Problem: Amazon Kindle Publishing

The Amazon Kindle store provides millions of ebooks to our customers. The process of publishing an
ebook to the kindle catalog is currently an extremely manual process, which causes a long wait time
to add a book to the catalog.

As a member of the Amazon Kindle team, you will be launching a new service that allows our
publishing department to convert books into a digital format.

*NOTE: We don't want to give the wrong impression, Amazon Kindle already supports the conversion
from physical to digital format, but this is our interpretation and simplification of what the
process of designing and implementing such a service might look like.*

The overview, architecture, and implementation are covered in the [design document here](DESIGN_DOCUMENT.md). Almost all major pieces of software at Amazon first go through an intensive design
review to answer the question "Are we building the right thing for our customer?".

By working on the preparedness and mastery tasks for this project, you will finish the
implementation of the service described in the document.

Carefully read the design document and refer back to it while working on the tasks.

## Project Preparedness Tasks

Up to this point, the services we have developed in ATA projects have had synchronous APIs. This
means that a client makes a request to the service, all of the work required to fulfill this request
is done, and then a response is returned to the client. In an asynchronous API, a client makes a
request, the service returns a response immediately, and the service completes the work after the
client disconnects. This is helpful when the work that needs to be done will take a long amount of
time. A client will only wait so long for a response, so it is helpful to quickly return a
successful response acknowledging the work is under way. The service will then continue to work on
the request as it continues to receive other, new requests. The service is working on these requests
concurrently - we can think of this as multi-tasking for now. We’ll spend a lot more time in ATA in
this unit and future units digging into the concept of concurrency much more deeply.

In previous units, we were deploying our services to AWS Lambda, which allowed us to save money and
resources by shutting down when we weren't busy working on a request. In this project, we will be
using AWS ECS to deploy and run our service. Unlike Lambda, our ECS service will always be up and
running. This will allow us to do our asynchronous work, since we won’t shut our service down after
we send back a response. We still have work to do!

Switching to this new model will come with some changes in our code. We are still using Coral but it
will look a little different than our Lambda Coral services. We’ll walk you though some of the major
changes in the PPTs. Let’s get started!

### [Project Preparedness Task 1: Use the Source, Again, and Again](tasks/project-preparedness-tasks/PreparednessTask01.md)

### [Project Preparedness Task 2: Hello, Project Buddy](tasks/project-preparedness-tasks/PreparednessTask02.md)

### [Project Preparedness Task 3: I Spy an API Tool](tasks/project-preparedness-tasks/PreparednessTask03.md)

### [Project Preparedness Task 4: Changes Ahead!](tasks/project-preparedness-tasks/PreparednessTask04.md)

&nbsp;

## Project Mastery Tasks

### [Mastery Task 1: Killing me softly](tasks/project-mastery-tasks/MasteryTask01.md)

### [Mastery Task 2: Submit to the process](tasks/project-mastery-tasks/MasteryTask02.md)

### [Mastery Task 3: Query, query on the wall, don’t load one, get them all!](tasks/project-mastery-tasks/MasteryTask03.md)

### [Mastery Task 4: Make a run(nable) for it](tasks/project-mastery-tasks/MasteryTask04.md)

### [Mastery Task 5: Recommendations - a Cache Cow](tasks/project-mastery-tasks/MasteryTask05.md)

### [Mastery Task 6: I don’t want to miss a thing](tasks/project-mastery-tasks/MasteryTask06.md)

### [Mastery Task 7: Mary had a little lambda](tasks/project-mastery-tasks/MasteryTask07.md)

&nbsp;

## Project Reflection

Think over the entire project, from the introduction to the final mastery task completion. Answer
three or more questions in the [Canvas quiz](https://mlu.instructure.com/courses/586/quizzes/1712?module_item_id=3688).
