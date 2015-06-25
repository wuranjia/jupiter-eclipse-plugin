# Preference Filter Configuration #
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
  1. [Rework Phase](ReworkPhase.md)
  1. _Preference filter configuration_
  1. [Item Reference](ItemReference.md)

# Preference filter configuration (for power users) #
Jupiter has two kind of filter setting: Preference filter setting (preference level) and Review ID filter setting (property level). Underling idea for the Review ID filter is that the author of a review ID can control all filters for each phase. This will save a time for customizing filters for each phase in the author's point of view. However, power users (or especially reviewers in the individual phase) might feel lame if all filter setting is under the control of the author). The object for the preference filter is for power users to control filter in their Eclipse.

The review ID filter determines the filter setting during the review session. so if review ID was changed to another, the filter setting is changed as well. This filter will be determined by the author of a review ID so that the reviewers for the review ID do not need to worry about the filter at all.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdFilter.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdFilter.jpg)

On the other hand, the preference filter determines the individual Eclipse environment filter setting regardless of review sessions (review IDs).

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PreferenceFilter.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PreferenceFilter.jpg)

If you check the "Overwrite the property's filter setting" in the preference filter setting, then you can use your own filter regardless of review session.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PreferenceFilterSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PreferenceFilterSetting.jpg)

If you want to use review ID filter setting in team phase, for example, you have to disable (uncheck) the "Overwrite the property's filter setting". Otherwise, the review ID filter setting will not be reflected to the review id session.