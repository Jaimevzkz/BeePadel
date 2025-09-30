
#!/bin/bash

# Check if at least 1 argument is provided
if [ "$#" -lt 1 ] || [ "$#" -gt 2 ]; then
  echo "Usage: $0 <mobile|wear> [mobile|wear]"
  exit 1
fi

run_workflow() {
  case "$1" in
    mobile)
      echo "Running Mobile Deployment Workflow..."
      gh workflow run "Mobile Deployment Workflow"
      ;;
    wear)
      echo "Running Wear Deployment Workflow..."
      gh workflow run "Wear Deployment Workflow"
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
