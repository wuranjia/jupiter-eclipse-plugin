_This guide assumes that the user is familiar with the Eclipse IDE._

# 1.0 Introduction #
Jupiter is an open-source code review tool that has been developed for a very popular IDE called [Eclipse](http://www.eclipse.org).  Jupiter easily plugs into Eclipse and allows for its users to faciliate code reviews on existing code.

# 2.0 Installation #
## 2.1 Prerequisites ##

Jupiter requires Java 5 or newer and Eclipse 3.3 (Europa) or newer.  Because Jupiter depends on team based work it is recommended that the project being reviewed be in a version control system (i.e. CVS or SVN).

## 2.2 Installing Jupiter from the Jupiter Update Site ##

The best way to install Jupiter is by using the Software Update mechanism in Eclipse. This feature enables you to install or update Jupiter easily when new versions are released.

Select "Help | Software Updates | Find and Install..", select the "Search for new features to install" option, and then add the update site by clicking on the "Add Update Site" button. Provide a name such as "Jupiter Update Server" and use the URL http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/site/.

Check the newly created Jupiter site, then follow the wizards to complete the installation process.

## 2.3 Installing Jupiter from downloaded jar file ##

Get the latest Jupiter release from http://code.google.com/p/jupiter-eclipse-plugin/downloads/list.  You should get a jar file with a name similar to `edu.hawaii.ics.csdl.jupiter_<version>.jar`.

Place the downloaded jar file in your eclipse/plugins directory.

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
A Review ID needs to be created for each code review.  Follow the following steps to create a new Review ID:

  1. Right-click on the project that will be reviewed and select 'Properties'.
  1. The Properties dialog will appear and you will see a 'Review' option in the list.  Select the 'Review' option.
  1. A table of all the Review IDs available will be shown along with buttons on the right.  Select the 'New...' button to create a new Review ID.
  1. After 'New...' is pressed, a wizard will appear.  The wizard will walk you through the creation of the Review ID.  Two basic things that the wizard allows you to do are configure the files that should be reviewed and define a set of reviewers.

After the Review ID is created the .jupiter file for the project will be updated.  It should be committed to version control.  Everyone participating in the review will then need to update and get the updated .jupiter file.

# 4.0 Conducting a Review: Individual -> Team -> Rework #
After Jupiter was installed, a purple Jupiter icon should have appeared on the Eclipse toolbar.  This toolbar action will allow you to select the appropriate phase of review to be executed.  Switching to the Jupiter perspective will also allow you to select the review phase.

## 4.1 Individual Phase ##
The individual phase is done by all the people participating in the review.  Each person will individually look through the files specified to be reviewed and add code review issues.  To add an issue:
  1. Select the line of code to add an issue to.
  1. Right-click on that line of code and select 'Add Review Issue...'.  This may switch you to the Jupiter perspective if you aren't already in it.
  1. The focus will then be on the Review Editor view.  Fill out the fields and press the floppy disk icon to save it.  Leaving the Review Editor will automatically save the issue.

All issues can be revisited by looking in the Review Table.  Double-clicking on any of the issues will open the associated file up to the line the issue is on and it will also load the issue back into the Review Editor.  Jupiter creates a file for each reviewer in the project's review folder.  Any files created in the review folder should be committed to version control.  The files for all of the reviewers will need to be put into one place for use with the rest of the review phases.

## 4.2 Team Phase ##
After all the review files for all reviewers are in the review folder it is time to move onto the team phase.  Team phase is done as a group.  One person leads the team phase and together all of the code issues found are discussed.

After switching to team phase the Review Table will populate with all the issues found.  Double-clicking on an issue will open up the file it is associated with and show the line the issue is from.  After reviewing the issue, you will then go to the Review Editor and assign the issue to one of the reviewers to be fixed.  You will also set a resolution that deals with the validity of the issue.  An annotation may also be added.

After all the issues have been assigned, the reviewer files will have been updated.  These should be committed to version control.

## 4.3 Rework Phase ##
Each of the reviewers will need now need to update their review folder and get the review files for all the reviewers in preparation for the rework phase.

When entering rework phase Jupiter may prompt for the user.  Just select the correct name and it will switch to rework phase.  In rework phase, each reviewer that was assigned issues in team phase will individually fix the issues assigned to them.  By default, the Review Table will only show the issues assigned to the user that was selected.  To see all issues click the 'Filters...' button above the Review Table.

As always, double-clicking on an issue in the Review Table will open up the file it is associated with and load the issue into the Review Editor.  After you have fixed the issue in the code it is time to change the status of the issue.  In the Review Editor set the status of the issue and add any additional notes.

Any files that were updated in the review folder should be committed to version control.  This will ensure that everyone else can see the changes made to the issues.

# 5.0 Legacy Support #

If you require Jupiter for Eclipse 3.2.x you can get it at http://jupiter-eclipse-plugin.googlecode.com/files/csdl.jupiter_3.2.1.zip.  It only requires Java 1.4.