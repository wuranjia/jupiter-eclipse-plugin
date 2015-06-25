# Jupiter User's Guide #
Takuya Yamashita

Hongbing Kou

Julie Ann Sakuda

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/JupiterThumbnail.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/JupiterThumbnail.jpg)

# 1.0 Introduction #
Jupiter is an open-source code review tool that has been developed for a very popular IDE called [Eclipse](http://www.eclipse.org).  Jupiter easily plugs into Eclipse and allows for its users to facilitate code reviews on existing code.

# 2.0 Installation #
## 2.1 Prerequisites ##

Jupiter requires Java 5 or newer and Eclipse 3.3 (Europa) or newer.  Because Jupiter depends on team based work it is recommended that the project being reviewed be in a version control system (i.e. CVS or SVN).

## 2.2 Installing Jupiter from the Jupiter Update Site ##

The best way to install Jupiter is by using the Software Update mechanism in Eclipse. This feature enables easy installation and/or updating of Jupiter when new versions are released.

### 2.2.1 Installing Jupiter in Eclipse 3.5 Galileo ###
Select "Help | Install New Software...".  In the dialog that appears, type in the Jupiter Update Site URL (http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/site/) in the "Work with" editable drop down.  Press "Add...".  Another dialog will appear asking for a name for the update site.  Provide a name such as "Jupiter Update Site" and press "OK".  The tree will then populate with the contents of the update site.  Check the desired version and press "Next >".  Follow the wizard to complete the installation process.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSiteGalileo.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSiteGalileo.jpg)

### 2.2.2 Installing Jupiter in Eclipse 3.4 Ganymede ###
Select "Help | Software Updates...".  In the dialog that appears, select the "Available Software" tab.  On the right, press the "Add Site..." button.  Type in the Jupiter Update Site URL (http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/site/) in the box that appears.  Press "OK".  Look for the newly created update site in the tree and expand it to see the Jupiter versions available from the Jupiter Update Site.  Check the desired version and press the "Install..." button on the upper right corner of the "Available Software" tab.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSiteGanymede.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSiteGanymede.jpg)


### 2.2.3 Installing Jupiter in Eclipse 3.2 to 3.3 (and 3.4 with Classic Update turned on) ###

Select "Help | Software Updates | Find and Install..", select the "Search for new features to install" option, and then add the update site by clicking on the "Add Update Site" button. Provide a name such as "Jupiter Update Server" and use the URL http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/site/.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSite.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/NewSite.jpg)

Check the newly created Jupiter site, then follow the wizards to complete the installation process.

## 2.3 Installing Jupiter from downloaded jar file ##

Get the latest Jupiter release from http://code.google.com/p/jupiter-eclipse-plugin/downloads/list.  A jar file with a name similar to `edu.hawaii.ics.csdl.jupiter_<version>.jar` should be available.

Place the downloaded jar file in the eclipse/plugins directory.

Jupiter will be available the next time Eclipse starts up.

## 2.4 Uninstallation ##

To unistall the Jupiter plug-in, please follow the instructions carefully. Otherwise errors may occur (these errors will be shown in the error log of Eclipse).

  1. Make sure all Jupiter related extensions are closed:
    1. Close Jupiter view by unchecking "Jupiter" in the "Window | Customize Perspective | Window > Show View ".
    1. Close Jupiter action button by unchecking "Jupiter" in the "Window | Customize Perspective | Other".
  1. Select "Help | Software Updates | Manage Configuration...", click the installed "Jupiter Feature..." by traversing "Eclipse Platform | file:...".
  1. Select the "Disable" link and follow the instructions then restart Eclipse.
  1. Select "Help | Software Updates | Manage Configuration...", click "Show disabled features" button in the third icon from the left of the main icon bar in order to see the disabled Jupiter plug-in.
  1. Select the disabled Jupiter plug-in and click the "Uninstall" link, follow the given instructions, and restart Eclipse.

# 3.0 Preparing a Review: Review ID Creation #
In order to conduct a review a new review ID must be created.   A review ID consists of a set of files to be reviewed, a set of reviewers, the author of the review session (ID), a review file storage location, item entries, default items, and filters.

## 3.1 Review ID Configuration Defaults ##
NOTE: This step is OPTIONAL.  If the defaults are not edited Jupiter will use the included defaults.

For teams that perform many reviews, it can become tiresome to configure a review ID from scratch each time.  To simplify this process, Jupiter provides a "Master" review ID.  The settings stored in the DEFAULT review ID become the default values when creating new review IDs.

Note that each project has a different DEFAULT review ID (see below), and that the DEFAULT review ID cannot deleted from the review ID list.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/DefaultReviewId.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/DefaultReviewId.jpg)

