package com.challenge4.challenge4.infra.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import software.amazon.awssdk.regions.Region;

@Component
@Getter
class AwsConfig {
    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    private Region region = Region.US_EAST_1;

}