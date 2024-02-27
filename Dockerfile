FROM ubuntu:latest
COPY target/social-media-app /social-media-app
CMD ["/social-media-app", "seed"]