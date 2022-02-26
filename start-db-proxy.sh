#!/usr/bin/env bash
echo "Copying config from dev environment to local device"
scp host.ffxivcensus.com:/opt/ffxivcensus/dev/config.xml config.xml
echo "Starting SSH tunnel to ffxivcensus host"
ssh -L localhost:3306:127.0.0.1:3306 -N host.ffxivcensus.com
