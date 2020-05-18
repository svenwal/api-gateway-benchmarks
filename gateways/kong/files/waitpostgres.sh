#!/bin/bash

check=1
timelimit=180

timeout=$timelimit
while [ $check -gt 0 ]
do
    check=`nc 127.0.0.1 5432 < /dev/null 2> /dev/null; echo $?`
    timeout=$(( $timeout - 1 ))
    if [ $timeout -eq 0 ]
    then
        echo "Postgres not yet up, aborting."
        exit 1
    fi
    sleep 1s
done

timediff=$(( $timelimit - $timeout ))
echo "Postgres started after $timediff seconds"
exit 0
