# Item Reference #
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
  1. [Preference filter configuration](PreferenceFilter.md)
  1. _Item Reference_

# Introduction #
Each issue has five different status. The following is the typical story to use them.

  1. A review issue is open when reviewers post it to Jupiter in the individual phase (Open status).
  1. An assigned person finishes fixing the issue in the rework phase (Resolved status).
  1. The person (team leader, or manager) verifies or approves the resolved issue and closes it (Closed status).
  1. The person (team leader, manager, or anybody) finds the issue (or related problem) takes place again (Reopened status).

# Type #
  * **Unset** - Not set.
  * **Coding Standards**
    * Violating language coding standards - A certain part of implementation does not follow the language or organization coding standards. e.g. Java coding standards, C/C++ coding standards, The elements of Java style, Effective java.
  * **Program Logic**
    * Wrong algorithm - The entire logic in a method is wrong and cannot provide the desired functionality.
    * Wrong expression - An expression (in an assignment or method call) computes the wrong value. A boolean expression is wrong. Objects or their names is confused. The wrong method, attribute, or variable is used.
    * Initialization error - Failure to set a data item, loop variables, initialize pointers, failure to clear flags
    * Calculation error - Outdated constants, wrong parentheses, truncation, incorrect conversion from one
    * Control flow error - Programs runs amunk, stops, loop error.
    * If then else, multiple cases error - Wrong inequalities (e.g.,> instead of >=), Comparison sometimes yields wrong result
  * **Optimization**
    * Lacking optimal design - Design will adversely affect the product's performance.
    * Lacking optimal method - Multiple calls to procedures to be replaced by fewer calls in the code.
    * Lacking optimal usage -
  * **Usability** - Panels, messages, help being reviewed are in error. Their design/implementation will adversely affect usability of the product.
  * **Clarity** - Clarity needed.
    * Clarify design (of required functionality) - Several requirements are designed in one unit (class).
    * Clarify implementation - A certain part of a design is not clearly implemented.
    * For e.g.., If the test cases / comments in source code, variable naming / paragraph naming etc doesn't give clarity to the reviewer.
  * **Missing** - One of the followings is missed.
    * Missing design (of required functionality) - A certain requirement is covered nowhere in the design at all.
    * Missing implementation - A certain part of a design is not implemented at all.
    * Missing error handling - An error case is not handled in the program at all.
    * Missing assignment - A single variable is not initialized or updated at all.
    * Missing call - A single method call is missing.
    * Missing other things. - Other things are missing.
  * **Irrelevant** - One of the followings is irrelevant.
    * Irrelevant design (to required functionality) - A required functionality is designed improperly.
    * Irrelevant implementation - A certain part of a design is implemented improperly.
    * Irrelevant error handing - An error case was handled improperly.
    * Irrelevant assignment - A single variable is initialized or updated improperly.
    * Irrelevant call - A single method is called improperly.
    * Irrelevant other things - Other improper things are found.
  * **Suggestion** - Constructive suggestion.
  * **Other** - Other types that are not fitted above.

# Severity #
  * **Unset** - Not set.
  * **Critical**
  * **Major**
  * **Normal**
  * **Minor**
  * **Trivial**

# Resolution #
  * **Unset** - Not set.
  * **Valid Needs Fixing** - The review issue provides valid argument and it needs to be fixed.
  * **Valid Fix later** - The review issue provides valid argument, but it is to be fixed later.
  * **Valid Duplicate** - The review issue provides valid argument, but it is raised before.
  * **Valid Won't Fix** - The review issue provides valid argument, but it does not need to be fixed.
  * **Invalid Won't Fix** - The review issue provides invalid argument so that it does not need to be fixed.
  * **Unsure validity** - It is not sure that the review issue provides valid or invalid argument.

# Status #
  * **Open** - A new review issue was opened.
  * **Resolved** - The review issue was resolved.
  * **Closed** - The resolved review issue was closed (approved).
  * **Reopened** - The closed review issue was reopened (had problem again).
