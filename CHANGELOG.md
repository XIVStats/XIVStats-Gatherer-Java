v1.2.2
======
* Completely removed 30 second exclude range - it was excluding players that are active, and none of the players being excluded are inactive.
* DB field `artbook` renamed to `arrartbook`
* Added parsing for owners of Encyclopedia Eorzea
* Added parsing for owners of Heavensward Lore book 1
* Added parsing for owners of Heavensward Lore book 2
* Added parsing for owners of Topaz Carbuncle plush
* Added parsing for owners of Emerald Carbuncle plush

v1.2.1
======
* Fixed issue with 'active' players not being defined as active because their profile had been updated in the 5 mins before the run (we built this is in as a feature to stop us defining inactive players as active)

v1.2.0
======
* Added testing of player activity based on last-modified date of full body image on lodestone

v1.1.0
=======
* Added check for 3.3 completion (Wind-up Aymeric)
* Improvements to test suite stability

v1.0.0
=======
* Initial stable release

