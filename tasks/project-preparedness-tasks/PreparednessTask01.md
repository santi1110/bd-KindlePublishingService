### Project Preparedness Task 1: Use the Source, Again, and Again

#### Milestone 1: Setup your workspace

Run `toolbox update` in your terminal to ensure the latest versions are installed. You should see that you are running
brazilcli version `2.0.202844.0` or later.

Create a local Brazil workspace using the `ATACurriculum-C2020May/KindlePublishingService-<alias>` version set.
Checkout the `<alias>ATACurriculumKindlePublishingService` Brazil package in this new workspace.

You will see the following message while adding your package to the workspace.
This is expected and you do not need to use the `brazil ws use --platform` command.

```
Detected that version set 'ATACurriculum-C2020May/KindlePublishingService-<alias>' only builds for platform 'AL2_x86_64'.
Setting workspace platform to AL2_x86_64.
To change this setting, use the `brazil ws use --platform` command.
```

In your new workspace, you will need to run `brazil setup platform-support`. It will tell you that it is configuring
the workspace to use ‘overlay’ mode, which requires cleaning the workspace and ask if you want to proceed. Enter `y`.
We haven’t built anything yet so there is nothing to clean! This has completed correctly if you see the message below.

```
The workspace is configured to use 'overlay' *mode*.

You must now re-build **all** your packages, which can also **be** done via running:

    brazil-recursive-cmd --allPackages brazil-build
```

You don’t need to rebuild anything now (don’t run `brazil-recursive-cmd`), because we are about to build it all for
the first time!

Now inside your `<alias>ATACurriculumKindlePublishingService` Brazil package, run `rde stack provision` to complete
setting up your workspace.

To verify everything is working correctly let’s go ahead and run the default RDE workflow, `rde wflow run`.
This workflow will build the code and start your service (this does NOT run any TCTs). The first time it runs, this
process may take a while - up to 20 minutes. If your workflow run doesn’t end with the line below (timestamp may vary,
that’s okay :) ), please open a CQA with what you think might be the problem, or come to office hours to get help.

```
[13:43:48-I] Finish running workflow [OK]
```

Unfortunately for all RDE workflows run in this project you will see warning message below. This is expected and you do
not need to take any action.

```
If you are exposing Java debug ports *form* your RDE application(s) please ask for an exception to avoid getting SEV2'd by InfoSec:
https://sim.amazon.com/issues/ASOC-CM-PUB-2
```


#### Milestone 2: Setup your IntelliJ project
Open the workspace you created in IntelliJ. Don't forget you can reference the steps from the
[Open Your Workspace in IntelliJ](https://w.amazon.com/bin/view/Amazon_Technical_Academy/Internal/HowTos/Open_Your_Workspace_In_IntelliJ/)
How To. Let’s ensure everything is configured in your IDE to build correctly. In IntelliJ, click on the "Build" menu
and then select "Build Project."

You may see an error message pop up saying the SDK is not specified. If you do, select OK. The Project Structure menu
should open. If it doesn’t select "File" > "Project Structure..." In the Project SDK drop down select "1.8". Ensure the
Project language level drop down reads "8 - Lambdas, type annotations etc." Click "Apply" and "OK." Once again select
the "Build" > "Build Project" menu option.

You may see an error message pop up saying the output path is not specified. Open the Project Structure menu; select
"File" > "Project Structure..." For "Project compiler output", select the folder icon in the text box. Select the `out`
folder in the root of your Unit 4 project workspace. If there isn’t an `out` folder, select your package folder and
then in the text box append `/out`. Once again select the "Build" > "Build Project" menu option.

There will be a package called `ATACurriculumKindlePublishingServiceImageBuild` in your workspace. You won’t need to
make any changes to this package, but it is required to help build and start your service.

**Exit Checklist:**

* You are able to successfully run `rde wflow run`
* You have set up your project in IntelliJ
