variable "TAG" {
  default = "latest"
}

group "default" {
  targets = ["basket-service"]
}

target "basket-service" {
  dockerfile = "./src/main/docker/Dockerfile.jvm"
  tags       = ["localhost:7000/basket-service:${TAG}", "localhost:7000/basket-service:latest"]
}
