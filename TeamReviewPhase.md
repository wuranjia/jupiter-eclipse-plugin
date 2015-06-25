# Jupiter's Team Review Phase #
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
  1. _Team Review Phase_
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)


# Team Review Phase #
In this phase, team members review all of the issues that have been generated for a given Project and Review ID.

A well filtered view will save you a great deal of time in the group meeting. To begin the Team Review Phase, click the "Open Jupiter Issue View" icon on the main tool bar (the purple '4', which is the Greek symbol for Jupiter). If the icon is not available, select "Customize Perspective", then click on the "Commands" label to display the Commands group, then click "Review".  Once that is accomplished, the following pull-down menu should be available:

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/TeamSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/TeamSelection.jpg)

After clicking the purple "Team Review" mode button above, the review ID selection page will pop up. If the correct Project is not listed, then cancel this page, select the project in the "Navigator" or "Package Explore" pane, and then select the "Team Phase" mode again. After making sure the project name is correct, you can select the review ID and reviewer ID.  Choose your own reviewer ID and the appropriate Review ID for this review session.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg)

To see each issue, you just click on a row in the Review Table, which displays the corresponding issue in the Review Editor.  If you edit a pre-existing issue, be sure to click the "Save", "Next", or "Previous" button. All three buttons save the modified issue. If you want to see what the actual source code that the issue refers to, double click on the particular issue row.

The Assigned To field contains the author of the review ID as a default, but you can change this to any member of the review team if that is more appropriate.

An important part of the Team Review phase is to set the "Resolution" field.  This field records the group's consensus regarding the current issue--does it actually need fixing? Is it actually a defect after all?

The Annotation field allows you to add supplemental information about this issue resulting from discussion during the Team Review Phase.

You can make use of the "Next" and "Previous" yellow arrow icons to move back and forth along the issue list.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorTeam.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorTeam.jpg)

Finally, the "Jump" bottom on the left side of either the Jupiter editor view or the Jupiter issue view enables you to retrieve the source code associated with this issue at any time.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Jump.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Jump.jpg)

Jupiter also allows you to display issues in read-only form using the purple marker fields in the source code editor.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Markers.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Markers.jpg)

If you single-click on the purple marker in the left hand side of the text editor, you can see the review issue summary in the resolution selection window with the purple marker. When you select the issue summary, you can see the description of the review issue in the right hand side. To see the full information of the review issue, you can just single-click on the review summary, then the review issue will be filled in the review editor view.

Note that markers obey the current filter settings.  For example, if review issues are filtered to only those containing "Unset" in the Resolution field, then the marker for an issue will disappear as soon as its Resolution field is changed to another value such as "Valid-NeedsFixing".  This is a good way to keep track of the issues that you haven't yet dealt with during the Team Review Phase.

Finally, don't forget to commit the modified .review files to your configuration management repository at the end of the Team Review Phase. These files are located in the review directory which you specified during the configuration phase.