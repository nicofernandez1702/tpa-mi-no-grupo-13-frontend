set -e

echo "Compilando front (skip tests)..."
mvn clean package -DskipTests

echo "Levantando demo..."

nohup java -jar target/demo-0.0.1-SNAPSHOT.jar > demo.log 2>&1 &

echo "Demo levantado en segundo plano"