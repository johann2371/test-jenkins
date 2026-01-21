# ====== STAGE 1 : BUILD ======
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. Copier les fichiers de configuration Maven
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

# 2. Copier le code source et les fichiers de configuration
COPY src/ src/

# 3. Build l'application
RUN ./mvnw clean package -DskipTests -B

# ====== STAGE 2 : APPLICATION ======
FROM eclipse-temurin:17-jre-alpine

# Labels
LABEL maintainer="dev@mboaspot.com"
LABEL version="1.0"
LABEL description="Backend Mboaspot API"

# Arguments
ARG APP_VERSION=1.0.0
ARG BUILD_DATE
ARG VCS_REF
ARG PROFILE=prod

# Labels Docker
LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="mboaspot-backend" \
      org.label-schema.description="Backend API for Mboaspot" \
      org.label-schema.url="https://mboaspot.com" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.vcs-url="https://gitlab.com/logan1xl-group/mboaspot" \
      org.label-schema.version=$APP_VERSION \
      org.label-schema.schema-version="1.0"

# Créer utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copier le JAR depuis le stage builder
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Copier les fichiers de configuration selon le profile
COPY --from=builder --chown=spring:spring /app/src/main/resources/application-${PROFILE}.properties application.properties

# Variables d'environnement avec profile dynamique
ENV SPRING_PROFILES_ACTIVE=${PROFILE}
ENV MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,prometheus,metrics
ENV MANAGEMENT_METRICS_EXPORT_PROMETHEUS_ENABLED=true
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"

# Ports
EXPOSE 8003 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8003/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
