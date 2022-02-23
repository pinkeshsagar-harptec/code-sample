package com.example.rssfeedissue;

import com.rometools.rome.feed.synd.SyndEntry;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.feed.dsl.Feed;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.MessageChannel;

@EnableIntegration
@Configuration
public class IntegrationRssFetch {

  @Value("https://www.reutersagency.com/feed/?post_type=reuters-best")
  private UrlResource urlResource;

  @Value("https://www.reutersagency.com/feed/?post_type=reuters-best")
  private URL url;

  @Bean
  public MetadataStore metadataStore() {
    PropertiesPersistingMetadataStore metadataStore = new PropertiesPersistingMetadataStore();
    metadataStore.setBaseDirectory("src/main/resources");
    return metadataStore;
  }

  @Bean
  public MessageChannel rssOutputChannel() {
    return MessageChannels.direct("rss_feed_flow").get();
  }

  @Bean
  public IntegrationFlow feedFlow() {
    return IntegrationFlows
        .from(Feed.inboundAdapter(this.urlResource, "feedTest")
                .metadataStore(metadataStore()),
            e -> e.poller(p -> p.fixedDelay(100)))
        .channel("rss_feed_flow")
        .get();
  }

  @Bean
  public IntegrationFlow rssReadFlow() {
    return IntegrationFlows
        .from("rss_feed_flow")
        .handle(message -> {
          SyndEntry entry = (SyndEntry) message.getPayload();
          System.out.println(entry.getTitle());
        })
        .get();
  }

}
