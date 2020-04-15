
# Changelog

## Version 2.11 and newer

See [GitHub releases](https://github.com/jenkinsci/dashboard-view-plugin/releases)

## Version 2.10 (2018/11/11)

(Needs Jenkins 1.625.3 and Java 7)

-   Modernizes dependencies, update joda-time to latest version
-   Don't depend on maven-plugin anymore
-   Fix fullscreen view on newer Jenkins versions (Thanks to
    eva-mueller-coremedia, NeverOddOrEven and szhem for providing
    pull-requests fixing this issue, fixes
    [JENKINS-39941](https://issues.jenkins-ci.org/browse/JENKINS-39941),
    )
-   Fix health percentage cutoff (Thanks to zizizach for noticing this)
-   Alternate percentages when near 0/100% (Provided by Arnaud
    TAMAILLON)
-   Apply full screen style to maximized portlets
-   Based on
    [JENKINS-13687](https://issues.jenkins-ci.org/browse/JENKINS-13687):
    Add config button to full screen (fixes
    [JENKINS-52170](http://jenkins-52170/), thanks to jjasper for the
    inspiration)
-   [JENKINS-49745](https://issues.jenkins-ci.org/browse/JENKINS-49745):
    Better JavaScript error handling in "Agent statistics" portlet  

## Version 2.9.11 (2017/06/07)

(Needs Jenkins 1.580.1)

-   [JENKINS-43863](https://issues.jenkins-ci.org/browse/JENKINS-43863):
    Bump Jenkins version and include needed dependencies.

## Version 2.9.10 (2016/06/01)

-   Bugfix: Ensure that ${jobs} is really a Collection\<Job\>
-   Minor cleanups
-   [JENKINS-35266](https://issues.jenkins-ci.org/browse/JENKINS-35266):
    Fix reset of Hide projects with no tests in the test portlet

## Version 2.9.9 (2015/05/17)

-   Hide inactive matrix configurations from the Test Statistics Grid
    (JENKINS-34804)
-   Add option to Test Statistics Portlet to allow jobs with 0 tests to
    be hidden. (JENKINS-33952)

## Version 2.9.7 (2015/12/19)

-   Fixed icons missing in build statistics portlet
    ([JENKINS-29690](https://issues.jenkins-ci.org/browse/JENKINS-29690))
-   Fixed test result trend not displayed in matrix project
    configuration top page
    ([JENKINS-12205](https://issues.jenkins-ci.org/browse/JENKINS-12205))
-   Optimized job statistics portlet
    ([JENKINS-26879](https://issues.jenkins-ci.org/browse/JENKINS-26879))
-   Add an option to show in full screen

## Version 2.9.6 (2015/07/13)

-   Do not rely on relative links
    ([JENKINS-28860](https://issues.jenkins-ci.org/browse/JENKINS-28860))

## Version 2.9.5 (2015/06/18)

-   Use request based URLs
-   Fixed "Starting jobs returns error 'Form post required'"
    ([JENKINS-20025](https://issues.jenkins-ci.org/browse/JENKINS-20025))

## Version 2.9.4 (2014/07/29)

-   Fix CCE which causes the Unstable jobs portlet to blank out if you
    have any Maven Job type projects.
-   Fix duplicate entries when using the Unstable jobs portlet with
    recursion enabled for the view
-   Fix the build link on the Jobs Grid

## Version 2.9.3 (2014/07/14)

-   Release fails with recent git client
-   Fixed issue where dashboard view would not work inside a folder. If
    a view was created inside a folder, the jobLink would fail to
    properly provide the correct url
    ([pull/24](https://github.com/jenkinsci/dashboard-view-plugin/pull/24)).
-   Ensure that ${jobs} is really a Collection\<Job\>, with no Folder
    for example
    ([JENKINS-21578](https://issues.jenkins-ci.org/browse/JENKINS-21578),
    [pull/23](https://github.com/jenkinsci/dashboard-view-plugin/pull/23)).
-   Fixed unstable job portlet to properly display failing or unstable
    jobs inside folders
    ([pull/22](https://github.com/jenkinsci/dashboard-view-plugin/pull/22)).
-   TestStatisticsPortlet config.jelly: the reference to the instance of
    the portlet was using the wrong syntax
    ([pull/21](https://github.com/jenkinsci/dashboard-view-plugin/pull/21)).

## Version 2.9.2 (2013/10/27)

-   No changes (!?)

## Version 2.9.1 (2013/8/16)

-   Upgrading to dashboard-view 2.9 causes Jobs Grid information to not
    be displayed
    ([JENKINS-19219](https://issues.jenkins-ci.org/browse/JENKINS-19219)).

## Version 2.9 (2013/8/13)

-   Added configuration to JobsPortlet: Number of column and job filling
    direction.
-   Added Timeshift-Option for Test-Trend-Chart
    ([JENKINS-15814](https://issues.jenkins-ci.org/browse/JENKINS-15814)).
-   Build statistics does not display result with regex .\* filter
    ([JENKINS-18880](https://issues.jenkins-ci.org/browse/JENKINS-18880)).
-   Added Number of columns and Fill column first parameters
    ([pull/10](https://github.com/jenkinsci/dashboard-view-plugin/pull/10)).
-   Awful performance from Latest Builds portlet
    ([JENKINS-18861](https://issues.jenkins-ci.org/browse/JENKINS-18861)).

## Version 2.8 (2013/7/9)

-   Avoid loading too many build records for display jobs
    ([JENKINS-15858](https://issues.jenkins-ci.org/browse/JENKINS-15858)).
-   Fix test trend chart for TestNG test results.
-   Sort list of available dashboards alphabetically
    ([JENKINS-6289](https://issues.jenkins-ci.org/browse/JENKINS-6289)).

## Version 2.7 (2013/6/27)

-   Dashboard View plugin v2.6 broke URL path to build stability
    images([JENKINS-18049](https://issues.jenkins-ci.org/browse/JENKINS-18049)).

## Version 2.6 (2012/05/19)

TBC

## Version 2.5 (2012/03/23)

-   Added possibility to sort the test statistics grid portlet results
    [JENKINS-16943](https://issues.jenkins-ci.org/browse/JENKINS-16943)
-   Added iframe portlet
-   Removed configure link from dashboard

## Version 2.4 (2012/11/12)

-   Fixed null pointer exception in TestTrendChart portlet
    [JENKINS-14522](https://issues.jenkins-ci.org/browse/JENKINS-14522)
-   Showing Jenkins pop-up menu when hovering on the job link
    [JENKINS-15294](https://issues.jenkins-ci.org/browse/JENKINS-15294)
-   Added optional colours to TestStatisticChart (improvement provided
    by user [jake-stacktrace](https://github.com/jake-stacktrace))
-   Added support for folders in view (provided by user
    [ndeloof](https://github.com/ndeloof))

## Version 2.3 (2012/07/17)

-   Added image portlet: allows to display an image given the URL
-   Modified "Test Trend Chart" portlet: allow to select what type of
    tests to display (All tests, Only successful, Only failed, Only
    skipped)
    [JENKINS-12126](https://issues.jenkins-ci.org/browse/JENKINS-12126)
-   Modified "Test Trend Chart" portlet: modified x-axis label of test
    trend to use only day-month

## Version 2.2 (2012/01/11)

-   Compatibility with [Extension Point for Project
    Navigation](https://wiki.jenkins.io/display/JENKINS/Extension+Point+for+Project+Views+Navigation)

## Version 2.1 (2011/10/08)

-   Added patch provided by Matt
    [JENKINS-9559](https://issues.jenkins-ci.org/browse/JENKINS-9559)
-   Added timestamp sorting in LatestBuilds view
    [JENKINS-9606](https://issues.jenkins-ci.org/browse/JENKINS-9606)
-   TestTrendChart was reporting tests contribution in the past;
    modified to contribute to the future
    [JENKINS-10529](https://issues.jenkins-ci.org/browse/JENKINS-10529)
-   Fixed visualization when a date range is provided
    [JENKINS-10529](https://issues.jenkins-ci.org/browse/JENKINS-10529)
-   Fixed issue with test trend chart is empty with maven2 projects
    [JENKINS-7099](https://issues.jenkins-ci.org/browse/JENKINS-7099)
    (fix provided by user [larrys](https://github.com/larrys))
-   Add new statistics portlet "Slaves statistics" which displays count
    of slaves, offline slaves, online slaves, disconected slaves, tasks
    in a queue and running jobs (provided by user
    [lvotypko](https://github.com/lvotypko))

## Version 2.0.2

-   Renamed hudson to jenkins.

## Version 2.0.1 (2011/03/07)

-   Supported near 100 and near 0 percents in test statistics grid
    [JENKINS-7913](https://issues.jenkins-ci.org/browse/JENKINS-7913)
-   Modified relative to absolute URL image for portlet collapse/expand
    icons
    [JENKINS-8956](https://issues.jenkins-ci.org/browse/JENKINS-8956)
-   Improved the usability of show/hide portlet; using new icons from
    jenkins theme
    [JENKINS-8624](https://issues.jenkins-ci.org/browse/JENKINS-8624)\]
-   Added build button to Unstable and Grid Jobs portlets
    [JENKINS-7569](https://issues.jenkins-ci.org/browse/JENKINS-7569)

## Version 2.0 (2011/02/11)

-   Modified for github and Jenkins.
-   Allow to hide/show portlets directly from the dashboard (without
    having to go to the configuration page); see
    [JENKINS-7465](https://issues.jenkins-ci.org/browse/JENKINS-7465)
    and
    [JENKINS-8623](https://issues.jenkins-ci.org/browse/JENKINS-8623)
    (fix provided by [Godin](https://github.com/Godin))
-   Allow to specify a range of dates for TestTrendGraph portlet (use 0
    to auto-adjust the range)
    [JENKINS-7596](https://issues.jenkins-ci.org/browse/JENKINS-7596)
-   Supported near 100 and near 0 percent in test statistics grid [issue
    \#7913](http://issues.jenkins-ci.org/browse/JENKINS-7913) (missing
    in this version)
-   Fixed bug
    [JENKINS-7595](https://issues.jenkins-ci.org/browse/JENKINS-7595)

## Version 1.8.2 (01/20/2011)

-   Added Japanese translation.

## Version 1.8.1 (09/16/2010)

-   Allow to configure standard view parameters for Dashboard View
    [JENKINS-6618](https://issues.jenkins-ci.org/browse/JENKINS-6618)

## Version 1.8 (09/15/2010)

-   Added some italian translations
-   The configuration page has been re-organized
-   Allow to specify width of left and right columns in dashboard view
    (percentage or fixed width)
    [JENKINS-7429](https://issues.jenkins-ci.org/browse/JENKINS-7429)

## Version 1.7 (08/12/2010)

-   Show job description tooltip and build description tooltip
    [JENKINS-7107](https://issues.jenkins-ci.org/browse/JENKINS-7107)
-   Allow parametrization of graph width and height for 'Test Trend
    Graph' Portlet
    [JENKINS-6811](https://issues.jenkins-ci.org/browse/JENKINS-6811)
-   Allow parametrization of number of builds used for 'Latest Builds'
    portlet
    [JENKINS-7090](https://issues.jenkins-ci.org/browse/JENKINS-7090)
-   Keep 'standard' order for jobLink: build status - health status -
    job name
    [JENKINS-6611](https://issues.jenkins-ci.org/browse/JENKINS-6611)
-   Added i18n to some parts of the plugin

## Version 1.6 (05/20/2010)

-   Added health icon in custom jobLink
-   Added 'title' attribute to img tags
-   Added default name on portlet add
    [JENKINS-5952](https://issues.jenkins-ci.org/browse/JENKINS-5952)
-   Added unique id to each portlet (within the same dashboard/view)
    [JENKINS-6118](https://issues.jenkins-ci.org/browse/JENKINS-6118)

## Version 1.5 (04/02/2010)

-   Fixed 'Last 10 builds' showing always bullet from last build
    [JENKINS-6091](https://issues.jenkins-ci.org/browse/JENKINS-6091)
-   Fixed 'Last 10 builds' doesn't filter jobs
    [JENKINS-6106](https://issues.jenkins-ci.org/browse/JENKINS-6106)
-   Allowing to hide disabled jobs when not using regexp

## Version 1.4 (03/25/2010)

-   Fixed standard Jenkins list view visualization problem

## Version 1.3 (03/05/2010)

WARNING: if upgrading from a previous release, all the dashboard views
must be edited and saved (also without modifying anything) to correctly
work with this version.

-   Layout: added top portlets (portlets that spawn the entire page
    width) and are shown above the left and right portlets
    [JENKINS-5759](https://issues.jenkins-ci.org/browse/JENKINS-5759)
-   New portlets:
    -   standard Jenkins jobs list
        [JENKINS-5691](https://issues.jenkins-ci.org/browse/JENKINS-5691)
    -   Jobs statistics
    -   Build statistics
-   Added option to show standard Jenkins jobs list before displaying
    all the portlets
    [JENKINS-5814](https://issues.jenkins-ci.org/browse/JENKINS-5814)

## Version 1.1 (11/15/2009)

-   Fixed issue with Unstable Jobs portlet where content was empty if
    builds were being actively run
-   New option on regular expression job inclusion to prevent disabled
    jobs from appearing
-   Supports single column view if only portlets are only specified for
    left or right column

## Version 1.0 (10/11/2009)

-   Initial release
