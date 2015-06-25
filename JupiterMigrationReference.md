# 1.0 Introduction #
This guide presents instructions to follow when moving from Jupiter that was downloaded from the CSDL (http://csdl.ics.hawaii.edu/Tools/Jupiter/Download/) to Jupiter that is hosted here on this site.

The reason why this guide is needed is because during the transition from CSDL to Google hosting the Jupiter plugin went through some refactoring.  If you do not remove the older CSDL version before installing the versions on Google hosting you will end up having two Jupiter instances running at once.

# 2.0 Migration Steps #

## 2.1 Uninstall the Old Jupiter ##
To unistall the Jupiter plug-in, please follow the instructions carefully. Otherwise errors may occur (these errors will be shown in the error log of Eclipse).

  1. Make sure all Jupiter related extensions are closed:
    1. Close the Jupiter perspective if it is selected perspective by going to "Window | Close Perspective".  If the Jupiter perspective is open but not selected you can select it and then follow the directions above.
  1. Select "Help | Software Updates | Manage Configuration...", click the installed "Jupiter Review Plugin..." by traversing "Eclipse SDK | file:...".
  1. Select the "Disable" link and follow the instructions then restart Eclipse.
  1. Select "Help | Software Updates | Manage Configuration...", click "Show disabled features" button in the third icon from the left of the main icon bar in order to see the disabled Jupiter plug-in.
  1. Select the disabled Jupiter plug-in and click the "Uninstall" link, follow the given instructions, and restart Eclipse.

_Note: You maybe be able to just select Uninstall.  If the uninstall option is available during step 3 you may select it and Eclipse will automatically disable and uninstall it at once._

## 2.2 Install Jupiter from Google ##
Refer to Section 2.2 of the UserGuide for installation instructions.