## 3.2 Adding a New Review ID ##
To add a review ID, right-click on a project in either "Package Explore" or "Navigator" view and select "Properties" to show the property window associated with the project.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ContextMenu.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ContextMenu.jpg)

Selecting 'Review' will bring up the properties for Jupiter.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ConfigProperty.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ConfigProperty.jpg)

Click the "New.." button to open the new Review ID wizard. Fill in the Review ID and Description fields. It is recommended that the Review ID not have any whitespace because it will be used as a part of the review storage file name. Provide a short description of this review in the description field.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewId.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewId.jpg)

The next step is to specify the files to be reviewed. These files will be listed in the jump button of the table view so that reviewers can easily navigate to the files of interest.  Click the "New..." button to open the Review File Selection dialog. Select a set of reviewing files and press "OK".

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewFileSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewFileSelection.jpg)

The set of files selected will now appear in the list of files to be reviewed.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewFileSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewFileSetting.jpg)

The next step is to specify a set of reviewers.  A review file will be created for each of the reviewers participating in the review.  All of the issues generated by each user will be saved to their corresponding review file.  The reviewers specified will also be the ones populated in the "Assigned To" field.

Click the "Add..." button on the page to open the "Add Reviewer" dialog. It is recommended that a reviewer ID have no whitespace. A simple approach is to use the user's first initial followed by their last name (i.e. jsakuda).

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewerId.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewerId.jpg)

There is no limit to the number of reviewers that can be added.  Note that the initial set of reviewers are copied from the DEFAULT review ID during the initial creation of the review ID.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewerListSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewerListSetting.jpg)

The next step is to specify the author of this review ID. The author of this review ID is automatically the Assigned To person in the team phase.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/AuthorSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/AuthorSetting.jpg)

The next step is to define the set of values for the Type, Severity, Resolution, and Status fields which appear throughout the review process.  Jupiter provides default values but it is not required that these be used.  From the drop drown select either Type, Severity, Resolution, or Status and click "New..." to add a new value, "Edit" to edit a value, or "Remove" to delete a value.  The up and down arrows will rearrange the values.  To set the values back to the defaults stored in the DEFAULT review ID press "Restore".

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ItemEntriesSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ItemEntriesSetting.jpg)

The next step configures the default values for Type, Severity, Resolution, and Status.  Each field has a drop down and the value shown will become the default value for that particular field.  This provides the default selection for each field when a new issue is entered in the review editor view.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/DefaultItemsSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/DefaultItemsSetting.jpg)

The next step is to specify the review storage file location. During the review, each issue is stored in an XML file. This setting specifies the location where the XML files should be stored. To customize the directory location, use the "/" (forward slash) as a file separator. For example, to save xml files under the review/sample directory, enter "review/sample".  The directory provided does not need to exist and should be relative to the top level of the project.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/StorageSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/StorageSetting.jpg)

The next step configures filter settings. Each phase has its own filter.  Here are example filters for each phase:

  * Individual Phase
    * Reviewer filter (automatic) - Allows the reviewers to be able to see only their own review issues entered.  This should always be on so reviewers do not read each others issues before the team phase.
  * Team Phase
    * Resolution filter (unset) - Allows a moderator to just focus on the review issues whose resolution is unset. Any resolution except unset will be filtered after the resolution is changed during the team phase.
  * Rework Phase
    * Assigned to filter (automatic) - Allows the assigned persons (in most case, the author of the review ID) to be able to just focus the review issues assigned to them.
    * Status filter (open) - Allows the assigned persons to just focus on the review issues whose status is open. Any status except open will be filtered after the status is changed during the rework phase.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/FilterSetting.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/FilterSetting.jpg)

After all settings are done, click the "Finish" button. The ".jupiter" configuration file is created in the project root.

Finally, don't forget commit the ".jupiter" file to the configuration management system.  After that is done an email announcement can be sent out to the review participants informing them of the review details.

Note: By default Eclipse filters out "." resources from the Package Explorer view.  To turn this of go to the little down arrow on the right side of the view's toolbar an select "Filters...".  In the dialog that appears, uncheck "`.* resources`" and click "OK".  All "." files should now appear in the Package Explorer, including the ".jupiter" file.

## 3.3 Editing an Existing Review ID ##
To edit a review ID right-click on the project in either "Package Explore" or "Navigator" view and select "Properties".  Select "Review" from the left column in the property dialog that appears.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ConfigProperty.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ConfigProperty.jpg)

In the list of existing review IDs, select the review ID that is to be edited and click the "Edit..." button.  The "Review ID Property" dialog will appear.  This dialog allows anything in the review to edited, except for the unique Review ID field.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PropertyEdit.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PropertyEdit.jpg)

