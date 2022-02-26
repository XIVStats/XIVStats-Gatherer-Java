#!/usr/bin/env bash
ROOT_FILE="$PWD/pom.xml"
RES_DIR="$PWD/src/test/resources/data/lodestone"
echo $ROOT_FILE
if test -f "$ROOT_FILE"; then
	echo "Enter the character ID to save test data for?"
	read ID
	curl https://eu.finalfantasyxiv.com/lodestone/character/$ID --output $RES_DIR/Character-$ID.html
	curl https://eu.finalfantasyxiv.com/lodestone/character/$ID/minion --output $RES_DIR/Character-$ID-Minions.html
	curl https://eu.finalfantasyxiv.com/lodestone/character/$ID/mount --output $RES_DIR/Character-$ID-Mounts.html
	curl https://eu.finalfantasyxiv.com/lodestone/character/$ID/class_job --output $RES_DIR/Character-$ID-Class-Jobs.html
	git add src/test/resources/data/lodestone/Character-$ID*
	echo "DONE: test data for character $ID downloaded and staged to git"
else
	echo "Not running at root directory, please re-run the script from the root directory of the xivstats-gatherer-java project"
fi


