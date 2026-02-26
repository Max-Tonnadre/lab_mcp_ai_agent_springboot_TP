FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
# Copier tout le dépôt pour que le wrapper mvnw et le pom soient accessibles
COPY . .

# Rendre mvnw exécutable et construire le projet (pom.xml à la racine)
RUN chmod +x ./mvnw && \
    ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copier le jar produit par Maven depuis le répertoire target à la racine
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
