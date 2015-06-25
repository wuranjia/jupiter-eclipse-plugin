# Jupiter's Individual Review Phase #
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
  1. _Individual Review Phase_
  1. [Team Review Phase](TeamReviewPhase.md)
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)

# Individual Review Phase #
After configuration, it's time to add review issues. First, update your Project from the configuration management system so that you get the .jupiter file containing this review ID.  Then, select the Jupiter Perspective, and select "Individual Phase" mode.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/IndividualSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/IndividualSelection.jpg)

You are then prompted to select a Project, a Review ID and a Reviewer ID, which identifies what you are working on, who you are, and where the data you generate during this review should be stored.   If you don't see the correct Project listed, cancel, open the correct Project, select it, then select the Individual Phase mode again.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdSelection.jpg)

The Jupiter issue view contains the following icons.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ViewToolBar.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ViewToolBar.jpg)

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JumpIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JumpIcon.jpg) Jump Icon - Jump to the specific source code that the selected issue refers to

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditIcon.jpg) Edit Icon - Edit the selected issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/AddIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/AddIcon.jpg) Add Icon - Add a new issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/RemoveIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/RemoveIcon.jpg) Remove Icon - Remove the selected issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/FilterIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/FilterIcon.jpg) Filter Icon - Filter the issue list.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PhaseSelectionIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PhaseSelectionIcon.jpg) Phase Selection Icon - Refresh the table for change phase mode.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PullDownIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PullDownIcon.jpg) Pull-down Icon - Contains the preference and property settings.

If the author of the review ID specify the reviewing files, the files are listed in the jump icon. Click the small downward triangle icon next to the jump icon, select one of the review file which the author want you to take a look at. you can jump to the target file and start review seamlessly.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdListSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/ReviewIdListSelection.jpg)

To add a review issue entry, you must click on "Add Jupiter Issue", which is available in several places:

  1. Right-click on the Compilation Unit (Java file) in the Package Explore of the Java Perspective.
  1. Right-click on the members in the Outline pane of the Java Perspective.
  1. Right-click on the Java source code in the Java editor of the Java Perspective.
  1. Click the blue plus icon on the table view tool bar - Note that the review issue entered by this will not be associated with a file so that you can not use jump function. Instead, this will be used for the review comments that concern design level such as system design, document, and so forth.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PopUpMenu.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/PopUpMenu.jpg)

For example, let's try to pick some area in a Java source code. select a text region there, right-clck and select "Add Jupiter Issue". The small text at the top of the window identifies that this issue has been raised by "kagawaa", the file that the issue is associated with, and the line number. If you select a region of the source code, the selected region is copied to the "Description" field.  Note that the "Type" and "Severity" field are required.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorIndividual.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/EditorIndividual.jpg)

The type field is used to identify the type of the review. At this point, whatever type you select is tentative--the final decision about whether an issue is really a "defect" or not (for example) will be made during the Team Review Phase. So just make your best guess for now.

The severity field is used to identify or prioritize the severity of this issue. For example, you can set severity as "Trivial" for a coding standard violation such as using variable name as "msg" (This should be corrected as "message").

The description field gives the area in which you want to comment something. Since you select a region before right-clicking on the source code, the selected part is automatically copied to the description field. After filling out the necessary information, Click the save Icon in the right upper side of the window to save the issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Marker.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/Marker.jpg)

After saving the review issue, you can see the purple marker in the editor ruler, which indicates that there is an issue associated with this region of the file.

Finally, don't forget to commit your .review  file to version control. The file is located in the directory which was specified during the configuration phase.