Here is a summary of what the tabs in the dialog are for:

  * Review Files - Specifies the files to be reviewed by reviewers. Specifying a set of review files helps to focus the review on a small part of the system.
  * Reviewers - Specifies the reviewers who will examine the review files. The issues generated by each reviewer for a given review ID are stored in their own file.
  * Author - Specifies the author of this review. By default, the review Author is assigned to deal with all of the issues generated during this review.
  * Storage - Specifies the directory where all of the files associated with the review ID will be stored.
  * Item Entries - Specifies the contents of an issue.  The set of types, severities, resolutions, and statuses are all customizable.  Jupiter does include defaults that should be sufficient.
  * Default Items - Specifies the default values for fields when a new issue is created.
  * Filters - Specifies the filters to be applied when displaying issues.  This is particularly useful during the Team and Rework phases.

## 3.4 Removing a Review ID ##
To remove a review ID, select the review ID to be removed and click the "Remove" button. Please note that deleting the review ID causes all related review files to be removed as well.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PropertyRemoval.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PropertyRemoval.jpg)

The following four 'TestReview' related review files will be removed if the review ID is deleted.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/RemoveReviewFiles.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/RemoveReviewFiles.jpg)

# 4.0 Conducting a Review: Individual -> Team -> Rework #
After Jupiter was installed, a purple Jupiter icon should have appeared on the Eclipse toolbar.  This toolbar has a drop that menu that allows for the review phase to be selected.  Switching to the Jupiter perspective will also provide the option of selecting the review phase.

## 4.1 Individual Phase ##
After configuration, it's time to add review issues. First, update the Project to be reviewed from the configuration management system so that the .jupiter file containing the new review ID is retrieved.  Then, select the Jupiter Perspective, and select "Individual Phase" mode.  There should be an icon on the main tool bar (the purple '4', which is the Greek symbol for Jupiter) that drops down to show the Jupiter review phases. Note: if the icon is not available, select "Customize Perspective", then click on the "Commands" label to display the Commands group, then click "Review".

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/IndividualSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/IndividualSelection.jpg)

A dialog will then appear and allow the project to be reviewed, the review ID, and reviewer to be selected.  Selecting the project to be reviewed will populate the review ID drop down with all the review IDs available for that project.  Once the review ID is selected the reviewer drop down will be populated with all the reviewers configured for that particular review ID.  Note: If the desired project is NOT in the drop down, press "Cancel", make sure that the project is open and selected, then select the Individual Phase mode again.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg)

The Jupiter issue view contains the following icons.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ViewToolBar.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ViewToolBar.jpg)

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/JumpIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/JumpIcon.jpg) Jump Icon - Jump to the specific line of source code that the selected issue refers to.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditIcon.jpg) Edit Icon - Edits the selected issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/AddIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/AddIcon.jpg) Add Icon - Adds a new issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/RemoveIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/RemoveIcon.jpg) Remove Icon - Removes the selected issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/FilterIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/FilterIcon.jpg) Filter Icon - Filters the issue list.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PhaseSelectionIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PhaseSelectionIcon.jpg) Phase Selection Icon - Changes the phase that the review is in.  The issue table will refresh to reflect the phase change.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PullDownIcon.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PullDownIcon.jpg) Pull-down Icon - Contains the preference and property settings.

If the author of the review ID specified the files to be reviewed, the files will be listed under the jump icon. Click the small downward triangle icon next to the jump icon and select one of the review files to focus on.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdListSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdListSelection.jpg)

To add a review issue entry, click on "Add Jupiter Issue", which is available in several places:

  1. Right-click on the Compilation Unit (Java file) in the Package Explore of the Java Perspective.
  1. Right-click on the members in the Outline pane of the Java Perspective.
  1. Right-click on the Java source code in the Java editor of the Java Perspective.
  1. Click the blue plus icon on the Jupiter issue table tool bar - Note that the review issue entered by this will not be associated with a file.  This should be used only for the review comments that concern issue on the design level such as system design, documents, and so forth.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PopUpMenu.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/PopUpMenu.jpg)

To add an issue to a specific line of code, select the line (Eclipse should highlight the line) and right-click.  Select "Add Jupiter Issue".  The issue editor view will populate.  The text at the top of the issue editor shows who generated the issue, what file the issue is in, and the line number it is at.  In the example below, the small text at the top of the view identifies that the issue has been raised by "kagawaa" in the file PostJobAction.java at line 58.  A block of code may also be selected to add a review issue to.  The selected code will be copied to the "Description" field of the issue.  Note that the "Type" and "Severity" fields are required.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorIndividual.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorIndividual.jpg)

The type field is used to identify the type of the review issue.

