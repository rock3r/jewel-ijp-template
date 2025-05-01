tasks {
  named<Wrapper>("wrapper") {
    gradleVersion = project.property("gradleVersion") as String?
    distributionType = Wrapper.DistributionType.ALL
  }
}
