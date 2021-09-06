function commit() {
  TIMESTAMP=$(date)
  git add .
  git commit -m "$TIMESTAMP"
}

while true
do
  commit
  sleep 60
done