The severity field is used to identify or prioritize the severity of the issue. For example, a severity such as "Trivial" could be used for a coding standard violation such as using variable name as "msg" (This should be corrected as "message").

The summary is a short overview of the issue.

The description field is for expanding on the summary.  An extended or more detailed explanation should be given.  If a block of code was selected before right-clicking on the source code, the selected part will be in the description field.

After filling out the necessary information, click the Save Icon on the right upper side of the window to save the issue.  Note that issues are automatically saved when the review issue editor loses focus.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Marker.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Marker.jpg)

After saving the review issue, a purple marker will in the editor ruler to the left of the source code.  This indicates that there is an issue associated with that particular region of the file.

Finally, don't forget to commit the generated .review  file to version control. The file is located in the directory which was specified during the configuration phase (the default is the 'review' folder).

## 4.2 Team Phase ##
In this phase, team members review all of the issues that have been generated for a given Project and Review ID as a group.  Before beginning this phase the project should be updated from source control to retrieve all the .review files from those who participated in the Individual Phase.

A well filtered view will save a great deal of time in the group meeting. To begin the Team Review Phase, select the Team Phase from the Jupiter phase selection drop down on the main toolbar.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/TeamSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/TeamSelection.jpg)

After clicking the purple "Team Phase" mode button above, the review ID selection page will pop up. If the correct project is not listed, select "Cancel" and select the project in the "Navigator" or "Package Explore" pane.  Then select the "Team Phase" mode again. After making sure the project name is correct, select the review ID and reviewer ID.  The reviewer ID for the team phase is usually the person that setup the review or the owner of the code that is under review.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg)

To see each issue, click on a row in the Review Table, which will display the corresponding issue in the Review Editor.  If an existing issue is edited, be sure to click the "Save", "Next", or "Previous" button. All three buttons will save the modified issue. Double-clicking on an issue in the Review Table will go to the source code line that the issue refers to.

The Assigned To field contains the author of the review ID as a default, but any member of the review team may be selected.

An important part of the Team Review Phase is to set the "Resolution" field.  This field records the group's consensus regarding the current issue--does it actually need fixing? Is it actually a defect after all?

The Annotation field allows for adding supplemental information about the issue resulting from discussion during the Team Review Phase.

The "Next" and "Previous" yellow arrow icons to move back and forth along the issue list.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorTeam.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorTeam.jpg)

Finally, the "Jump" button on the left side of either the 'Review Editor' view or the 'Review Table' view enables the ability to jump the source code associated with the selected issue.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Jump.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Jump.jpg)

Jupiter issues can also be previewed by using the purple marker to the left of the source code.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Markers.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/Markers.jpg)

Single-clicking on the purple marker on the left hand side of the text editor will bring up a list and one of the entries in that list should be the review issue summary.  Selecting the issue summary pop up a box with the description of the review issue. To see the full information of the review issue, single-click on the review summary.  The 'Review Editor' will be populated with the contents of the selected review issue.

Note that markers obey the current filter settings.  For example, if review issues are filtered to only show those containing "Unset" in the Resolution field, then the marker for an issue will disappear as soon as its Resolution field is changed to another value such as "Valid-NeedsFixing".  This is a good way to keep track of the issues that haven't yet been dealt with during the Team Review Phase.

Finally, don't forget to commit the modified .review files to the configuration management repository at the end of the Team Review Phase. These files are located in the review directory that was specified during the configuration phase.

## 4.3 Rework Phase ##
Following the Team Phase the people that had issues assigned to them during the Team Phase will participate in the Rework Phase.  The purpose of the Rework Phase is to address the issues that were found.  The reviewers with issues assigned to them should focus only on the issues that were delegated to them.

To begin the Rework Phase select "Rework Phase" from the phase selection menu on the toolbar.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReworkSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReworkSelection.jpg)

A dialog will appear and the proper review ID and reviewer ID should be selected.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/ReviewIdSelection.jpg)

In the Rework Phase, the Jupiter 'Review Editor' view contains the status, resolution, and revision fields.

The status field allows the status of the issue to be set.  For example, after the issue is fixed its status can be changed to "Resolved".

The revision field allows the person who fixed the issue to leave a further explanation of what was done to resolve the issue.  Or any other notes can also be recorded.

![http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorRework.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/wiki/images/EditorRework.jpg)

After changes are made commit the .review files to the configuration management repository.  All of those who fixed issues should commit to the repository.  After people have committed anyone can update their review folder to get the updated .review files to see what was done during the Rework Phase.

# 5.0 Legacy Support #

If you require Jupiter for Eclipse 3.2.x you can get it at http://jupiter-eclipse-plugin.googlecode.com/files/csdl.jupiter_3.2.1.zip.  It only requires Java 1.4.