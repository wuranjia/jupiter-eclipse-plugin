# Jupiter's Rework Phase #
[Takuya Yamashita](http://csdl.ics.hawaii.edu/Members/TakuyaYamashita.html) [Hongbing Kou](http://csdl.ics.hawaii.edu/~hongbing)

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

## Content ##
  1. [Introduction](JupiterUserGuide.md)
  1. [Installation](InstallationGuide.md)
  1. [Review Perspective](ReviewPerspective.md)
  1. [The Jupiter Review Process](ReviewProcess.md)
  1. [Review ID configuration defaults](ReviewConfiguration.md)
  1. [Configuration Phase](ReviewConfiguration.md)
  1. [Individual Review Phase](IndividualReviewPhase.md)
  1. [Team Review Phase](TeamReviewPhase.md)
  1. _Rework Phase_
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)

# Rework Phase #
After the team review phase, the Jupiter plugin can help you work through the fixes to the code identified through the review.  For this rework phase, you might want to see which issues are assigned to you, and which issues you have not done yet so that you can work on what you have to do efficiently.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReworkSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReworkSelection.jpg)

You can select the "Rework Phase" mode and select the proper review ID and reviewer ID.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg)

In rework phase, the Jupiter editor view contains the status, resolution, and revision field.

Status field provides the status of the issue. You can change the status from "Unsolved" to "Solved" after fixing the issue. You might want to fix the issue in a different way than was suggested during the review after examining it more carefully. In this case, you can provide a comment documenting your new approach in the Revision field.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorRework.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorRework.jpg)

Once again, make sure you commit your changed .review files to your configuration management repository.