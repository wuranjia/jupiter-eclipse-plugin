# Jupiter Installation Guide #
[Takuya Yamashita](http://csdl.ics.hawaii.edu/Members/TakuyaYamashita.html) [Hongbing Kou](http://csdl.ics.hawaii.edu/~hongbing)

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

## Content ##
  1. [Introduction](JupiterUserGuide.md)
  1. _Installation_
  1. [Review Perspective](ReviewPerspective.md)
  1. [The Jupiter Review Process](ReviewProcess.md)
  1. [Review ID configuration defaults](ReviewConfiguration.md)
  1. [Configuration Phase](ReviewConfiguration.md)
  1. [Individual Review Phase](IndividualReviewPhase.md)
  1. [Team Review Phase](TeamReviewPhase.md)
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)

# Installation #
## Installing Jupiter Using the Jupiter Update Site ##
The best way to install Jupiter is to use the Software Update mechanism in Eclipse. This feature enables you to install or update Jupiter easily when updates to the package occur.

Select "Help | Software Updates | Find and Install..", select the "Search for new features to install" option, and then add the update site by clicking on the "Add Update Site" button. Provide a name such as "Jupiter Update Server" and use the URL http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/site/.

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/NewSite.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/NewSite.jpg)

Check the newly created Jupiter site, then follow the wizards to complete the installation process.

## Installing Jupiter From Downloaded Zipfile ##
These instructions assume that you have unzipped the zip file to create a directory containing the plug-in components.

  1. Please make sure the following are done before the installation process:
    1. The **Java 5 or above level Java runtime (JRE) or Java development kit (JDK) is installed** on your environment.  If you are using a version prior to 1.5, install 1.5 or above and specify the "vm" option to start up your eclipse with the 1.5.
      1. Your Eclipse version is suitable for Jupiter version downloaded.  The zipfile should contain more detailed version information.  Please note that officially Jupiter supports Eclipse 3.3 and above (although it should work with 3.2.2).
      1. Your Eclipse is shut down.
  1. Copy both ./plugins/edu.hawaii.ics.csdl.jupiter\_version and ./features/edu.hawaii.ics.csdl.jupiter\_version into 

<installed\_eclipse\_dir>

/plugins and 

<installed\_eclipse\_dir>

/features respectively (NOTE: an example of a version number is 3.2.1).  The ./features/edu.hawaii.ics.csdl.jupiter\_version helps you to configure the Jupiter plug-in by allowing it to be disabled and/or uninstalled gracefully. Otherwise, some errors may occur because the Jupiter related view and action buttons would be still stored in the workbench just after the Jupiter plug-in deletion.
  1. Start your Eclipse.
  1. Show "Open Jupiter Issue View" button in either way:
    1. Select "Window | Reset Perspective" in the main tool bar.
    1. Select "Window | Customize Perspective", check "Jupiter" (the check box is located in "Commands" tab).
  1. Click the button that appeared to open Jupiter Issue View.

# Uninstallation #
To unistall the Jupiter plug-in, please follow the instructions carefully.  Otherwise errors may occur (these errors will be shown in the error log of Eclipse).

  1. **Make sure all Jupiter related extensions are closed:**
    1. **Close Jupiter view** by unchecking "Jupiter" in the "Window | Customize Perspective | Window > Show View ".
    1. **Close Jupiter action** button by unchecking "Jupiter" in the "Window | Customize Perspective | Other".
  1. Select "Help | Software Updates | Manage Configuration...", click the installed "Jupiter Feature..." by traversing "Eclipse Platform | file:...".
  1. Select the "Disable" link and follow the instructions then restart Eclipse.
  1. Select "Help | Software Updates | Manage Configuration...", click "Show disable features" button in the third icon from the left of the main icon bar in order to see the disabled Jupiter plug-in.
  1. Select the disabled Jupiter plug-in and click the "Uninstall" link, follow the given instructions, and restart Eclipse.












