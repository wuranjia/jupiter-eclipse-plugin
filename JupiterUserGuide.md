# Jupiter User's Guide #
[Takuya Yamashita](http://csdl.ics.hawaii.edu/Members/TakuyaYamashita.html) [Hongbing Kou](http://csdl.ics.hawaii.edu/~hongbing)

[Collaborative Software Development Laboratory](http://csdl.ics.hawaii.edu/)

Department of Information and Computer Sciences

University of Hawaii

![http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JupiterThumbnail.jpg](http://jupiter-eclipse-plugin.googlecode.com/svn/trunk/doc/images/JupiterThumbnail.jpg)

## Content ##
  1. _Introduction_
  1. [Installation](InstallationGuide.md)
  1. [Review Perspective](ReviewPerspective.md)
  1. [The Jupiter Review Process](ReviewProcess.md)
  1. [Review ID configuration defaults](ReviewConfiguration.md)
  1. [Configuration Phase](ReviewConfiguration.md)
  1. [Individual Review Phase](IndividualReviewPhase.md)
  1. [Team Review Phase](TeamReviewPhase.md)
  1. [Rework Phase](ReworkPhase.md)
  1. [Preference filter configuration](PreferenceFilter.md)
  1. [Item Reference](ItemReference.md)

# Introduction #
## Why use a review tool? ##
Why should you use a review tool? There are a few reasons:

  1. Reviews can find many faults during requirements, design and coding;
  1. Early detection of errors can improve productivity;
  1. Review helps members of the team learn about other parts of the system, and can increase their overall programming skill.
Both review and testing are complementary and should be a part of a high quality software development process.  Many studies have shown the effectiveness of review for error detection:

  * _"Fagan (1976) performed an experiment in which 67 percent of the system's faults eventually detected were found before unit testing using inspections."_
  * _"Ackerman, Bushwald, and Lewski (1986) noted that 93 percent of all faults in a 6000-line business application were found by inspections."_
  * _"Weller (1993) examined data from three years of inspections at Bull Information Systems. Measurements from almost seven thousand inspection meeting included information about 11,557 faults and 14,677 pages of design documentation."_
> > _Shari Lawrence Pfleeger, "Software Engineering : theory and practice", 2nd ed._

While most forms of review will detect at least some errors, overall effectiveness will vary depending upon the approach and objectives.  The goal of the Jupiter project is to provide a simple and convenient approach to code review for users of the Eclipse integrated development environment.

## Why use Jupiter? ##
Some of the features of Jupiter include:

  1. **open source** - Jupiter uses the CPL License.  As of June 2007 all new versions of Jupiter will carry the Apache 2.0 License.
  1. **free** - Jupiter is distributed free of charge.
  1. **IDE integration** - Jupiter is based upon the Eclipse plug-in architecture.
  1. **Cross-platform** - Jupiter is available for all platforms supported by Eclipse.
  1. **XML data storage** - Jupiter stores data in XML format to simplify use and re-use.
  1. **CM repository** - Users of Jupiter share their data files the same way they share their code--using CVS or some other CM repository.
  1. **Sorting and searching** - Jupiter provides filters and sorting to facilitate issue review.
  1. **File integration** - Jupiter supports jumping back and forth between reviews and source code.

## History of Jupiter ##
Jupiter is the result of over ten years of research of software review tools and techniques by the Collaborative Software Development Laboratory at the University of Hawaii. In the early 1990's, we developed CSRS (Collaborative Software Review System). CSRS provided sophisticated support for software review, including a configurable review process modeling language, a back-end hypertext database for persistency, an Emacs-based user interface, and fine-grained metrics collection.  While extremely sophisticated, CSRS was complicated to install, use, and maintain, all of which hindered its adoption.  In the late 1990's, we developed a much simpler code review system as part of the Leap toolkit for software engineering measurement and analysis. This tool was Java-based and independent of any editor.  While much simpler to use, its lack of integration with a software development environment made it less functional and created more overhead for users.  Jupiter is our third generation approach.  With Jupiter, we are exploiting the Eclipse IDE framework to provide highly usable code annotation that is much simpler to install and use than CSRS.  Jupiter implements a very simple, lightweight "process" for code review that should suffice for most users.  Rather than incur the overhead of a back-end database for persistency, which we learned will greatly hinder adoption, Jupiter stores review comments in simple XML files which developers are responsible for managing and sharing via their configuration management system. In other words, Jupiter files must be managed and shared just as source code files must be managed and shared.  Finally,  rather than build metrics collection and analysis directly into Jupiter, we developed a Jupiter sensor for the Hackystat software engineering measurement system.  The sensor allows review metrics to be unobtrusively collected and sent to the user's account at a Hackystat server, where it can be combined with other software engineering metrics collected for this user and their development group.   Our hope is that the design of Jupiter hits a "sweet spot" in the many trade-offs that must be made between functionality and usability in code review, one that makes it useful to large segment of the software engineering community.





