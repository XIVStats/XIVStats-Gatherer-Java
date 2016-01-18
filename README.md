## XIV Stats ##

[![Build Status](https://mygitlab.org:4043/job/XIVStats-Gatherer-Java/5/badge/icon)](https://mygitlab.org:4043/job/XIVStats-Gatherer-Java/5/)

XIV Stats is primarily a program to produce a database of player
information for FFXIV. The information is pulled from directly from the
lodestone. An example PHP file has been included to demonstrate
visualising the statistics. You can also view a live demo of the example
web page by visiting [ffxivcensus.com/](http://ffxivcensus.com/).

The aim of this project is to allow people to build their own projects
using this data. For example, creating a website to compare players.

If you would rather download a pre-populated database containing every
player (as of 2015-04-23), please see the "Notes" section below.

The project is inspired by [xivsoul.com](https://xivsoul.com).

## Configuration

Before use please configure the following configuration options in 'config.xml'. The script is intended for use with MySQL server, however you can configure to your liking using the url configuration option detailed below.

### Database Configuration
#### Server URL
The url of your SQL server instance (support for sockets coming soon): e.g.

```xml
<url>mysql://localhost:3306</url>
```

#### Database 
The database to create the table in and write records to. Ensure that you have created the database and successfully granted the user specified further on in this config full read/write access to said database.
```xml
<database>dbPlayers</database>
```

#### Username and password
Please provide a username and password with read/write access to the database.
```xml
<username>xivstats</username>
<password>password</password>
```

###Execution configuration
#### Thread Limit
Please specify the number of threads you want to allocate to the program as follows:
```xml
<threads>32</threads>
```

###Notes
Please ensure to place the above configuration options within the xml structure specified in the provided config.xml.

## Usage 

The script will collect information on all players with IDs in the specified
range, as shown below:
```shell
    java -jar gatherer.jar <lowest-id-to-fetch> <highest-id-to-fetch>
```

The java script requires either Oracle or OpenJDK Java Runtime 8 to run.

The player ID can be determined by looking at the URL for the lodestone
profile page of a given player.

A pre-built JAR of the Java application can be found [here](http://download.reidweb.com/xivstats/XIVStats.zip).

## Notes 

The java package relies on the [jsoup](http://jsoup.org/) library, the program will not compile without it. 

A complete copy of the database has been compiled (to a sqlite db) and is avaiable
from the following URLs. 

| Release | Live Patch | Live Expansion | Download |
|---------|------------|----------------|----------|
| April 2015 | 2.5 | A Realm Reborn | [Link](https://jonathanprice.org/xiv/players.db)
| July 2015 | 3.0 | Heavensward | [Link](https://jonathanprice.org/xiv/players-20150801.db) 

A simple example PHP web page has been included. This web page draws data
directly from the database and uses it to draw a few charts. Due to the
large amount of data that has to be compiled to produce the charts,
the page takes a very long time to render (around 5 minutes on my server).
If you were planning to embed data from the database in a web page, it 
would be recommended to compile a static copy of the page, and re-compile
it if you re-scan the lodestone. A simple way to compile the page is shown
below:

    php index.php > index.html

# Data Structure #

All information is stored in a single table called "players".

This table contain the following columns:
- id (derived from the player's lodestone profile URL)
- realm
- player_name
- race
- gender
- grand_company
- level_gladiator
- level_pugilist
- level_marauder
- level_lancer
- level_archer
- level_rogue
- level_conjurer
- level_thaumaturge
- level_arcanist
- level_darkknight
- level_machinist
- level_astrologian
- level_carpenter
- level_blacksmith
- level_armorer
- level_goldsmith
- level_leatherworker
- level_weaver
- level_alchemist
- level_culinarian
- level_miner
- level_botanist
- level_fisher
- 30days (subscription time)
- 60days
- 90days
- 180days
- 270days
- 360days
- 450days
- 630days
- prearr (pre-ordered A Realm Reborn)
- prehw (pre-ordered Heavensward)
- artbook (bought "The Art of Eorzea")
- beforemeteor (bought "Before Meteor" soundtrack)
- beforethefall (bought "Before The Fall" soundtrack)
- soundtrack (bought "A Realm Reborn OST" soundtrack)
- saweternalbond (guest at a ceremony of eternal bonding)
- sightseeing (completed all 80 entries in the ARR sightseeing log)
- arr_25_complete (completed patch 2.5)
- comm50 (attained 50 player commendations)
- moogleplush (bought a moogle plush)
- hildibrand (Completed the 2.5 hildibrand quest line)
- ps4collectors (Bought the PS4 collectors edition for A Realm Reborn)
- hwcomplete (has completed the Heavensward 3.0 story)
- hw_31_complete (has completed the Heavensward 3.1 story)
