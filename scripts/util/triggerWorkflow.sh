#!/bin/bash

if [ "$#" -lt 1 ] || [ "$#" -gt 2 ]; then
  echo "Usage: $0 <mobile|wear> [mobile|wear]"
  exit 1
fi

# Get the current branch name
current_branch=$(git rev-parse --abbrev-ref HEAD)

run_workflow() {
  case "$1" in
    mobile)
      echo "Running Mobile Deployment Workflow on branch $current_branch..."
      gh workflow run "Mobile Deployment Workflow" --ref "$current_branch"
      ;;
    wear)
      echo "Running Wear Deployment Workflow on branch $current_branch..."
      gh workflow run "Wear Deployment Workflow" --ref "$current_branch"
      ;;
    *)
      echo "Invalid argument: $1. Allowed values are 'mobile' or 'wear'."
      exit 1
      ;;
  esac
}

for arg in "$@"; do
  run_workflow "$arg"
done

