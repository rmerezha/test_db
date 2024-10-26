FROM alpine:3.20 as build

WORKDIR /build

RUN apk add maven openjdk21

COPY . .

RUN mvn package

FROM tomcat:10.1.24

COPY --from=build /build/target/ROOT.war webapps