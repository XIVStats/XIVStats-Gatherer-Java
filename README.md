# XIV Stats Gatherer (Java) #

[![Build Status](https://mygitlab.org:4043/buildStatus/icon?job=XIVStats-Gatherer-Java)](https://mygitlab.org:4043/job/XIVStats-Gatherer-Java/)
[![Tests Status](https://img.shields.io/jenkins/t/https/mygitlab.org:4043/XIVStats-Gatherer-Java-Tests.svg)](https://mygitlab.org:4043/job/XIVStats-Gatherer-Java-Tests/lastBuild/testReport/)
[![codecov.io](https://codecov.io/github/XIVStats/XIVStats-Gatherer-Java/coverage.svg?branch=master)](https://codecov.io/github/XIVStats/XIVStats-Gatherer-Java?branch=master)

XIVStats Gatherer Java is a multi threaded Java program with the purpose of
fetching a set of character profiles from the
[Final Fantasy XIV Lodestone](http://eu.finalfantasyxiv.com/lodestone/), and
then parse the content of the character profile page before passing it into a
database (MySQL or PostgreSQL).

To see an example of data gathered using this script see
[FFXIVCensus.com](http://ffxivcensus.com/). The database generated by this program
can be used in conjunction with the [XIVStats PHP Script(s)](https://github.com/xivstats/xivstats)
to produce a web page displaying statistics for the data gathered.

## Table of contents
* [Quick start](#quick-start)
* [Bugs and feature requests](#bugs-and-feature-requests)
* [Documentation](#documentation)
* [Contributing](#contributing)
* [Database](#database)
* [XIVStats-Gatherer-Ruby](#xivstats-gatherer-ruby)
* [Creators](#creators)
* [Copyright and license](#copyright-and-license)

## Quick Start
Follow these steps to setup XIVStats-Gatherer-Java:

### 1. Database setup
  1. Setup your own [MySQL](https://dev.mysql.com/doc/refman/5.1/en/installing.html)
   or [PostgreSQL](https://wiki.postgresql.org/wiki/Detailed_installation_guides)
  server instance (if you have not already done so).
  2. Setup a database to store the program data in:
    ```sql
    
    CREATE DATABASE dbplayers;
    ```
  3. Create a user for the program to use to connect to the database.

  **Replace {password} with your choice of password, take a note of this for later.**
  ```sql
  GRANT ALL PRIVILEGES ON dbplayers.* TO `xivstats`@`localhost` IDENTIFIED BY '{password}';
  ```

### 2. Program Setup
  1. Install the latest version of either [OpenJDK 8 JRE](http://openjdk.java.net/install/)
  or [Oracle JRE ](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
  (if you have not already done so).
  2. Download the latest release from the [releases page](https://github.com/XIVStats/XIVStats-Gatherer-Java/releases).
  3. Extract the zip to the directory you wish to install to.
  4. If this is your first time setting up the program - rename the file ```config_example.xml```
  to ```config.xml```.
  5. Open ```config.xml``` in editor of choice.
  6. Configure the options as follows
      * Set the ***url*** parameter to the URL of your SQL server instance, MySQL default: ```mysql://localhost:3306```, PostgreSQL default: ```postgresql://localost:5407```.
      * Set the ***database*** parameter to the database you configured earlier (```dbplayers```).
      * Set the ***username*** parameter to the username you configured earlier (```xivstats```).
      * Set the ***password*** parameter to the password you configured earlier.
      * Set the ***threads*** parameter to the number of threads you want the program to utilize (more threads = faster gatherer crawls). At present there is a safety limit of 64.
  7. Save and close ```config.xml```.
  8. Using a shell (or CMD on windows) run the following command (replace
    {words in brackets} with integer parameters):
  ```shell
  java -jar XIVStats-Gatherer-Java.jar {lowest character id to fetch} {highest character id to fetch}
  ```

  Note: On Linux/Unix it is advised to run the program in Tmux/Screen or similar.

## Bugs and feature requests

If you have discovered a bug, are having issues running the program
(and Google hasn't been any immediate help), or you would like to request a feature
[open an issue over here](https://github.com/XIVStats/XIVStats-Gatherer-Java/issues).

## Documentation
Javadoc documentation for the program can be found [here](http://download.reidweb.com/xivstats/doc/master/index.html).

## Contributing
If you want to contribute to the XIVStats-Gatherer-Java project, please fork this
repository, make the changes you want to make and commit them then
[open a pull request](https://github.com/XIVStats/XIVStats-Gatherer-Java/pulls)
**describing clearly what you are adding**. All feature additions, bug fixes and
other positive contributions are welcome.

All pull requests are subject to contributor review before passing, all build and
test CI stages must also pass before a contribution can be merged.

## Database
The database table ```tblplayers``` has the following structure:

|Column Name           |Datatype |Checks for Mount/Minion         |
|:--------------------:|:-------:|:------------------------------:|
|id                    |int      |N/A                             |
|name                  |text     |N/A                             |
|realm                 |text     |N/A                             |
|gender                |text     |N/A                             |
|grand_company         |text     |N/A                             |
|level_gladiator       |int      |N/A                             |
|level_pugilist        |int      |N/A                             |
|level_marauder        |int      |N/A                             |
|level_lancer          |int      |N/A                             |
|level_archer          |int      |N/A                             |
|level_rogue           |int      |N/A                             |
|level_conjurer        |int      |N/A                             |
|level_thaumaturge     |int      |N/A                             |
|level_arcanist        |int      |N/A                             |
|level_darkknight      |int      |N/A                             |
|level_machinist       |int      |N/A                             |
|level_astrologian     |int      |N/A                             |
|level_carpenter       |int      |N/A                             |
|level_blacksmith      |int      |N/A                             |
|level_armorer         |int      |N/A                             |
|level_goldsmith       |int      |N/A                             |
|level_leatherworker   |int      |N/A                             |
|level_weaver          |int      |N/A                             |
|level_alchemist       |int      |N/A                             |
|level_culinarian      |int      |N/A                             |
|level_miner           |int      |N/A                             |
|level_botanist        |int      |N/A                             |
|level_fisher          |int      |N/A                             |
|p30days               |bit      |Minion - Wind-up Cursor         |
|p60days               |bit      |Minion - Black Chocobo Chick    |
|p90days               |bit      |Minion - Beady Eye              |
|p180days              |bit      |Minion - Minion Of Light        |
|p270days              |bit      |Minion - Wind-up Leader         |
|p360days              |bit      |Minion - Wind-up Odin           |
|p450days              |bit      |Minion - Wind-up Goblin         |
|p630days              |bit      |Minion - Wind-up Nanamo         |
|p960days              |bit      |Minion - Wind-up Firion         |
|prearr                |bit      |Minion - Cait Sith Doll         |
|prehw                 |bit      |Minion - Chocobo Chick Courier  |
|artbook               |bit      |Minion - Model Enterprise       |
|beforemeteor          |bit      |Minion - Wind-up Dalamud        |
|beforethefall         |bit      |Minion - Set Of Primogs         |
|soundtrack            |bit      |Minion - Wind-up Bahamut        |
|saweternalbond        |bit      |Minion - Demon Box              |
|sightseeing           |bit      |Minion - Fledgling Apkallu      |
|arr_25_complete       |bit      |Minion - Midgardsormr           |
|comm50                |bit      |Minion - Princely Hatchling     |
|moogleplush           |bit      |Minion - Wind-up Delivery Moogle|
|hildibrand            |bit      |Minion - Wind-up Gentleman      |
|ps4collectors         |bit      |Minion - Wind-up Moogle         |
|dideternalbond        |bit      |Mount - Ceremony Chocobo        |
|arrcollector          |bit      |Mount - Coeurl                  |
|kobold                |bit      |Mount - Bomb Palanquin          |
|sahagin               |bit      |Mount - Cavalry Elbst           |
|amaljaa               |bit      |Mount - Cavalry Drake           |
|sylph                 |bit      |Mount - Laurel Goobbue          |
|hwcomplete            |bit      |Mount - Midgardsormr            |
|hw_31_complete        |bit      |Minion - Wind-up Haurchefant    |
|legacy_player         |bit      |Mount - Legacy Chocobo          |
|*mounts*              |*text*   |*N/A*                           |
|*minions*             |*text*   |*N/A*                           |

*Italicised fields are only completed jf specified with a command line flag.*

## XIVStats-Gatherer-Ruby
XIVStats-Gatherer-Java provides the same functionality as the original ruby-based  [XIVStats-Gatherer-Ruby](https://github.com/XIVStats/XIVStats-Gatherer-Ruby),
but is written in Java to make use of Multi-threading capabilities that could
not be harnessed in Ruby. This allows XIVStats-Gatherer-Java to perform large
crawl operations in a much shorter period of time, utilizing only one application
instance. XIVStats-Gatherer-Java also brings with it the benefit of being able
to use a full SQL setup as opposed to a sqlite file, giving the advantage of
being able to perform asynchronous database transactions.

## Creators
**Peter Reid (Project Maintainer)**
* [Website](https://reidweb.com)
* [GitHub](https://github.com/reidweb)

**Jonathan Price (XIVStats and XIVStats-Gatherer-Ruby)**
* [Website](https://jonathanprice.uk)
* [GitHub](https://github.com/pricetx)

## Copyright and license
Code and documentation copyright 2015-2016 Jonathan Price & Peter Reid, Code
and documentation released under the [BSD 2-Clause "Simplified" License](https://github.com/XIVStats/XIVStats-Gatherer-Java/blob/master/LICENSE).
