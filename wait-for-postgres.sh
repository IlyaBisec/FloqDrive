#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

echo "Waiting for PostgreSQL at $host:5432..."

until nc -z "$host" 5432; do
  echo "Postgres unavailable - sleeping"
  sleep 2
done

echo "Postgres is up"
exec $cmd
