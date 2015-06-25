# Review Perspective #
[Takuya Yamashita](http://csdl.ics.hawaii.edu/Members/TakuyaYamashita.html) [Hongbing Kou](http://csdl.ics.hawaii.edu/~hongbing)

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

## Content ##
  1. [Introduction](JupiterUserGuide.md)
  1. [Installation](InstallationGuide.md)
  1. _Review Perspective_
  1. [The Jupiter Review Process](ReviewProcess.md)
  1. [Review ID configuration defaults](ReviewConfiguration.md)
  1. [Configuration Phase](ReviewConfiguration.md)
  1. [Individual Review Phase](IndividualReviewPhase.md)
  1. [Team Review Phase](TeamReviewPhase.md)
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)


# Review Perspective in Eclipse #
The Jupiter plugin provides a new perspective called "Review".  You can open the perspective by selecting "Window | Open Perspective | Other... | Review".

  * **Review Perspective** - Provides Review Table and Review Editor windows.
  * **Review Editor** - Supports entry of new Review Issues.  Typically, you will place the cursor (or select a region of text) where you wish to document an issue, right-click, and select "Add Jupiter Issue".
  * **Review Table** - Supports display and modification of all created issues.  The review table helps users to see a summary of all the review issues entries at a glance, allows them to switch review phase, enables them to sort by item category by slicking the header of a column name, and enables filtering by certain item conditions.
  * **Phase section** - Provides the selection of the review phases which are individual, team, and rework phase.  The phase selection icons are located in the main tool bar and the review table's tool bar.  The item categories (columns) in the review table and the tab in the review editor will be changed corresponding to the phase selection.  This will optimize the review for each review stage.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JupiterFeature.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JupiterFeature.jpg)
