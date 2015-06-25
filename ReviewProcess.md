# The Jupiter Review Process #
[Takuya Yamashita](http://csdl.ics.hawaii.edu/Members/TakuyaYamashita.html) [Hongbing Kou](http://csdl.ics.hawaii.edu/~hongbing)

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

## Content ##
  1. [Introduction](JupiterUserGuide.md)
  1. [Installation](InstallationGuide.md)
  1. [Review Perspective](ReviewPerspective.md)
  1. _The Jupiter Review Process_
  1. [Review ID configuration defaults](ReviewConfiguration.md)
  1. [Configuration Phase](ReviewConfiguration.md)
  1. [Individual Review Phase](IndividualReviewPhase.md)
  1. [Team Review Phase](TeamReviewPhase.md)
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)

# Jupiter's Four Step Process #
A code review in Jupiter involves the following four step process:

### Step 1: Configuration ###
In this step, you define a new "Review ID" that represents the new code review.  Every Jupiter code review is associated with a single Eclipse project.  In the configuration step, you also specify the files in the Project that should be reviewed, the IDs of those who should perform the review, the types of issues that can be raised during the review, and the location in the Project's root directory where data files generated during the review should be stored.

This configuration information is stored in a file called ".jupiter" in the top-level directory of your Project.  The .jupiter file contains information about all of the Code Reviews (each with their own unique ID) associated with the Project.

Once the leader of the code review has finished configuring a new Review ID to represent this code review, they must commit the .jupiter file to the configuration management system (so that it is now available to all reviewers of the code).  They will then typically send out an email announcing the code review.  The email should indicate the Project upon which the review is to be performed, the Review ID to be used, and finally reminding members to update their local repository in order to get the .jupiter file containing the configuration information.


### Step 2: Individual Review ###
During this phase, each person doing the review works by themselves to review the specified files and create issues.  This is easy to do in Jupiter: just move the cursor to the place in the code where there is an issue (or select a region of code), right-click, and select "Add Jupiter Issue".   The issues are saved into an XML file whose name contains the review ID and the selected Jupiter user name for this review.  This file has the suffix ".review", not ".xml", even though it is internally in XML format. The .review files are saved into a subdirectory in the Project directory that was specified during the configuration phase.

During the individual review phase, Jupiter shows only the issues created by the selected user that have been created for the selected Review ID.

Once individual review of the code is completed, the review file must be added to the configuration management repository so that all comments and issues are available during the Team Review Phase.

### Step 3: Team Review Phase ###
Once everyone has created their individual review files, it is time for the group to go over the work together. This is typically done as follows: one person updates their local workspace to obtain copies of all of the individual review files and then brings up Eclipse and enters the Team Review Phase.  During this phase, all of the issues created by all members of the team are available for display, editing, or deletion.

Depending upon the needs and goals, the issues can be reviewed member-by-member, or going through each file, looking at all of the issues in order by location in the file, or even sorting the issues by severity and tackling the most important ones first.  Jupiter supports all of these methods and allows for switching between approach at any time during the Team Review.

At the conclusion of Team Review, the modified review files should be committed back into the configuration management repository so that the results of the Team Review are available for the next phase, Rework.

### Step 4: Rework Phase ###
During rework, the author of the code under review (or other members of the team) go through the issues as finalized during the Team Review Phase and make corrections to the code as necessary.  They can update each issue to indicate its final status and what they did to make the correction.

At the conclusion of the rework phase, the review files should once again be committed to the configuration management repository so that the outcome of review is available to everyone on the team and is permanently stored as part of the project records.