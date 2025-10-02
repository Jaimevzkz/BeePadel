#!/bin/bash

if [ "$#" -lt 2 ] || [ "$#" -gt 3 ]; then
  echo "Usage: $0 <version> <mobile|wear> [mobile|wear]"
  exit 1
fi

if [[ ! "$1" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Invalid version name, it should follow the *.*.* regex"
    exit 1
fi

branchName=$1/release

git checkout master
git checkout -b $branchName
git push origin $branchName

util/triggerDeployWorkflow.sh $2 $3

git checkout master
