FROM openjdk:8
ADD target/search-facade.jar search-facade.jar
EXPOSE 6868
ENTRYPOINT ["java", "-jar", "search-facade.jar"]